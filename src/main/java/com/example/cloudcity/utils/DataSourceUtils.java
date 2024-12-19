/**
 * 数据源工具类
 * 用途：管理数据库连接池，包括：
 * - 初始化C3P0连接池
 * - 提供数据源获取方法
 * - 提供数据库连接获取方法
 * - 管理连接池参数(初始连接数、最小连接数、最大连接数)
 */
package com.example.cloudcity.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceUtils {
    private static final ComboPooledDataSource cpds = new ComboPooledDataSource();

    static {
        try {
            cpds.setDriverClass("com.mysql.cj.jdbc.Driver");
            cpds.setJdbcUrl("jdbc:mysql://localhost:3306/cloudcity");
            cpds.setUser("root");
            cpds.setPassword("123456");
            cpds.setInitialPoolSize(3);
            cpds.setMinPoolSize(3);
            cpds.setMaxPoolSize(15);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ComboPooledDataSource getDataSource() {
        return cpds;
    }

    public static Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }
}
