package com.example.cloudcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/UserManageServlet")
public class UserManageServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity";
    private static final String USER = "cloudcity";
    private static final String PASS = "cloudcity";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String action = request.getParameter("action");
        
        try {
            switch (action) {
                case "list":
                    listUsers(request, response);
                    break;
                case "get":
                    getUser(request, response);
                    break;
                default:
                    response.getWriter().write("{\"error\": \"Unknown action\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
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
        String searchTerm = request.getParameter("search");
        StringBuilder sql = new StringBuilder("SELECT id, username, password, email, mobile, is_admin FROM users");
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append(" WHERE username LIKE ?");
        }
        
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                stmt.setString(1, "%" + searchTerm.trim() + "%");
            }
            
            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                if (!first) {
                    jsonBuilder.append(",");
                }
                first = false;
                jsonBuilder.append("{")
                    .append("\"id\":").append(rs.getLong("id")).append(",")
                    .append("\"username\":\"").append(rs.getString("username")).append("\",")
                    .append("\"password\":\"").append(rs.getString("password")).append("\",")
                    .append("\"email\":\"").append(rs.getString("email")).append("\",")
                    .append("\"mobile\":\"").append(rs.getString("mobile")).append("\",")
                    .append("\"isAdmin\":").append(rs.getBoolean("is_admin"))
                    .append("}");
            }
            jsonBuilder.append("]");
            response.getWriter().write(jsonBuilder.toString());
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
        long id = Long.parseLong(request.getParameter("id"));
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM users WHERE id = ?")) {
            
            stmt.setLong(1, id);
            
            int result = stmt.executeUpdate();
            response.getWriter().write("{\"success\": " + (result > 0) + "}");
        }
    }

    private void getUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id, username, email, mobile, is_admin FROM users WHERE id = ?")) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append("{")
                    .append("\"id\":").append(rs.getLong("id")).append(",")
                    .append("\"username\":\"").append(rs.getString("username")).append("\",")
                    .append("\"email\":\"").append(rs.getString("email")).append("\",")
                    .append("\"mobile\":\"").append(rs.getString("mobile")).append("\",")
                    .append("\"isAdmin\":").append(rs.getBoolean("is_admin"))
                    .append("}");
                response.getWriter().write(jsonBuilder.toString());
            } else {
                response.getWriter().write("{\"error\": \"User not found\"}");
            }
        }
    }
} 