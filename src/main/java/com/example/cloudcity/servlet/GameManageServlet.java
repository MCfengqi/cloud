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
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.sql.*;
import java.util.*;

public class GameManageServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity";
    private static final String USER = "cloudcity";
    private static final String PASS = "cloudcity";

    private final Gson gson = new Gson();

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
        
        try {
            // 读取JSON数据
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            
            // 解析JSON数据
            JsonObject jsonData = gson.fromJson(sb.toString(), JsonObject.class);
            String action = jsonData.get("action").getAsString();

            switch (action) {
                case "add":
                    addGame(jsonData, response);
                    break;
                case "update":
                    updateGame(jsonData, response);
                    break;
                case "delete":
                    deleteGame(jsonData, response);
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
        
        // 添加调试日志
        System.out.println("Games data: " + new Gson().toJson(games));
        
        response.getWriter().write(new Gson().toJson(games));
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

    private void addGame(JsonObject jsonData, HttpServletResponse response) 
            throws SQLException, IOException {
        String gamename = jsonData.get("gamename").getAsString();
        String gameimg = jsonData.get("gameimg").getAsString();
        String gametxt = jsonData.get("gametxt").getAsString();
        String gamelink = jsonData.get("gamelink").getAsString();
        
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

    private void updateGame(JsonObject jsonData, HttpServletResponse response) 
            throws SQLException, IOException {
        long gameId = jsonData.get("id").getAsLong();
        String gamename = jsonData.get("gamename").getAsString();
        String gameimg = jsonData.get("gameimg").getAsString();
        String gametxt = jsonData.get("gametxt").getAsString();
        String gamelink = jsonData.get("gamelink").getAsString();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE gamelist SET gamename = ?, gameimg = ?, gametxt = ?, " +
                     "gamelink = ?, updated_at = NOW() WHERE gameid = ?")) {
            
            stmt.setString(1, gamename);
            stmt.setString(2, gameimg);
            stmt.setString(3, gametxt);
            stmt.setString(4, gamelink);
            stmt.setLong(5, gameId);
            
            int result = stmt.executeUpdate();
            sendSuccess(response, result > 0);
        }
    }

    private void deleteGame(JsonObject jsonData, HttpServletResponse response) 
            throws SQLException, IOException {
        long gameId = jsonData.get("id").getAsLong();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM gamelist WHERE gameid = ?")) {
            
            stmt.setLong(1, gameId);
            int result = stmt.executeUpdate();
            
            JsonObject response_data = new JsonObject();
            if (result > 0) {
                response_data.addProperty("success", true);
                response_data.addProperty("message", "游戏删除成功");
            } else {
                response_data.addProperty("success", false);
                response_data.addProperty("error", "未找到要删除的游戏");
            }
            response.getWriter().write(gson.toJson(response_data));
        } catch (SQLException e) {
            JsonObject error_response = new JsonObject();
            error_response.addProperty("success", false);
            error_response.addProperty("error", "删除游戏时发生错误: " + e.getMessage());
            response.getWriter().write(gson.toJson(error_response));
        }
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