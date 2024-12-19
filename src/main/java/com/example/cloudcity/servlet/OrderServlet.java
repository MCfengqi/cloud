/**
 * 订单处理Servlet
 * 用途：处理订单相关的所有后端请求，包括：
 * 1. 订单的创建和提交
 * 2. 订单支付处理
 * 3. 订单状态更新
 * 4. 订单日志记录
 * 5. 订单错误处理
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

public class OrderServlet extends HttpServlet {
    /** JSON处理工具 */
    private final Gson gson = new Gson();

    /**
     * 处理POST请求 - 创建订单
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        try {
            // 读取请求数据
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            
            // 解析JSON数据
            JsonObject jsonData = gson.fromJson(sb.toString(), JsonObject.class);
            
            // 创建订单
            try (Connection conn = DriverManager.getConnection(
                    DatabaseConfig.DB_URL, 
                    DatabaseConfig.USER, 
                    DatabaseConfig.PASS)) {
                
                // 准备SQL语句
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO game_order (total, amount, status, paytype, gamename, gameimg, gametxt, user_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                    
                    // 设置参数
                    stmt.setBigDecimal(1, new java.math.BigDecimal(jsonData.get("total").getAsString()));
                    stmt.setBigDecimal(2, new java.math.BigDecimal(jsonData.get("amount").getAsString()));
                    stmt.setString(3, jsonData.get("status").getAsString());
                    stmt.setString(4, jsonData.get("paytype").getAsString());
                    stmt.setString(5, jsonData.get("gamename").getAsString());
                    stmt.setString(6, jsonData.get("gameimg").getAsString());
                    stmt.setString(7, jsonData.get("gametxt").getAsString());
                    stmt.setLong(8, jsonData.get("user_id").getAsLong());
                    
                    // 执行SQL
                    int result = stmt.executeUpdate();
                    
                    // 处理结果
                    if (result > 0) {
                        // 记录操作日志
                        LogUtils.logOperation(
                            "创建订单",
                            "创建新订单: " + jsonData.get("gamename").getAsString(),
                            (String) request.getSession().getAttribute("username"),
                            request,
                            "成功"
                        );
                        
                        // 返回成功响应
                        JsonObject response_data = new JsonObject();
                        response_data.addProperty("success", true);
                        response_data.addProperty("message", "订单创建成功");
                        response.getWriter().write(gson.toJson(response_data));
                    } else {
                        // 返回失败响应
                        JsonObject error_response = new JsonObject();
                        error_response.addProperty("success", false);
                        error_response.addProperty("error", "订单创建失败");
                        response.getWriter().write(gson.toJson(error_response));
                    }
                }
            }
        } catch (Exception e) {
            // 记录错误日志
            System.err.println("Error creating order: " + e.getMessage());
            e.printStackTrace();
            
            // 返回错误响应
            JsonObject error_response = new JsonObject();
            error_response.addProperty("success", false);
            error_response.addProperty("error", e.getMessage());
            response.getWriter().write(gson.toJson(error_response));
        }
    }
} 