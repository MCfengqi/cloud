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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String action = request.getParameter("action");
        
        try {
            switch (action) {
                case "add":
                    addUser(request, response);
                    break;
                case "update":
                    updateUser(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                default:
                    response.getWriter().write("{\"error\": \"Unknown action\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
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

    private void updateUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");
        
        StringBuilder sql = new StringBuilder("UPDATE users SET email = ?, mobile = ?");
        if (password != null && !password.trim().isEmpty()) {
            sql.append(", password = ?");
        }
        sql.append(" WHERE id = ?");
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            stmt.setString(1, email);
            stmt.setString(2, mobile);
            if (password != null && !password.trim().isEmpty()) {
                stmt.setString(3, password);
                stmt.setLong(4, id);
            } else {
                stmt.setLong(3, id);
            }
            
            int result = stmt.executeUpdate();
            response.getWriter().write("{\"success\": " + (result > 0) + "}");
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
            
            String sql = "SELECT * FROM users WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            
            System.out.println("Executing SQL: " + sql + " with ID: " + id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", rs.getLong("id"));
                user.put("username", rs.getString("username"));
                user.put("password", rs.getString("password"));
                user.put("email", rs.getString("email"));
                user.put("mobile", rs.getString("mobile"));
                user.put("isAdmin", rs.getInt("is_admin") == 1);
                user.put("userType", rs.getInt("is_admin") == 1 ? "超级管理员" : "普通用户");
                
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(user);
                
                System.out.println("User found: " + jsonResponse);
                response.getWriter().write(jsonResponse);
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