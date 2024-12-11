
/**
 * 管理员列表Servlet
 * 用途：处理管理员相关的请求，包括：
 * 1. 获取管理员列表
 * 2. 显示管理员状态
 * 3. 显示管理员最后登录时间
 * 4. 提供管理员的编辑和删除功能
 */
package com.example.cloudcity.servlet;

import com.example.cloudcity.service.Admin;
import com.example.cloudcity.service.AdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet; // 导入HttpServlet类
import jakarta.servlet.http.HttpServletRequest; // 导入HttpServletRequest类
import jakarta.servlet.http.HttpServletResponse; // 导入HttpServletResponse类
import com.google.gson.Gson; // 导入Gson类用于JSON处理
import com.google.gson.JsonObject; // 导入JsonObject类用于处理JSON对象

import java.io.BufferedReader; // 导入BufferedReader类用于读取请求体
import java.io.IOException; // 导入IOException类用于处理IO异常
import java.util.List; // 导入List接口用于存储列表数据
import java.sql.Connection; // 导入Connection类用于数据库连接
import java.sql.DriverManager; // 导入DriverManager类用于获取数据库连接
import java.sql.PreparedStatement; // 导入PreparedStatement类用于预编译SQL语句
import java.sql.ResultSet; // 导入ResultSet类用于存储查询结果
import java.sql.SQLException; // 导入SQLException类用于处理SQL异常
import java.sql.Timestamp; // 导入Timestamp类用于处理时间戳

public class AdminListServlet extends HttpServlet { // 定义AdminListServlet类继承自HttpServlet
    private static final AdminService adminService = AdminService.getInstance(); // 获取AdminService实例
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity"; // 数据库URL
    private static final String USER = "cloudcity"; // 数据库用户名
    private static final String PASS = "cloudcity"; // 数据库密码

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8"); // 设置响应内容类型为JSON

