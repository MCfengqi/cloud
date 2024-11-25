/**
 * 游戏管理Servlet
 * 用途：处理游戏相关的所有后端请求，包括：
 * 1. 获取游戏列表
 * 2. 添加新游戏
 * 3. 更新游戏信息
 * 4. 删除游戏
 * 5. 处理游戏图片上传
 */
package com.example.cloudcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.sql.*;
import java.util.*;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 1024 * 1024 * 10,  // 10 MB
    maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class GameManageServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity";
    private static final String USER = "cloudcity";
    private static final String PASS = "cloudcity";
    private static final String UPLOAD_DIR = "game_images";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "list":
                    listGames(response);
                    break;
                case "get":
                    getGame(request, response);
                    break;
                default:
                    sendError(response, "Unknown action: " + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "add":
                    addGame(request, response);
                    break;
                case "update":
                    updateGame(request, response);
                    break;
                case "delete":
                    deleteGame(request, response);
                    break;
                default:
                    sendError(response, "Unknown action: " + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, e.getMessage());
        }
    }

    private void listGames(HttpServletResponse response) throws SQLException, IOException {
        List<Map<String, Object>> games = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM gamelist ORDER BY created_at DESC");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> game = new HashMap<>();
                game.put("gameid", rs.getLong("gameid"));
                game.put("gamename", rs.getString("gamename"));
                game.put("gameimg", rs.getString("gameimg"));
                game.put("gametxt", rs.getString("gametxt"));
                game.put("gamelink", rs.getString("gamelink"));
                game.put("created_at", rs.getTimestamp("created_at"));
                game.put("updated_at", rs.getTimestamp("updated_at"));
                games.add(game);
            }
        }
        
        new Gson().toJson(games, response.getWriter());
    }

    private void getGame(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        long gameId = Long.parseLong(request.getParameter("id"));
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM gamelist WHERE gameid = ?")) {
            
            stmt.setLong(1, gameId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Map<String, Object> game = new HashMap<>();
                game.put("gameid", rs.getLong("gameid"));
                game.put("gamename", rs.getString("gamename"));
                game.put("gameimg", rs.getString("gameimg"));
                game.put("gametxt", rs.getString("gametxt"));
                game.put("gamelink", rs.getString("gamelink"));
                game.put("created_at", rs.getTimestamp("created_at"));
                game.put("updated_at", rs.getTimestamp("updated_at"));
                
                new Gson().toJson(game, response.getWriter());
            } else {
                sendError(response, "Game not found");
            }
        }
    }

    private void addGame(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        // 获取表单数据
        String gamename = request.getParameter("gamename");
        String gametxt = request.getParameter("gametxt");
        String gamelink = request.getParameter("gamelink");
        
        // 处理图片上传
        Part filePart = request.getPart("gameimg");
        String fileName = getSubmittedFileName(filePart);
        String gameimg = saveFile(filePart, fileName);
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO gamelist (gamename, gameimg, gametxt, gamelink, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, NOW(), NOW())")) {
            
            stmt.setString(1, gamename);
            stmt.setString(2, gameimg);
            stmt.setString(3, gametxt);
            stmt.setString(4, gamelink);
            
            int result = stmt.executeUpdate();
            sendSuccess(response, result > 0);
        }
    }

    private void updateGame(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        long gameId = Long.parseLong(request.getParameter("id"));
        String gamename = request.getParameter("gamename");
        String gametxt = request.getParameter("gametxt");
        String gamelink = request.getParameter("gamelink");
        
        StringBuilder sql = new StringBuilder("UPDATE gamelist SET gamename = ?, gametxt = ?, gamelink = ?");
        
        // 检查是否有新图片上传
        Part filePart = request.getPart("gameimg");
        String gameimg = null;
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = getSubmittedFileName(filePart);
            gameimg = saveFile(filePart, fileName);
            sql.append(", gameimg = ?");
        }
        
        sql.append(", updated_at = NOW() WHERE gameid = ?");
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            stmt.setString(paramIndex++, gamename);
            stmt.setString(paramIndex++, gametxt);
            stmt.setString(paramIndex++, gamelink);
            if (gameimg != null) {
                stmt.setString(paramIndex++, gameimg);
            }
            stmt.setLong(paramIndex, gameId);
            
            int result = stmt.executeUpdate();
            sendSuccess(response, result > 0);
        }
    }

    private void deleteGame(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        long gameId = Long.parseLong(request.getParameter("id"));
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM gamelist WHERE gameid = ?")) {
            
            stmt.setLong(1, gameId);
            int result = stmt.executeUpdate();
            sendSuccess(response, result > 0);
        }
    }

    private String saveFile(Part filePart, String fileName) throws IOException {
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        String filePath = uploadPath + File.separator + uniqueFileName;
        
        try (InputStream input = filePart.getInputStream();
             OutputStream output = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        }
        
        return UPLOAD_DIR + "/" + uniqueFileName;
    }

    private String getSubmittedFileName(Part part) {
        String header = part.getHeader("content-disposition");
        if (header == null) return null;
        for (String token : header.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("success", false);
        json.addProperty("error", message);
        response.getWriter().write(json.toString());
    }

    private void sendSuccess(HttpServletResponse response, boolean success) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("success", success);
        response.getWriter().write(json.toString());
    }
} 