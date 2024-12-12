package com.example.cloudcity.dao;

import com.example.cloudcity.model.User;
import com.example.cloudcity.utils.DataSourceUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao {
    private static final QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private User selectByField(String field, String value, String password) throws SQLException {
        String sql = "SELECT * FROM gamesuser WHERE " + field + " = ? AND password = ?";
        logger.info("Executing query: {} with parameters: {}, {}", sql, value, password);
        return queryRunner.query(sql, new BeanHandler<>(User.class), value, password);
    }

    public User selectByUsername(String username, String password) throws SQLException {
        return selectByField("username", username, password);
    }

    public User selectByEmailPassword(String email, String password) throws SQLException {
        return selectByField("email", email, password);
    }

    public User login(String usernameOrEmail, String password) {
        User user = null;
        try {
            user = selectByUsername(usernameOrEmail, password);
        } catch (SQLException e) {
            logger.error("Failed to select user by username: {}", e.getMessage(), e);
            return null;
        }

        if (user != null) {
            logger.info("User found by username: {}", user);
            return user;
        }

        try {
            user = selectByEmailPassword(usernameOrEmail, password);
        } catch (SQLException e) {
            logger.error("Failed to select user by email: {}", e.getMessage(), e);
            return null;
        }

        if (user != null) {
            logger.info("User found by email: {}", user);
        }

        return user;
    }

    public void registerUser(User user) throws SQLException {
        // 数据验证
        if (user == null || user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("用户信息不完整");
        }

        try (Connection conn = DataSourceUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO users (username, password, email, mobile, isadmin) VALUES (?, ?, ?, ?, ?)")) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getMobile());
            pstmt.setString(5, user.getIsadmin());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected <= 0) {
                throw new SQLException("注册失败，数据库插入失败");
            }
        } catch (SQLException e) {
            logger.error("Failed to register user: {}", e.getMessage(), e);
            throw e;
        }
    }
}
