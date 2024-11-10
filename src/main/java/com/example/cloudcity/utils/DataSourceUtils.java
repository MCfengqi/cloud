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
            cpds.setUser("cloudcity");
            cpds.setPassword("cloudcity");
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
