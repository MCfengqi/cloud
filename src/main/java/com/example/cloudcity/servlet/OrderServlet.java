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

public class OrderServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity";
    private static final String USER = "root";
    private static final String PASS = "123456";
    private final Gson gson = new Gson();

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
            
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO game_order (total, amount, status, paytype, gamename, gameimg, gametxt, user_id) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                
                stmt.setBigDecimal(1, new java.math.BigDecimal(jsonData.get("total").getAsString()));
                stmt.setBigDecimal(2, new java.math.BigDecimal(jsonData.get("amount").getAsString()));
                stmt.setString(3, jsonData.get("status").getAsString());
                stmt.setString(4, jsonData.get("paytype").getAsString());
                stmt.setString(5, jsonData.get("gamename").getAsString());
                stmt.setString(6, jsonData.get("gameimg").getAsString());
                stmt.setString(7, jsonData.get("gametxt").getAsString());
                stmt.setLong(8, jsonData.get("user_id").getAsLong());
                
                int result = stmt.executeUpdate();
                
                if (result > 0) {
                    LogUtils.logOperation(
                        "创建订单",
                        "创建新订单: " + jsonData.get("gamename").getAsString(),
                        (String) request.getSession().getAttribute("username"),
                        request,
                        "成功"
                    );
                    
                    JsonObject response_data = new JsonObject();
                    response_data.addProperty("success", true);
                    response_data.addProperty("message", "订单创建成功");
                    response.getWriter().write(gson.toJson(response_data));
                } else {
                    JsonObject error_response = new JsonObject();
                    error_response.addProperty("success", false);
                    error_response.addProperty("error", "订单创建失败");
                    response.getWriter().write(gson.toJson(error_response));
                }
            }
        } catch (Exception e) {
            System.err.println("Error creating order: " + e.getMessage());
            e.printStackTrace();
            JsonObject error_response = new JsonObject();
            error_response.addProperty("success", false);
            error_response.addProperty("error", e.getMessage());
            response.getWriter().write(gson.toJson(error_response));
        }
    }
} 