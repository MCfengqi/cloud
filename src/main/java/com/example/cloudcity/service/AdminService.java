/**
 * 管理员服务类
 * 用途：处理管理员相关的业务逻辑，包括：
 * 1. 管理员信息的增删改查
 * 2. 管理员权限的验证和管理
 * 3. 管理员状态的维护
 * 4. 管理员操作的日志记录
 */
package com.example.cloudcity.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.example.cloudcity.utils.DatabaseConfig;

public class AdminService {
    /** 单例实例 */
    private static AdminService instance;
    
    /**
     * 私有构造函数，防止直接实例化
     */
    private AdminService() {}
    
    /**
     * 获取AdminService实例
     * @return AdminService单例实例
     */
    public static AdminService getInstance() {
        if (instance == null) {
            instance = new AdminService();
        }
        return instance;
    }
    
    /**
     * 更新管理员信息
     * @param id 管理员ID
     * @param username 用户名
     * @param email 邮箱
     * @param mobile 手机号
     * @return 更新是否成功
     */
    public boolean updateAdmin(Long id, String username, String email, String mobile) {
        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.DB_URL, 
                DatabaseConfig.USER, 
                DatabaseConfig.PASS)) {
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
    
    /**
     * 删除管理员
     * @param id 管理员ID
     * @return 删除是否成功
     */
    public boolean deleteAdmin(Long id) {
        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.DB_URL, 
                DatabaseConfig.USER, 
                DatabaseConfig.PASS)) {
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
    
    /**
     * 获取管理员信息
     * @param id 管理员ID
     * @return Admin对象，如果未找到返回null
     */
    public Admin getAdmin(Long id) {
        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.DB_URL, 
                DatabaseConfig.USER, 
                DatabaseConfig.PASS)) {
            String sql = "SELECT id, username, email, mobile, updated_at " +
                        "FROM users WHERE id = ? AND is_admin = 1";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    return new Admin(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("mobile"),
                        false, // 默认离线状态
                        rs.getTimestamp("updated_at")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 获取所有管理员列表
     * @return 管理员列表，如果出错返回空列表
     */
    public List<Admin> getAllAdmins() {
        List<Admin> adminList = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.DB_URL, 
                DatabaseConfig.USER, 
                DatabaseConfig.PASS)) {
            String sql = "SELECT id, username, email, mobile, " +
                        "updated_at, " +
                        "CASE WHEN updated_at > DATE_SUB(NOW(), INTERVAL 30 MINUTE) " +
                        "THEN true ELSE false END as is_online " +
                        "FROM users WHERE is_admin = 1";
            
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return adminList;
    }
}
