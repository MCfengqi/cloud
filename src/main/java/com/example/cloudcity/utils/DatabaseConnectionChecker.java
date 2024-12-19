
/**
 * 数据库连接检查工具类
 * 用途：检查数据库连接是否正常，包括：
 * - 测试数据库连接是否可用
 * - 验证数据源配置是否正确
 * - 提供数据库连接状态检查方法
 */
package com.example.cloudcity.utils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionChecker {

    public static boolean checkDatabaseConnection() {
        try {
            // 获取数据源
            DataSource dataSource = DataSourceUtils.getDataSource();
            // 获取数据库连接
            Connection connection = dataSource.getConnection();
            // 检查连接是否有效
            if (connection != null && !connection.isClosed()) {
                System.out.println("数据库连接成功");
                // 关闭连接
                connection.close();
                return true;
            } else {
                System.out.println("数据库连接失败");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("数据库连接失败: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        // 测试数据库连接
        boolean isConnected = checkDatabaseConnection();
        if (isConnected) {
            System.out.println("数据库连接成功");
        } else {
            System.out.println("数据库连接失败");
        }
    }
}
