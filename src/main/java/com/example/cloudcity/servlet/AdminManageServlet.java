package com.example.cloudcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/AdminManageServlet")
public class AdminManageServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity";
    private static final String USER = "cloudcity";
    private static final String PASS = "cloudcity";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String action = request.getParameter("action");
        
        // 检查是否是管理员
        Boolean isAdmin = (Boolean) request.getSession().getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            response.getWriter().write("{\"error\": \"Unauthorized access\"}");
            return;
        }

        try {
            switch (action) {
                case "list":
                    listAdmins(request, response);
                    break;
                case "get":
                    getAdmin(request, response);
                    break;
                default:
                    response.getWriter().write("{\"error\": \"Unknown action\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        // 检查是否是管理员
        Boolean isAdmin = (Boolean) request.getSession().getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            response.getWriter().write("{\"error\": \"Unauthorized access\"}");
            return;
        }

        try {
            // 如果是 JSON 请求
            if (request.getContentType() != null && request.getContentType().contains("application/json")) {
                // 读取 JSON 数据
                StringBuilder buffer = new StringBuilder();
                String line;
                try (BufferedReader reader = request.getReader()) {
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                }
                
                // 解析 JSON
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(buffer.toString(), JsonObject.class);
                String action = jsonObject.get("action").getAsString();
                
                switch (action) {
                    case "update":
                        updateAdmin(jsonObject, response);
                        break;
                    case "add":
                        addAdmin(request, response);
                        break;
                    case "delete":
                        deleteAdmin(request, response);
                        break;
                    default:
                        response.getWriter().write("{\"error\": \"Unknown action\"}");
                }
            } else {
                // 处理普通表单提交
                String action = request.getParameter("action");
                switch (action) {
                    case "add":
                        addAdmin(request, response);
                        break;
                    case "delete":
                        deleteAdmin(request, response);
                        break;
                    default:
                        response.getWriter().write("{\"error\": \"Unknown action\"}");
                }
            }
        } catch (Exception e) {
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void listAdmins(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            String searchTerm = request.getParameter("search");
            StringBuilder sql = new StringBuilder("SELECT * FROM users WHERE is_admin = 1");
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                sql.append(" AND username LIKE ?");
            }
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.prepareStatement(sql.toString());
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                stmt.setString(1, "%" + searchTerm.trim() + "%");
            }
            
            rs = stmt.executeQuery();
            
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[");
            
            boolean first = true;
            while (rs.next()) {
                if (!first) {
                    jsonBuilder.append(",");
                }
                first = false;
                
                // 获取管理员信息
                long id = rs.getLong("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String mobile = rs.getString("mobile");
                
                // 构建 JSON 对象
                jsonBuilder.append("{")
                    .append("\"id\":").append(id).append(",")
                    .append("\"username\":\"").append(escapeJson(username)).append("\",")
                    .append("\"password\":\"").append(escapeJson(password)).append("\",")
                    .append("\"email\":\"").append(escapeJson(email)).append("\",")
                    .append("\"mobile\":\"").append(escapeJson(mobile)).append("\",")
                    .append("\"isAdmin\":true,")
                    .append("\"userType\":\"超级管理员\"")
                    .append("}");
                
                System.out.println("Found admin: " + username + ", ID: " + id);
            }
            
            jsonBuilder.append("]");
            
            String jsonResponse = jsonBuilder.toString();
            System.out.println("JSON Response: " + jsonResponse);
            
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jsonResponse);
            
        } catch (Exception e) {
            System.err.println("Error in listAdmins: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("[{\"error\":\"" + escapeJson(e.getMessage()) + "\"}]");
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // 添加 escapeJson 方法
    private String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\b", "\\b")
                    .replace("\f", "\\f")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }

    private void addAdmin(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (username, password, email, mobile, is_admin) VALUES (?, ?, ?, ?, 1)")) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, mobile);
            
            int result = stmt.executeUpdate();
            response.getWriter().write("{\"success\": " + (result > 0) + "}");
        }
    }

    private void updateAdmin(JsonObject data, HttpServletResponse response) 
            throws SQLException, IOException {
        long id = data.get("id").getAsLong();
        String email = data.get("email").getAsString();
        String mobile = data.get("mobile").getAsString();
        boolean isAdmin = data.get("isAdmin").getAsBoolean();
        String password = data.has("password") ? data.get("password").getAsString() : null;
        
        System.out.println("Updating admin - ID: " + id + ", isAdmin: " + isAdmin);
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
            // 首先检查是否是最后一个管理员
            if (!isAdmin) {
                String countSql = "SELECT COUNT(*) FROM users WHERE is_admin = 1 AND id != ?";
                try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
                    countStmt.setLong(1, id);
                    ResultSet rs = countStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        response.getWriter().write("{\"success\": false, \"error\": \"不能将最后一个管理员改为普通用户！\"}");
                        return;
                    }
                }
            }
            
            // 构建更新 SQL
            StringBuilder sql = new StringBuilder("UPDATE users SET email = ?, mobile = ?, is_admin = ?");
            if (password != null && !password.trim().isEmpty()) {
                sql.append(", password = ?");
            }
            sql.append(" WHERE id = ?");
            
            stmt = conn.prepareStatement(sql.toString());
            
            // 设置参数
            int paramIndex = 1;
            stmt.setString(paramIndex++, email);
            stmt.setString(paramIndex++, mobile);
            stmt.setInt(paramIndex++, isAdmin ? 1 : 0);
            
            if (password != null && !password.trim().isEmpty()) {
                stmt.setString(paramIndex++, password);
            }
            stmt.setLong(paramIndex, id);
            
            // 执行更新
            int result = stmt.executeUpdate();
            
            System.out.println("Update result: " + result);
            
            response.getWriter().write("{\"success\": " + (result > 0) + "}");
            
        } catch (Exception e) {
            System.err.println("Error updating admin: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"error\": \"" + escapeJson(e.getMessage()) + "\"}");
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    private void deleteAdmin(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        
        // 检查是否是最后一个管理员
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // 首先检查管理员总数
            String countSql = "SELECT COUNT(*) FROM users WHERE is_admin = 1";
            try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
                ResultSet rs = countStmt.executeQuery();
                if (rs.next() && rs.getInt(1) <= 1) {
                    response.getWriter().write("{\"success\": false, \"error\": \"不能删除最后一个管理员！\"}");
                    return;
                }
            }
            
            // 如果不是最后一个管理员，则执行删除
            String deleteSql = "DELETE FROM users WHERE id = ? AND is_admin = 1";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setLong(1, id);
                int result = deleteStmt.executeUpdate();
                response.getWriter().write("{\"success\": " + (result > 0) + "}");
            }
        }
    }

    private void getAdmin(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE id = ? AND is_admin = 1")) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // 构建完整的用户信息
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append("{")
                    .append("\"id\":").append(rs.getLong("id")).append(",")
                    .append("\"username\":\"").append(escapeJson(rs.getString("username"))).append("\",")
                    .append("\"password\":\"").append(escapeJson(rs.getString("password"))).append("\",")
                    .append("\"email\":\"").append(escapeJson(rs.getString("email"))).append("\",")
                    .append("\"mobile\":\"").append(escapeJson(rs.getString("mobile"))).append("\",")
                    .append("\"isAdmin\":").append(rs.getInt("is_admin") == 1).append(",")
                    .append("\"userType\":\"").append(rs.getInt("is_admin") == 1 ? "超级管理员" : "普通用户").append("\"")
                    .append("}");

                System.out.println("Admin JSON response: " + jsonBuilder.toString());
                response.getWriter().write(jsonBuilder.toString());
            } else {
                response.getWriter().write("{\"error\": \"Admin not found\"}");
            }
        } catch (Exception e) {
            System.err.println("Error in getAdmin: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        }
    }
} 