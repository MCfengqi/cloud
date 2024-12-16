/**
 * 用户管理Servlet
 * 用途：处理用户相关的所有后端请求，包括：
 * 1. 用户列表的获取
 * 2. 用户信息的添加
 * 3. 用户信息的修改
 * 4. 用户的删除
 * 5. 用户权限的管理
 */
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
import com.example.cloudcity.utils.LogUtils;

public class
UserManageServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity";
    private static final String USER = "root";
    private static final String PASS = "123456";

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
                        updateUser(jsonObject, request, response);
                        break;
                    case "add":
                        addUserFromJson(jsonObject, request, response);
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
                        addUserFromForm(request, response);
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
                Timestamp created_at = rs.getTimestamp("created_at");
                Timestamp updated_at = rs.getTimestamp("updated_at");
                
                jsonBuilder.append("{")
                    .append("\"id\":").append(id).append(",")
                    .append("\"username\":\"").append(escapeJson(username)).append("\",")
                    .append("\"password\":\"").append(escapeJson(password)).append("\",")
                    .append("\"email\":\"").append(escapeJson(email)).append("\",")
                    .append("\"mobile\":\"").append(escapeJson(mobile)).append("\",")
                    .append("\"created_at\":\"").append(created_at != null ? created_at.toString() : "").append("\",")
                    .append("\"updated_at\":\"").append(updated_at != null ? updated_at.toString() : "").append("\",")
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

    private void addUserFromJson(JsonObject jsonObject, HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        System.out.println("Received data: " + jsonObject.toString());
        
        // 获取并验证必填字段
        if (!jsonObject.has("username") || !jsonObject.has("password") || 
            !jsonObject.has("email") || !jsonObject.has("mobile")) {
            response.getWriter().write("{\"success\": false, \"error\": \"缺少必填字段\"}");
            return;
        }
        
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();
        String email = jsonObject.get("email").getAsString();
        String mobile = jsonObject.get("mobile").getAsString();
        boolean isAdmin = jsonObject.has("isAdmin") && jsonObject.get("isAdmin").getAsBoolean();
        
        // 验证字段不为空
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            response.getWriter().write("{\"success\": false, \"error\": \"用户名和密码不能为空\"}");
            return;
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO users (username, password, email, mobile, is_admin, created_at, updated_at) " +
                        "VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username.trim());
                stmt.setString(2, password.trim());
                stmt.setString(3, email.trim());
                stmt.setString(4, mobile.trim());
                stmt.setBoolean(5, isAdmin);
                
                int result = stmt.executeUpdate();
                if (result > 0) {
                    LogUtils.logOperation(
                        "添加用户",
                        "添加用户: " + username,
                        (String) request.getSession().getAttribute("username"),
                        request,
                        "成功"
                    );
                }
                response.getWriter().write("{\"success\": " + (result > 0) + "}");
            }
        } catch (SQLException e) {
            System.err.println("Error in addUser: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"error\": \"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private void addUserFromForm(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        boolean isAdmin = "1".equals(request.getParameter("userType"));
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO users (username, password, email, mobile, is_admin, created_at, updated_at) " +
                        "VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, email);
                stmt.setString(4, mobile);
                stmt.setBoolean(5, isAdmin);
                
                int result = stmt.executeUpdate();
                if (result > 0) {
                    LogUtils.logOperation(
                        "添加用户",
                        "添加用户: " + username,
                        (String) request.getSession().getAttribute("username"),
                        request,
                        "成功"
                    );
                }
                response.getWriter().write("{\"success\": " + (result > 0) + "}");
            }
        }
    }

    private void updateUser(JsonObject data, HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        try {
            long id = data.get("id").getAsLong();
            String username = data.get("username").getAsString();
            String email = data.get("email").getAsString();
            String mobile = data.get("mobile").getAsString();
            boolean isAdmin = data.has("is_admin") ? data.get("is_admin").getAsBoolean() : false;
            String password = data.has("password") ? data.get("password").getAsString() : null;
            
            System.out.println("Updating user - ID: " + id + ", Username: " + username + ", IsAdmin: " + isAdmin);
            
            Connection conn = null;
            PreparedStatement stmt = null;
            
            try {
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                
                // 构建更新 SQL
                StringBuilder sql = new StringBuilder("UPDATE users SET username = ?, email = ?, mobile = ?, is_admin = ?, updated_at = NOW()");
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
                stmt.setBoolean(paramIndex++, isAdmin);
                
                if (password != null && !password.trim().isEmpty()) {
                    stmt.setString(paramIndex++, password);
                }
                stmt.setLong(paramIndex, id);
                
                // 执行更新
                int result = stmt.executeUpdate();
                if (result > 0) {
                    LogUtils.logOperation(
                        "更新用户",
                        "更新用户: " + username,
                        (String) request.getSession().getAttribute("username"),
                        request,
                        "成功"
                    );
                }
                
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
                if (result > 0) {
                    LogUtils.logOperation(
                        "删除用户",
                        "删除用户ID: " + id,
                        (String) request.getSession().getAttribute("username"),
                        request,
                        "成功"
                    );
                }
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