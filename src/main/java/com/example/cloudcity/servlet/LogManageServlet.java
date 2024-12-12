package com.example.cloudcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/LogManageServlet")
public class LogManageServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity";
    private static final String USER = "root";
    private static final String PASS = "123456";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "list":
                    listLogs(request, response);
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
                    addLog(request, response);
                    break;
                case "delete":
                    deleteLog(request, response);
                    break;
                default:
                    sendError(response, "Unknown action: " + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, e.getMessage());
        }
    }

    private void listLogs(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String searchText = request.getParameter("searchText");

        System.out.println("Listing logs with parameters:");
        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + endDate);
        System.out.println("searchText: " + searchText);

        List<Map<String, Object>> logs = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT * FROM operation_logs WHERE 1=1");

        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND DATE(created_at) >= ?");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append(" AND DATE(created_at) <= ?");
        }
        if (searchText != null && !searchText.isEmpty()) {
            sql.append(" AND (operation_content LIKE ? OR username LIKE ? OR operation_type LIKE ?)");
        }
        sql.append(" ORDER BY created_at DESC");

        System.out.println("Executing SQL: " + sql.toString());

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (startDate != null && !startDate.isEmpty()) {
                stmt.setString(paramIndex++, startDate);
            }
            if (endDate != null && !endDate.isEmpty()) {
                stmt.setString(paramIndex++, endDate);
            }
            if (searchText != null && !searchText.isEmpty()) {
                String searchPattern = "%" + searchText + "%";
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex, searchPattern);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> log = new HashMap<>();
                log.put("id", rs.getLong("id"));
                log.put("operation_type", rs.getString("operation_type"));
                log.put("operation_content", rs.getString("operation_content"));
                log.put("username", rs.getString("username"));
                log.put("ip_address", rs.getString("ip_address"));
                log.put("created_at", rs.getTimestamp("created_at").toString());
                log.put("result", rs.getString("result"));
                logs.add(log);
            }

            System.out.println("Found " + logs.size() + " logs");
        }

        String jsonResponse = new Gson().toJson(logs);
        System.out.println("Sending response: " + jsonResponse);
        response.getWriter().write(jsonResponse);
    }

    private void addLog(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        String operationType = request.getParameter("operation_type");
        String operationContent = request.getParameter("operation_content");
        String username = request.getParameter("username");
        String ipAddress = request.getRemoteAddr();
        String result = request.getParameter("result");

        String sql = "INSERT INTO operation_logs (operation_type, operation_content, username, ip_address, result) " +
                    "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, operationType);
            stmt.setString(2, operationContent);
            stmt.setString(3, username);
            stmt.setString(4, ipAddress);
            stmt.setString(5, result);

            int rowsAffected = stmt.executeUpdate();
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("success", rowsAffected > 0);
            response.getWriter().write(jsonResponse.toString());
        }
    }

    private void deleteLog(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));

        String sql = "DELETE FROM operation_logs WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("success", rowsAffected > 0);
            response.getWriter().write(jsonResponse.toString());
        }
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("error", message);
        response.getWriter().write(json.toString());
    }
} 