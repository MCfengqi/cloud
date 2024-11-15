package com.example.cloudcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;

@WebServlet("/UserManageServlet")
public class UserManageServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity";
    private static final String USER = "cloudcity";
    private static final String PASS = "cloudcity";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        
        String action = request.getParameter("action");
        System.out.println("Received action: " + action);
        
        try {
            if ("list".equals(action)) {
                listUsers(request, response);
            } else if ("get".equals(action)) {
                getUser(request, response);
            } else {
                response.getWriter().write("{\"error\": \"Unknown action: " + action + "\"}");
            }
        } catch (Exception e) {
            System.err.println("Error in doGet: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"error\": \"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
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
                        updateUser(jsonObject, response);
                        break;
                    case "add":
                        addUser(request, response);
                        break;
                    case "delete":
                        deleteUser(request, response);
                        break;
                    default:
                        response.getWriter().write("{\"success\": false, \"error\": \"Unknown action\"}");
                }
            } else {
                // 处理普通表单提交
                String action = request.getParameter("action");
                switch (action) {
                    case "add":
                        addUser(request, response);
                        break;
                    case "delete":
                        deleteUser(request, response);
                        break;
                    default:
                        response.getWriter().write("{\"success\": false, \"error\": \"Unknown action\"}");
                }
            }
        } catch (Exception e) {
            System.err.println("Error in doPost: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"error\": \"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
            String sql = "SELECT * FROM users WHERE is_admin = 0";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[");
            
            boolean first = true;
            while (rs.next()) {
                if (!first) {
                    jsonBuilder.append(",");
                }
                first = false;
                
                long id = rs.getLong("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String mobile = rs.getString("mobile");
                
                jsonBuilder.append("{")
                    .append("\"id\":").append(id).append(",")
                    .append("\"username\":\"").append(escapeJson(username)).append("\",")
                    .append("\"password\":\"").append(escapeJson(password)).append("\",")
                    .append("\"email\":\"").append(escapeJson(email)).append("\",")
                    .append("\"mobile\":\"").append(escapeJson(mobile)).append("\",")
                    .append("\"isAdmin\":false,")
                    .append("\"userType\":\"普通用户\"")
                    .append("}");
                
                System.out.println("Found user: " + username + ", ID: " + id);
            }
            
            jsonBuilder.append("]");
            
            String jsonResponse = jsonBuilder.toString();
            System.out.println("JSON Response: " + jsonResponse);
            
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jsonResponse);
            
        } catch (Exception e) {
            System.err.println("Error in listUsers: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("[{\"error\":\"" + escapeJson(e.getMessage()) + "\"}]");
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        boolean isAdmin = Boolean.parseBoolean(request.getParameter("isAdmin"));
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (username, password, email, mobile, is_admin) VALUES (?, ?, ?, ?, ?)")) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, mobile);
            stmt.setBoolean(5, isAdmin);
            
            int result = stmt.executeUpdate();
            response.getWriter().write("{\"success\": " + (result > 0) + "}");
        }
    }

    private void updateUser(JsonObject data, HttpServletResponse response) 
            throws SQLException, IOException {
        try {
            long id = data.get("id").getAsLong();
            String username = data.get("username").getAsString();
            String email = data.get("email").getAsString();
            String mobile = data.get("mobile").getAsString();
            boolean isAdmin = data.get("isAdmin").getAsBoolean();
            String password = data.has("password") ? data.get("password").getAsString() : null;
            
            System.out.println("Updating user - ID: " + id + ", Username: " + username + ", IsAdmin: " + isAdmin);
            
            Connection conn = null;
            PreparedStatement stmt = null;
            
            try {
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                
                // 构建更新 SQL
                StringBuilder sql = new StringBuilder("UPDATE users SET username = ?, email = ?, mobile = ?, is_admin = ?");
                if (password != null && !password.trim().isEmpty()) {
                    sql.append(", password = ?");
                }
                sql.append(" WHERE id = ?");
                
                stmt = conn.prepareStatement(sql.toString());
                
                // 设置参数
                int paramIndex = 1;
                stmt.setString(paramIndex++, username);
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
                
            } finally {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"error\": \"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        String idStr = request.getParameter("id");
        
        try {
            long id = Long.parseLong(idStr);
            
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement stmt = conn.prepareStatement(
                         "DELETE FROM users WHERE id = ?")) {
                
                stmt.setLong(1, id);
                
                int result = stmt.executeUpdate();
                String jsonResponse = "{\"success\":" + (result > 0) + "}";
                System.out.println("Delete response: " + jsonResponse);
                response.getWriter().write(jsonResponse);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format: " + idStr);
            response.getWriter().write("{\"success\":false,\"error\":\"Invalid ID format\"}");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            response.getWriter().write("{\"success\":false,\"error\":\"Database error\"}");
        }
    }

    private void getUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            System.out.println("ID parameter is empty or null");
            response.getWriter().write("{\"error\":\"Invalid ID parameter\"}");
            return;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            long id = Long.parseLong(idStr.trim());
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
            // 修改 SQL 查询，移除 is_admin 限制
            String sql = "SELECT * FROM users WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            
            System.out.println("Executing SQL: " + sql + " with ID: " + id);
            rs = stmt.executeQuery();
            
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

                System.out.println("User JSON response: " + jsonBuilder.toString());
                response.getWriter().write(jsonBuilder.toString());
            } else {
                System.out.println("No user found with ID: " + id);
                response.getWriter().write("{\"error\":\"User not found\"}");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format: " + idStr);
            response.getWriter().write("{\"error\":\"Invalid ID format\"}");
        } catch (Exception e) {
            System.err.println("Error getting user: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

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
} 