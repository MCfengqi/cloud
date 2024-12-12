package com.example.cloudcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet; // 导入HttpServlet类
import jakarta.servlet.http.HttpServletRequest; // 导入HttpServletRequest类
import jakarta.servlet.http.HttpServletResponse; // 导入HttpServletResponse类
import java.io.BufferedReader; // 导入BufferedReader类用于读取请求体
import java.io.IOException; // 导入IOException类用于处理IO异常
import java.sql.*; // 导入SQL相关类
import java.util.*; // 导入Java集合框架
import com.google.gson.Gson; // 导入Gson类用于JSON处理
import com.google.gson.JsonObject; // 导入JsonObject类用于处理JSON对象
import com.example.cloudcity.utils.LogUtils; // 导入LogUtils类用于日志记录

@WebServlet("/AdminManageServlet")
public class AdminManageServlet extends HttpServlet { // 定义AdminManageServlet类继承自HttpServlet
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity"; // 数据库URL
    private static final String USER = "cloudcity"; // 数据库用户名
    private static final String PASS = "cloudcity"; // 数据库密码

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8"); // 设置响应内容类型为JSON
        String action = request.getParameter("action"); // 获取请求中的action参数

        // 检查是否是管理员
        Boolean isAdmin = (Boolean) request.getSession().getAttribute("isAdmin"); // 从会话中获取isAdmin属性
        if (isAdmin == null || !isAdmin) {
            response.getWriter().write("{\"error\": \"Unauthorized access\"}"); // 如果不是管理员，返回错误信息
            return;
        }

        try {
            switch (action) {
                case "list":
                    listAdmins(request, response); // 处理获取管理员列表请求
                    break;
                case "get":
                    getAdmin(request, response); // 处理获取单个管理员信息请求
                    break;
                default:
                    response.getWriter().write("{\"error\": \"Unknown action\"}"); // 处理未知操作请求
            }
        } catch (Exception e) {
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}"); // 捕获并返回异常信息
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8"); // 设置响应内容类型为JSON

        // 检查是否是管理员
        Boolean isAdmin = (Boolean) request.getSession().getAttribute("isAdmin"); // 从会话中获取isAdmin属性
        if (isAdmin == null || !isAdmin) {
            response.getWriter().write("{\"error\": \"Unauthorized access\"}"); // 如果不是管理员，返回错误信息
            return;
        }

