/**
 * 管理员服务类
 * 用途：提供管理员相关的业务逻辑处理，包括：
 * 1. 获取在线管理员列表
 * 2. 管理员信息的更新
 * 3. 管理员的删除
 * 4. 使用单例模式确保全局唯一实例
 */
package com.example.cloudcity.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity";
    private static final String USER = "root";
    private static final String PASS = "123456";
    
    private static AdminService instance;
    
    private AdminService() {}
    
    public static AdminService getInstance() {
        if (instance == null) {
            instance = new AdminService();
        }
        return instance;
    }
    
    public List<Admin> getOnlineAdmins() {
        List<Admin> adminList = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("AdminService: 数据库连接成功");
            String sql = "SELECT id, username, email, mobile, " +
                        "updated_at, " +
                        "CASE WHEN updated_at > DATE_SUB(NOW(), INTERVAL 30 MINUTE) " +
                        "THEN true ELSE false END as is_online " +
                        "FROM users WHERE is_admin = 1";
            System.out.println("AdminService: 执行SQL: " + sql);
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    Admin admin = new Admin(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("mobile"),
                        rs.getBoolean("is_online"),
                        rs.getTimestamp("updated_at")
                    );
                    adminList.add(admin);
                    System.out.println("AdminService: 找到管理员 - ID: " + admin.getId() + 
                                     ", 用户名: " + admin.getUsername() +
                                     ", 邮箱: " + admin.getEmail() +
                                     ", 手机: " + admin.getMobile() +
                                     ", 在线状态: " + admin.isOnline());
                }
            }
        } catch (SQLException e) {
            System.err.println("AdminService错误: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("AdminService: 总共找到 " + adminList.size() + " 个管理员");
        return adminList;
    }
    
    public boolean updateAdmin(Long id, String username, String email, String mobile) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "UPDATE users SET username = ?, email = ?, mobile = ?, updated_at = NOW() " +
                        "WHERE id = ? AND is_admin = 1";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, mobile);
                stmt.setLong(4, id);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteAdmin(Long id) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "DELETE FROM users WHERE id = ? AND is_admin = 1";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, id);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
