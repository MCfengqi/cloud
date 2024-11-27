/**
 * 管理员列表Servlet
 * 用途：处理管理员相关的请求，包括：
 * 1. 获取管理员列表
 * 2. 显示管理员状态
 * 3. 显示管理员最后登录时间
 * 4. 提供管理员的编辑和删除功能
 */
package com.example.cloudcity.servlet;

import com.example.cloudcity.service.Admin;
import com.example.cloudcity.service.AdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AdminListServlet extends HttpServlet {
    private static final AdminService adminService = AdminService.getInstance();
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity";
    private static final String USER = "cloudcity";
    private static final String PASS = "cloudcity";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        try {
            String action = request.getParameter("action");
            if ("get".equals(action)) {
                // 获取单个管理员信息
                Long id = Long.parseLong(request.getParameter("id"));
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                    String sql = "SELECT * FROM users WHERE id = ? AND is_admin = 1";
                               
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setLong(1, id);
                        ResultSet rs = stmt.executeQuery();
                        
                        if (rs.next()) {
                            JsonObject admin = new JsonObject();
                            admin.addProperty("id", rs.getLong("id"));
                            admin.addProperty("username", rs.getString("username"));
                            admin.addProperty("password", rs.getString("password"));
                            admin.addProperty("email", rs.getString("email"));
                            admin.addProperty("mobile", rs.getString("mobile"));
                            admin.addProperty("isAdmin", rs.getBoolean("is_admin"));
                            admin.addProperty("created_at", 
                                rs.getTimestamp("created_at") != null ? 
                                rs.getTimestamp("created_at").toString() : null);
                            admin.addProperty("updated_at", 
                                rs.getTimestamp("updated_at") != null ? 
                                rs.getTimestamp("updated_at").toString() : null);
                            
                            response.getWriter().write(admin.toString());
                        } else {
                            JsonObject error = new JsonObject();
                            error.addProperty("error", "管理员不存在");
                            response.getWriter().write(error.toString());
                        }
                    }
                }
            } else {
                // 获取管理员列表
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                    String sql = "SELECT * FROM users WHERE is_admin = 1";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        ResultSet rs = stmt.executeQuery();
                        
                        StringBuilder jsonBuilder = new StringBuilder();
                        jsonBuilder.append("[");
                        boolean first = true;
                        
                        while (rs.next()) {
                            if (!first) {
                                jsonBuilder.append(",");
                            }
                            first = false;
                            
                            jsonBuilder.append("{")
                                .append("\"id\":").append(rs.getLong("id")).append(",")
                                .append("\"username\":\"").append(escapeJson(rs.getString("username"))).append("\",")
                                .append("\"password\":\"").append(escapeJson(rs.getString("password"))).append("\",")
                                .append("\"email\":\"").append(escapeJson(rs.getString("email"))).append("\",")
                                .append("\"mobile\":\"").append(escapeJson(rs.getString("mobile"))).append("\",")
                                .append("\"created_at\":\"").append(rs.getTimestamp("created_at") != null ? 
                                    rs.getTimestamp("created_at").toString() : "").append("\",")
                                .append("\"updated_at\":\"").append(rs.getTimestamp("updated_at") != null ? 
                                    rs.getTimestamp("updated_at").toString() : "").append("\",")
                                .append("\"isAdmin\":true")
                                .append("}");
                        }
                        
                        jsonBuilder.append("]");
                        response.getWriter().write(jsonBuilder.toString());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("AdminListServlet错误: " + e.getMessage());
            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("error", e.getMessage());
            response.getWriter().write(error.toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
            String action = jsonObject.get("action").getAsString();
            
            JsonObject result = new JsonObject();
            
            switch (action) {
                case "add":
                    // 处理添加管理员
                    String username = jsonObject.get("username").getAsString();
                    String password = jsonObject.get("password").getAsString();
                    String email = jsonObject.get("email").getAsString();
                    String mobile = jsonObject.get("mobile").getAsString();
                    // TODO: 实现添加管理员的逻辑
                    result.addProperty("success", true);
                    break;
                    
                case "update":
                    Long id = jsonObject.get("id").getAsLong();
                    username = jsonObject.get("username").getAsString();
                    email = jsonObject.get("email").getAsString();
                    mobile = jsonObject.get("mobile").getAsString();
                    boolean success = adminService.updateAdmin(id, username, email, mobile);
                    result.addProperty("success", success);
                    break;
                    
                case "delete":
                    id = jsonObject.get("id").getAsLong();
                    success = adminService.deleteAdmin(id);
                    result.addProperty("success", success);
                    break;
                    
                default:
                    result.addProperty("success", false);
                    result.addProperty("error", "Unknown action");
            }
            
            response.getWriter().write(result.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject result = new JsonObject();
            result.addProperty("success", false);
            result.addProperty("error", e.getMessage());
            response.getWriter().write(result.toString());
        }
    }

    // 添加 escapeJson 方法
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
