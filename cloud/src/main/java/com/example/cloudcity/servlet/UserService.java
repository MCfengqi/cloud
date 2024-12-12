package com.example.cloudcity.servlet;

import com.example.cloudcity.dao.UserDao;
import com.example.cloudcity.model.User;
import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final UserDao userDao = new UserDao(); // 使用单例模式

    public static UserService getInstance() {
        return new UserService();
    }

    public User selectByEmailPassword(String email, String password) throws Exception {
        return userDao.selectByEmailPassword(email, password);
    }

    public User login(String username, String password) {
        // 数据验证
        if (!isValidInput(username, password)) {
            logger.warn("Invalid input: username or password is empty");
            return null;
        }

        try {
            User user = userDao.login(username, password);
            if (user == null) {
                logger.warn("User not found: {}", username);
                return null;
            }
            return user;
        } catch (Exception e) {
            logger.error("Failed to login user: {}", e.getMessage(), e);
            return null;
        }
    }

    private boolean isValidInput(String username, String password) {
        return username != null && !username.isEmpty() && password != null && !password.isEmpty();
    }

    public void register(User user) throws ServletException {
        // 输入验证
        if (!isValidInput(user)) {
            logger.warn("Invalid input: one or more fields are empty or invalid");
            throw new ServletException("注册失败: 输入数据无效");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/CloudCity");
            conn = ds.getConnection();

            String sql = "INSERT INTO users (username, password, email, mobile, is_admin) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getMobile());
            pstmt.setString(5, user.getIsadmin());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("User registered successfully: {}", user.getUsername());
            } else {
                logger.warn("Failed to register user: {}", user.getUsername());
            }
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage(), e);
            throw new ServletException("注册失败", e);
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                logger.error("Error closing resources: {}", e.getMessage(), e);
            }
        }
    }

    private boolean isValidInput(User user) {
        return user != null &&
               user.getUsername() != null && !user.getUsername().isEmpty() &&
               user.getPassword() != null && !user.getPassword().isEmpty() &&
               user.getEmail() != null && !user.getEmail().isEmpty() &&
               user.getMobile() != null && !user.getMobile().isEmpty() &&
               user.getIsadmin() != null;
    }
}