        try {
            // 如果是 JSON 请求
            if (request.getContentType() != null && request.getContentType().contains("application/json")) {
                // 读取 JSON 数据
                StringBuilder buffer = new StringBuilder();
                String line;
                try (BufferedReader reader = request.getReader()) { // 获取请求体的BufferedReader
                    while ((line = reader.readLine()) != null) { // 逐行读取请求体内容
                        buffer.append(line); // 将每一行内容追加到StringBuilder中
                    }
                }

                // 解析 JSON
                Gson gson = new Gson(); // 创建Gson实例
                JsonObject jsonObject = gson.fromJson(buffer.toString(), JsonObject.class); // 使用Gson解析JSON字符串为JsonObject
                String action = jsonObject.get("action").getAsString(); // 获取action参数

                switch (action) {
                    case "update":
                        updateAdmin(jsonObject, request, response); // 处理更新管理员信息请求
                        break;
                    case "add":
                        addAdmin(request, response); // 处理添加管理员请求
                        break;
                    case "delete":
                        deleteAdmin(request, response); // 处理删除管理员请求
                        break;
                    default:
                        response.getWriter().write("{\"error\": \"Unknown action\"}"); // 处理未知操作请求
                }
            } else {
                // 处理普通表单提交
                String action = request.getParameter("action"); // 获取请求中的action参数
                switch (action) {
                    case "add":
                        addAdmin(request, response); // 处理添加管理员请求
                        break;
                    case "delete":
                        deleteAdmin(request, response); // 处理删除管理员请求
                        break;
                    default:
                        response.getWriter().write("{\"error\": \"Unknown action\"}"); // 处理未知操作请求
                }
            }
        } catch (Exception e) {
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}"); // 捕获并返回异常信息
        }
    }

    private void listAdmins(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String searchTerm = request.getParameter("search"); // 获取搜索参数
            StringBuilder sql = new StringBuilder("SELECT * FROM users WHERE is_admin = 1"); // 准备SQL查询语句

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                sql.append(" AND username LIKE ?"); // 如果有搜索参数，添加LIKE条件
            }

            Class.forName("com.mysql.cj.jdbc.Driver"); // 加载MySQL驱动
            conn = DriverManager.getConnection(DB_URL, USER, PASS); // 获取数据库连接
            stmt = conn.prepareStatement(sql.toString()); // 准备预编译SQL语句

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                stmt.setString(1, "%" + searchTerm.trim() + "%"); // 设置LIKE条件参数
            }

            rs = stmt.executeQuery(); // 执行查询并获取结果集

            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("["); // 开始构建JSON数组

            boolean first = true;
            while (rs.next()) { // 遍历结果集中的每一行
                if (!first) {
                    jsonBuilder.append(","); // 如果不是第一个元素，添加逗号分隔符
                }
                first = false; // 设置标记为false表示已经处理过一个元素

                // 获取管理员信息
                long id = rs.getLong("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String mobile = rs.getString("mobile");

                // 构建 JSON 对象
                jsonBuilder.append("{")
                    .append("\"id\":").append(id).append(",")
                    .append("\"username\":\"").append(escapeJson(username)).append("\",")
                    .append("\"password\":\"").append(escapeJson(password)).append("\",")
                    .append("\"email\":\"").append(escapeJson(email)).append("\",")
                    .append("\"mobile\":\"").append(escapeJson(mobile)).append("\",")
                    .append("\"isAdmin\":true,")
                    .append("\"userType\":\"超级管理员\"")
                    .append("}");

                System.out.println("Found admin: " + username + ", ID: " + id); // 打印找到的管理员信息
            }

            jsonBuilder.append("]"); // 结束构建JSON数组

            String jsonResponse = jsonBuilder.toString();
            System.out.println("JSON Response: " + jsonResponse); // 打印JSON响应

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jsonResponse); // 将JSON数据写入响应

        } catch (Exception e) {
            System.err.println("Error in listAdmins: " + e.getMessage()); // 打印错误信息到控制台
            e.printStackTrace(); // 打印堆栈跟踪信息
            response.getWriter().write("[{\"error\":\"" + escapeJson(e.getMessage()) + "\"}]"); // 将错误信息转换为JSON并写入响应
        } finally {
            if (rs != null) rs.close(); // 关闭ResultSet
            if (stmt != null) stmt.close(); // 关闭PreparedStatement
            if (conn != null) conn.close(); // 关闭Connection
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

    private void addAdmin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String username = request.getParameter("username"); // 获取用户名
        String password = request.getParameter("password"); // 获取密码
        String email = request.getParameter("email"); // 获取邮箱
        String mobile = request.getParameter("mobile"); // 获取手机号

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); // 获数据库连接
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (username, password, email, mobile, is_admin) VALUES (?, ?, ?, ?, 1)")) { // 准备预编译SQL语句

            stmt.setString(1, username); // 设置用户名参数
            stmt.setString(2, password); // 设置密码参数
            stmt.setString(3, email); // 设置邮箱参数
            stmt.setString(4, mobile); // 设置手机号参数

            int result = stmt.executeUpdate(); // 执行插入操作
            response.getWriter().write("{\"success\": " + (result > 0) + "}"); // 返回插入结果

            // 添加日志记录
            LogUtils.logOperation(
                "添加管理员",
                "添加管理员: " + username,
                (String) request.getSession().getAttribute("username"),
                request,
                "成功"
            );
        }
    }

    private void updateAdmin(JsonObject data, HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        long id = data.get("id").getAsLong(); // 获取管理员ID
        String username = data.get("username").getAsString(); // 获取用户名
        String email = data.get("email").getAsString(); // 获取邮箱
        String mobile = data.get("mobile").getAsString(); // 获取手机号
        boolean isAdmin = data.get("isAdmin").getAsBoolean(); // 获取是否为管理员
        String password = data.has("password") ? data.get("password").getAsString() : null; // 获取密码，如果存在的话

        System.out.println("Updating admin - ID: " + id + ", isAdmin: " + isAdmin); // 打印更新信息

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS); // 获取数据库连接

            // 首先检查是否是最后一个管理员
            if (!isAdmin) {
                String countSql = "SELECT COUNT(*) FROM users WHERE is_admin = 1 AND id != ?"; // 准备SQL查询语句
                try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
                    countStmt.setLong(1, id); // 设置参数
                    ResultSet rs = countStmt.executeQuery(); // 执行查询并获取结果集
                    if (rs.next() && rs.getInt(1) == 0) {
                        response.getWriter().write("{\"success\": false, \"error\": \"不能将最后一个管理员改为普通用户！\"}"); // 返回错误信息
                        return;
                    }
                }
            }

            // 构建更新 SQL
            StringBuilder sql = new StringBuilder("UPDATE users SET email = ?, mobile = ?, is_admin = ?");
            if (password != null && !password.trim().isEmpty()) {
                sql.append(", password = ?"); // 如果有密码参数，添加到SQL语句中
            }
            sql.append(" WHERE id = ?");

            stmt = conn.prepareStatement(sql.toString()); // 准备预编译SQL语句

            // 设置参数
            int paramIndex = 1;
            stmt.setString(paramIndex++, email); // 设置邮箱参数
            stmt.setString(paramIndex++, mobile); // 设置手机号参数
            stmt.setInt(paramIndex++, isAdmin ? 1 : 0); // 设置是否为管理员参数

            if (password != null && !password.trim().isEmpty()) {
                stmt.setString(paramIndex++, password); // 设置密码参数
            }
            stmt.setLong(paramIndex, id); // 设置管理员ID参数

            // 执行更新
            int result = stmt.executeUpdate(); // 执行更新操作

            System.out.println("Update result: " + result); // 打印更新结果

            response.getWriter().write("{\"success\": " + (result > 0) + "}"); // 返回更新结果

            // 添加日志记录
            LogUtils.logOperation(
                "更新管理员",
                "更新管理员信息: " + username,
                (String) request.getSession().getAttribute("username"),
                request,
                "成功"
            );
        } catch (Exception e) {
            System.err.println("Error updating admin: " + e.getMessage()); // 打印错误信息到控制台
            e.printStackTrace(); // 打印堆栈跟踪信息
            response.getWriter().write("{\"success\": false, \"error\": \"" + escapeJson(e.getMessage()) + "\"}"); // 将错误信息转换为JSON并写入响应
        } finally {
            if (stmt != null) stmt.close(); // 关闭PreparedStatement
            if (conn != null) conn.close(); // 关闭Connection
        }
    }

    private void deleteAdmin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id")); // 获取管理员ID

        // 检查是否是最后一个管理员
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) { // 获取数据库连接
            // 首先检查管理员总数
            String countSql = "SELECT COUNT(*) FROM users WHERE is_admin = 1"; // 准备SQL查询语句
            try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
                ResultSet rs = countStmt.executeQuery(); // 执行查询并获取结果集
                if (rs.next() && rs.getInt(1) <= 1) {
                    response.getWriter().write("{\"success\": false, \"error\": \"不能删除最后一个管理员！\"}"); // 返回错误信息
                    return;
                }
            }

            // 如果不是最后一个管理员，则执行删除
            String deleteSql = "DELETE FROM users WHERE id = ? AND is_admin = 1"; // 准备SQL删除语句
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setLong(1, id); // 设置管理员ID参数
                int result = deleteStmt.executeUpdate(); // 执行删除操作
                response.getWriter().write("{\"success\": " + (result > 0) + "}"); // 返回删除结果
            }
        }

        // 添加日志记录
        LogUtils.logOperation(
            "删除管理员",
            "删除管理员ID: " + id,
            (String) request.getSession().getAttribute("username"),
            request,
            "成功"
        );
    }

    private void getAdmin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id")); // 获取管理员ID

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); // 获取数据库连接
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE id = ? AND is_admin = 1")) { // 准备预编译SQL查询语句

            stmt.setLong(1, id); // 设置管理员ID参数
            ResultSet rs = stmt.executeQuery(); // 执行查询并获取结果集

            if (rs.next()) {
                // 构建完整的用户信息
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append("{")
                    .append("\"id\":").append(rs.getLong("id")).append(",")
                    .append("\"username\":\"").append(escapeJson(rs.getString("username"))).append("\",")
                    .append("\"password\":\"").append(escapeJson(rs.getString("password"))).append("\",")
                    .append("\"email\":\"").append(escapeJson(rs.getString("email"))).append("\",")
                    .append("\"mobile\":\"").append(escapeJson(rs.getString("mobile"))).append("\",")
                    .append("\"isAdmin\":").append(rs.getInt("is_admin") == 1).append(",")
                    .append("\"userType\":\"").append(rs.getInt("is_admin") == 1 ? "超级管理员" : "普通用户").append("\"")
                    .append("}");

                System.out.println("Admin JSON response: " + jsonBuilder.toString()); // 打印JSON响应
                response.getWriter().write(jsonBuilder.toString()); // 将JSON数据写入响应
            } else {
                response.getWriter().write("{\"error\": \"Admin not fou nd\"}"); // 返回管理员未找到的错误信息
            }
        } catch (Exception e) {
            System.err.println("Error in getAdmin: " + e.getMessage()); // 打印错误信息到控制台
            e.printStackTrace(); // 打印堆栈跟踪信息
            response.getWriter().write("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}"); // 将错误信息转换为JSON并写入响应
        }
    }
}
