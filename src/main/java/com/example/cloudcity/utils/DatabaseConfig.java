/**
 * 数据库配置类
 * 用途：集中管理数据库连接信息，包括：
 * 1. 数据库连接URL
 * 2. 数据库用户名
 * 3. 数据库密码
 * 4. 数据库驱动类名
 */
package com.example.cloudcity.utils;

public class DatabaseConfig {
    /** 数据库连接URL */
    public static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity";
    /** 数据库用户名 */
    public static final String USER = "root";
    /** 数据库密码 */
    public static final String PASS = "123456";
    /** 数据库驱动类名 */
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * 私有构造函数，防止实例化
     */
    private DatabaseConfig() {
        // 私有构造函数
    }
} 