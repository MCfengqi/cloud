package com.example.cloudcity.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogUtils {
    
    public static void logOperation(String operationType, String operationContent, 
                                  String username, HttpServletRequest request, String result) {
        System.out.println("Logging operation:");
        System.out.println("Type: " + operationType);
        System.out.println("Content: " + operationContent);
        System.out.println("Username: " + username);
        System.out.println("IP: " + getClientIpAddress(request));
        System.out.println("Result: " + result);

        String sql = "INSERT INTO operation_logs (operation_type, operation_content, username, ip_address, result) " +
                    "VALUES (?, ?, ?, ?, ?)";
                    
        try (Connection conn = DataSourceUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, operationType);
            stmt.setString(2, operationContent);
            stmt.setString(3, username);
            stmt.setString(4, getClientIpAddress(request));
            stmt.setString(5, result);
            
            int rows = stmt.executeUpdate();
            System.out.println("Inserted " + rows + " row(s)");
        } catch (SQLException e) {
            System.err.println("Error logging operation: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
} 