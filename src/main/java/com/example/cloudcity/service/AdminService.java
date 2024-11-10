package com.example.cloudcity.service;

import java.util.Arrays;
import java.util.List;

public class AdminService {
    private static AdminService instance;

    private AdminService() {}

    public static AdminService getInstance() {
        if (instance == null) {
            synchronized (AdminService.class) {
                if (instance == null) {
                    instance = new AdminService();
                }
            }
        }
        return instance;
    }

    public List<Admin> getOnlineAdmins() {
        // 假设使用 JDBC 进行数据库操作
        // 连接数据库
        // 执行查询操作
        // 关闭连接
        // 返回在线管理员列表
        // 示例 SQL 语句
        // String sql = "SELECT username FROM admins WHERE status = 'online'";
        // PreparedStatement stmt = connection.prepareStatement(sql);
        // ResultSet rs = stmt.executeQuery();
        // List<Admin> adminList = new ArrayList<>();
        // while (rs.next()) {
        //     Admin admin = new Admin(rs.getString("username"));
        //     adminList.add(admin);
        // }
        // return adminList;

        // 临时数据
        return Arrays.asList(new Admin("admin1"), new Admin("admin2"));
    }
}
