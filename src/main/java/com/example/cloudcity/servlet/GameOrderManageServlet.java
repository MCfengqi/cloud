package com.example.cloudcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.example.cloudcity.utils.LogUtils;

import java.io.*;
import java.sql.*;
import java.util.*;

public class GameOrderManageServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity";
    private static final String USER = "root";
    private static final String PASS = "123456";
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "list":
                    listOrders(response);
                    break;
                case "get":
                    getOrder(request, response);
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
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            
            JsonObject jsonData = gson.fromJson(sb.toString(), JsonObject.class);
            String action = jsonData.get("action").getAsString();

            switch (action) {
                case "add":
                    addOrder(jsonData, request, response);
                    break;
                case "update":
                    updateOrder(jsonData, request, response);
                    break;
                case "delete":
                    deleteOrder(jsonData, request, response);
                    break;
                default:
                    sendError(response, "Unknown action: " + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, e.getMessage());
        }
    }

    private void listOrders(HttpServletResponse response) throws SQLException, IOException {
        List<Map<String, Object>> orders = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT o.*, u.username FROM game_order o " +
                     "JOIN users u ON o.user_id = u.id " +
                     "ORDER BY o.datetime DESC")) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> order = new HashMap<>();
                order.put("id", rs.getLong("id"));
                order.put("total", rs.getBigDecimal("total"));
                order.put("amount", rs.getBigDecimal("amount"));
                order.put("status", rs.getString("status"));
                order.put("paytype", rs.getString("paytype"));
                order.put("gamename", rs.getString("gamename"));
                order.put("gameimg", rs.getString("gameimg"));
                order.put("gametxt", rs.getString("gametxt"));
                order.put("datetime", rs.getTimestamp("datetime"));
                order.put("user_id", rs.getLong("user_id"));
                orders.add(order);
            }
        }
        
        response.getWriter().write(gson.toJson(orders));
    }

    private void getOrder(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        long orderId = Long.parseLong(request.getParameter("id"));
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT o.*, u.username FROM game_order o " +
                     "JOIN users u ON o.user_id = u.id " +
                     "WHERE o.id = ?")) {
            
            stmt.setLong(1, orderId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Map<String, Object> order = new HashMap<>();
                order.put("id", rs.getLong("id"));
                order.put("total", rs.getBigDecimal("total"));
                order.put("amount", rs.getBigDecimal("amount"));
                order.put("status", rs.getString("status"));
                order.put("paytype", rs.getString("paytype"));
                order.put("gamename", rs.getString("gamename"));
                order.put("gameimg", rs.getString("gameimg"));
                order.put("gamelink", rs.getString("gamelink"));
                order.put("datetime", rs.getTimestamp("datetime"));
                order.put("username", rs.getString("username"));
                
                response.getWriter().write(gson.toJson(order));
            } else {
                sendError(response, "Order not found");
            }
        }
    }

    // 其他必要的方法实现...
    private void addOrder(JsonObject jsonData, HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO game_order (total, amount, status, paytype, gamename, gameimg, gamelink, user_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            
            stmt.setBigDecimal(1, jsonData.get("total").getAsBigDecimal());
            stmt.setBigDecimal(2, jsonData.get("amount").getAsBigDecimal());
            stmt.setString(3, jsonData.get("status").getAsString());
            stmt.setString(4, jsonData.get("paytype").getAsString());
            stmt.setString(5, jsonData.get("gamename").getAsString());
            stmt.setString(6, jsonData.get("gameimg").getAsString());
            stmt.setString(7, jsonData.get("gamelink").getAsString());
            stmt.setLong(8, jsonData.get("user_id").getAsLong());
            
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                LogUtils.logOperation(
                    "添加订单",
                    "添加新订单: " + jsonData.get("gamename").getAsString(),
                    (String) request.getSession().getAttribute("username"),
                    request,
                    "成功"
                );
                response.getWriter().write("{\"success\": true}");
            } else {
                sendError(response, "Failed to add order");
            }
        }
    }

    private void updateOrder(JsonObject jsonData, HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        try {
            long orderId = jsonData.get("id").getAsLong();
            String status = jsonData.get("status").getAsString();
            
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE game_order SET status = ? WHERE id = ?")) {
                
                stmt.setString(1, status);
                stmt.setLong(2, orderId);
                
                int result = stmt.executeUpdate();
                
                if (result > 0) {
                    // 修改日志记录，使其更详细
                    LogUtils.logOperation(
                        "更新订单",
                        "更新订单ID" + orderId + "状态为" + status,
                        (String) request.getSession().getAttribute("username"),
                        request,
                        "成功"
                    );
                    
                    // 返回成功响应
                    JsonObject response_data = new JsonObject();
                    response_data.addProperty("success", true);
                    response_data.addProperty("message", "订单状态更新成功");
                    response.getWriter().write(gson.toJson(response_data));
                } else {
                    JsonObject error_response = new JsonObject();
                    error_response.addProperty("success", false);
                    error_response.addProperty("error", "未找到要更新的订单");
                    response.getWriter().write(gson.toJson(error_response));
                }
            }
        } catch (Exception e) {
            System.err.println("Error updating order: " + e.getMessage());
            e.printStackTrace();
            JsonObject error_response = new JsonObject();
            error_response.addProperty("success", false);
            error_response.addProperty("error", e.getMessage());
            response.getWriter().write(gson.toJson(error_response));
        }
    }

    private void deleteOrder(JsonObject jsonData, HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        try {
            long orderId = jsonData.get("id").getAsLong();
            
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement stmt = conn.prepareStatement(
                         "DELETE FROM game_order WHERE id = ?")) {
                
                stmt.setLong(1, orderId);
                int result = stmt.executeUpdate();
                
                if (result > 0) {
                    // 添加日志记录
                    LogUtils.logOperation(
                        "删除订单",
                        "删除订单ID: " + orderId,
                        (String) request.getSession().getAttribute("username"),
                        request,
                        "成功"
                    );
                    
                    // 返回成功响应
                    JsonObject response_data = new JsonObject();
                    response_data.addProperty("success", true);
                    response_data.addProperty("message", "订单删除成功");
                    response.getWriter().write(gson.toJson(response_data));
                } else {
                    JsonObject error_response = new JsonObject();
                    error_response.addProperty("success", false);
                    error_response.addProperty("error", "未找到要删除的订单");
                    response.getWriter().write(gson.toJson(error_response));
                }
            }
        } catch (Exception e) {
            System.err.println("Error deleting order: " + e.getMessage());
            e.printStackTrace();
            JsonObject error_response = new JsonObject();
            error_response.addProperty("success", false);
            error_response.addProperty("error", e.getMessage());
            response.getWriter().write(gson.toJson(error_response));
        }
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("success", false);
        json.addProperty("error", message);
        response.getWriter().write(json.toString());
    }
} 