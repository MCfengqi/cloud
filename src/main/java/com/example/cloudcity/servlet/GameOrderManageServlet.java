/**
 * 游戏订单管理Servlet
 * 用途：处理游戏订单相关的所有后端请求，包括：
 * 1. 订单列表的获取和展示
 * 2. 订单状态的更新和管理
 * 3. 订单的删除操作
 * 4. 订单详情的查看
 * 5. 订单的创建和支付处理
 */
package com.example.cloudcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.example.cloudcity.utils.LogUtils;
import com.example.cloudcity.utils.DatabaseConfig;

import java.io.*;
import java.sql.*;
import java.util.*;

public class GameOrderManageServlet extends HttpServlet {
    /** JSON处理工具 */
    private final Gson gson = new Gson();

    /**
     * 处理GET请求
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
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

    /**
     * 获取订单列表
     * @param response HTTP响应对象
     * @throws SQLException SQL异常
     * @throws IOException IO异常
     */
    private void listOrders(HttpServletResponse response) throws SQLException, IOException {
        List<Map<String, Object>> orders = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.DB_URL, 
                DatabaseConfig.USER, 
                DatabaseConfig.PASS);
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

    /**
     * 获取单个订单详情
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @throws SQLException SQL异常
     * @throws IOException IO异常
     */
    private void getOrder(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        long orderId = Long.parseLong(request.getParameter("id"));
        
        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.DB_URL, 
                DatabaseConfig.USER, 
                DatabaseConfig.PASS);
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

    /**
     * 处理POST请求
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // ... 方法实现
    }

    /**
     * 添加订单
     * @param jsonData 订单数据JSON对象
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @throws SQLException SQL异常
     * @throws IOException IO异常
     */
    private void addOrder(JsonObject jsonData, HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        // ... 方法实现
    }

    /**
     * 更新订单状态
     * @param jsonData 订单数据JSON对象
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @throws SQLException SQL异常
     * @throws IOException IO异常
     */
    private void updateOrder(JsonObject jsonData, HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        // ... 方法实现
    }

    /**
     * 删除订单
     * @param jsonData 订单数据JSON对象
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @throws SQLException SQL异常
     * @throws IOException IO异常
     */
    private void deleteOrder(JsonObject jsonData, HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        // ... 方法实现
    }

    /**
     * 发送错误响应
     * @param response HTTP响应对象
     * @param message 错误信息
     * @throws IOException IO异常
     */
    private void sendError(HttpServletResponse response, String message) throws IOException {
        // ... 方法实现
    }
} 