        try {
            String action = request.getParameter("action"); // 获取请求中的action参数
            if ("get".equals(action)) {
                // 获取单个管理员信息
                Long id = Long.parseLong(request.getParameter("id")); // 获取请求中的管理员ID
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) { // 获取数据库连接
                    String sql = "SELECT * FROM users WHERE id = ? AND is_admin = 1"; // 准备SQL查询语句

                    try (PreparedStatement stmt = conn.prepareStatement(sql)) { // 准备预编译SQL语句
                        stmt.setLong(1, id); // 设置SQL查询参数
                        ResultSet rs = stmt.executeQuery(); // 执行查询并获取结果集

                        if (rs.next()) { // 检查结果集中是否有数据
                            JsonObject admin = new JsonObject(); // 创建JsonObject存储管理员信息
                            admin.addProperty("id", rs.getLong("id")); // 存储管理员ID
                            admin.addProperty("username", rs.getString("username")); // 存储用户名
                            admin.addProperty("password", rs.getString("password")); // 存储密码
                            admin.addProperty("email", rs.getString("email")); // 存储邮箱
                            admin.addProperty("mobile", rs.getString("mobile")); // 存储手机号
                            admin.addProperty("isAdmin", rs.getBoolean("is_admin")); // 存储是否为管理员
                            admin.addProperty("created_at",
                                    rs.getTimestamp("created_at") != null ?
                                            rs.getTimestamp("created_at").toString() : null); // 存储创建时间
                            admin.addProperty("updated_at",
                                    rs.getTimestamp("updated_at") != null ?
                                            rs.getTimestamp("updated_at").toString() : null); // 存储更新时间

                            response.getWriter().write(admin.toString()); // 将管理员信息转换为JSON并写入响应
                        } else {
                            JsonObject error = new JsonObject(); // 创建JsonObject存储错误信息
                            error.addProperty("error", "管理员不存在"); // 设置错误消息
                            response.getWriter().write(error.toString()); // 将错误信息转换为JSON并写入响应
                        }
                    }
                }
            } else {
                // 获取管理员列表
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) { // 获取数据库连接
                    String sql = "SELECT * FROM users WHERE is_admin = 1"; // 准备SQL查询语句
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) { // 准备预编译SQL语句
                        ResultSet rs = stmt.executeQuery(); // 执行查询并获取结果集

                        StringBuilder jsonBuilder = new StringBuilder(); // 创建StringBuilder存储JSON数据
                        jsonBuilder.append("["); // 开始构建JSON数组
                        boolean first = true; // 标记是否为第一个元素

                        while (rs.next()) { // 遍历结果集中的每一行
                            if (!first) {
                                jsonBuilder.append(","); // 如果不是第一个元素，添加逗号分隔符
                            }
                            first = false; // 设置标记为false表示已经处理过一个元素

                            jsonBuilder.append("{") // 开始构建JSON对象
                                    .append("\"id\":").append(rs.getLong("id")).append(",") // 存储管理员ID
                                    .append("\"username\":\"").append(escapeJson(rs.getString("username"))).append("\",") // 存储用户名
                                    .append("\"password\":\"").append(escapeJson(rs.getString("password"))).append("\",") // 存储密码
                                    .append("\"email\":\"").append(escapeJson(rs.getString("email"))).append("\",") // 存储邮箱
                                    .append("\"mobile\":\"").append(escapeJson(rs.getString("mobile"))).append("\",") // 存储手机号
                                    .append("\"created_at\":\"").append(rs.getTimestamp("created_at") != null ?
                                            rs.getTimestamp("created_at").toString() : "").append("\",") // 存储创建时间
                                    .append("\"updated_at\":\"").append(rs.getTimestamp("updated_at") != null ?
                                            rs.getTimestamp("updated_at").toString() : "").append("\",") // 存储更新时间
                                    .append("\"isAdmin\":true") // 存储是否为管理员
                                    .append("}"); // 结束构建JSON对象
                        }

                        jsonBuilder.append("]"); // 结束构建JSON数组
                        response.getWriter().write(jsonBuilder.toString()); // 将JSON数据写入响应
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("AdminListServlet错误: " + e.getMessage()); // 打印错误信息到控制台
            e.printStackTrace(); // 打印堆栈跟踪信息
            JsonObject error = new JsonObject(); // 创建JsonObject存储错误信息
            error.addProperty("error", e.getMessage()); // 设置错误消息
            response.getWriter().write(error.toString()); // 将错误信息转换为JSON并写入响应
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8"); // 设置响应内容类型为JSON

        try {
            BufferedReader reader = request.getReader(); // 获取请求体的BufferedReader
            StringBuilder sb = new StringBuilder(); // 创建StringBuilder存储请求体内容
            String line;
            while ((line = reader.readLine()) != null) { // 逐行读取请求体内容
                sb.append(line); // 将每一行内容追加到StringBuilder中
            }

            Gson gson = new Gson(); // 创建Gson实例
            JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class); // 使用Gson解析JSON字符串为JsonObject
            String action = jsonObject.get("action").getAsString(); // 获取action参数

            JsonObject result = new JsonObject(); // 创建JsonObject存储响应数据

            switch (action) {
                case "add":
                    // 处理添加管理员
                    String username = jsonObject.get("username").getAsString(); // 获取用户名
                    String password = jsonObject.get("password").getAsString(); // 获取密码
                    String email = jsonObject.get("email").getAsString(); // 获取邮箱
                    String mobile = jsonObject.get("mobile").getAsString(); // 获取手机号
                    // TODO: 实现添加管理员的逻辑
                    result.addProperty("success", true); // 设置成功标志
                    break;

                case "update":
                    Long id = jsonObject.get("id").getAsLong(); // 获取管理员ID
                    username = jsonObject.get("username").getAsString(); // 获取用户名
                    email = jsonObject.get("email").getAsString(); // 获取邮箱
                    mobile = jsonObject.get("mobile").getAsString(); // 获取手机号
                    boolean success = adminService.updateAdmin(id, username, email, mobile); // 调用服务更新管理员信息
                    result.addProperty("success", success); // 设置成功标志
                    break;

                case "delete":
                    id = jsonObject.get("id").getAsLong(); // 获取管理员ID
                    success = adminService.deleteAdmin(id); // 调用服务删除管理员
                    result.addProperty("success", success); // 设置成功标志
                    break;

                default:
                    result.addProperty("success", false); // 设置失败标志
                    result.addProperty("error", "Unknown action"); // 设置错误消息
            }

            response.getWriter().write(result.toString()); // 将响应数据转换为JSON并写入响应

        } catch (Exception e) {
            e.printStackTrace(); // 打印堆栈跟踪信息
            JsonObject result = new JsonObject(); // 创建JsonObject存储错误响应数据
            result.addProperty("success", false); // 设置失败标志
            result.addProperty("error", e.getMessage()); // 设置错误消息
            response.getWriter().write(result.toString()); // 将错误响应数据转换为JSON并写入响应
        }
    }

    // 添加 escapeJson 方法
    private String escapeJson(String input) {
        if (input == null) {
            return ""; // 如果输入为空，返回空字符串
        }
        return input.replace("\\", "\\\\") // 转义反斜杠
                .replace("\"", "\\\"") // 转义双引号
                .replace("\b", "\\b") // 转义退格符
                .replace("\f", "\\f") // 转义换页符
                .replace("\n", "\\n") // 转义换行符
                .replace("\r", "\\r") // 转义回车符
                .replace("\t", "\\t"); // 转义制表符
    }
}