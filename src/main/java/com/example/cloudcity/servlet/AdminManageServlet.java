package com.example.cloudcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.*;

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
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
                case "add":
                    addAdmin(request, response);
                    break;
                case "update":
                    updateAdmin(request, response);
                    break;
                case "delete":
                    deleteAdmin(request, response);
                    break;
                default:
                    response.getWriter().write("{\"error\": \"Unknown action\"}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void listAdmins(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String searchTerm = request.getParameter("search");
        StringBuilder sql = new StringBuilder("SELECT id, username, password, email, mobile FROM users WHERE is_admin = 1");
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append(" AND username LIKE ?");
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
                    .append("\"mobile\":\"").append(rs.getString("mobile")).append("\"")
                    .append("}");
            }
            jsonBuilder.append("]");
            response.getWriter().write(jsonBuilder.toString());
        }
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

    private void updateAdmin(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");
        
        StringBuilder sql = new StringBuilder("UPDATE users SET email = ?, mobile = ?");
        if (password != null && !password.trim().isEmpty()) {
            sql.append(", password = ?");
        }
        sql.append(" WHERE id = ? AND is_admin = 1");
        
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

    private void deleteAdmin(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM users WHERE id = ? AND is_admin = 1")) {
            
            stmt.setLong(1, id);
            
            int result = stmt.executeUpdate();
            response.getWriter().write("{\"success\": " + (result > 0) + "}");
        }
    }

    private void getAdmin(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id, username, email, mobile FROM users WHERE id = ? AND is_admin = 1")) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append("{")
                    .append("\"id\":").append(rs.getLong("id")).append(",")
                    .append("\"username\":\"").append(rs.getString("username")).append("\",")
                    .append("\"email\":\"").append(rs.getString("email")).append("\",")
                    .append("\"mobile\":\"").append(rs.getString("mobile")).append("\"")
                    .append("}");
                response.getWriter().write(jsonBuilder.toString());
            } else {
                response.getWriter().write("{\"error\": \"Admin not found\"}");
            }
        }
    }
} 