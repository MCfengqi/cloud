/**
 * 管理员管理Servlet
 * 用途：处理管理员相关的所有后端请求，包括：
 * 1. 管理员列表的获取和展示
 * 2. 管理员信息的添加和注册
 * 3. 管理员信息的修改和更新
 * 4. 管理员账号的删除
 * 5. 管理员权限的分配和管理
 */
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
import com.example.cloudcity.utils.DatabaseConfig;

/**
 * 管理员管理Servlet
 */
@WebServlet("/AdminManageServlet")
public class AdminManageServlet extends HttpServlet { // 定义AdminManageServlet类继承自HttpServlet

    /**
     * 处理GET请求的方法
     * 主要用于处理管理员列表的获取和展示
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应内容类型为JSON，使用UTF-8编码
        response.setContentType("application/json;charset=UTF-8");

        // 获取请求中的action参数
        String action = request.getParameter("action");

        // 权限检查：验证用户是否是管理员
        Boolean isAdmin = (Boolean) request.getSession().getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            // 如果不是管理员，返回未授权错误
            response.getWriter().write("{\"error\": \"Unauthorized access\"}");
            return;
        }

        try {
            // 根据不同的操作类型执行相应的处理
            switch (action) {
                case "list":
                    // 处理获取管理员列表的请求
                    listAdmins(request, response);
                    break;
                case "get":
                    // 处理获取单个管理员信息的请求
                    getAdmin(request, response);
                    break;
                default:
                    // 处理未知的操作类型
                    response.getWriter().write("{\"error\": \"Unknown action\"}");
            }
        } catch (Exception e) {
            // 发生异常时返回错误信息
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * 处理POST请求的方法
     * 主要用于处理管理员的添加、更新和删除操作
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应内容类型为JSON，使用UTF-8编码
        response.setContentType("application/json;charset=UTF-8");

        // 权限检查：验证用户是否是管理员
        Boolean isAdmin = (Boolean) request.getSession().getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            // 如果不是管理员，返回未授权错误
            response.getWriter().write("{\"error\": \"Unauthorized access\"}");
            return;
        }

        try {
            // 判断请求是否为JSON格式
            if (request.getContentType() != null && request.getContentType().contains("application/json")) {
                // 读取JSON格式的请求数据
                StringBuilder buffer = new StringBuilder();
                String line;
                try (BufferedReader reader = request.getReader()) {
                    // 逐行读取请求体内容
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                }

                // 使用Gson解析JSON数据
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(buffer.toString(), JsonObject.class);
                // 获取操作类型
                String action = jsonObject.get("action").getAsString();

                // 根据不同的操作类型执行相应的处理
                switch (action) {
                    case "update":
                        // 处理更新管理员信息的请求
                        updateAdmin(jsonObject, request, response);
                        break;
                    case "add":
                        // 处理添加新管理员的请求
                        addAdmin(request, response);
                        break;
                    case "delete":
                        // 处理删除管理员的请求
                        deleteAdmin(request, response);
                        break;
                    default:
                        // 处理未知的操作类型
                        response.getWriter().write("{\"error\": \"Unknown action\"}");
                }
            } else {
                // 处理普通的表单提交请求
                String action = request.getParameter("action");
                switch (action) {
                    case "add":
                        // 处理添加新管理员的请求
                        addAdmin(request, response);
                        break;
                    case "delete":
                        // 处理删除管理员的请求
                        deleteAdmin(request, response);
                        break;
                    default:
                        // 处理未知的操作类型
                        response.getWriter().write("{\"error\": \"Unknown action\"}");
                }
            }
        } catch (Exception e) {
            // 发生异常时返回错误信息
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * 获取管理员列表的方法
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     */
    private void listAdmins(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        // 获取数据库连接
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // 获取搜索参数
            String searchTerm = request.getParameter("search");

            // 构建SQL查询语句
            StringBuilder sql = new StringBuilder("SELECT * FROM users WHERE is_admin = 1");
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                sql.append(" AND username LIKE ?");
            }

            // 加载MySQL驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 获取数据库连接
            conn = DriverManager.getConnection(DatabaseConfig.DB_URL, DatabaseConfig.USER, DatabaseConfig.PASS);
            // 准备预编译SQL语句
            stmt = conn.prepareStatement(sql.toString());

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // 设置LIKE条件参数
                stmt.setString(1, "%" + searchTerm.trim() + "%");
            }

            // 执行查询并获取结果集
            rs = stmt.executeQuery();

            // 构建JSON数组
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[");

            boolean first = true;
            while (rs.next()) {
                if (!first) {
                    jsonBuilder.append(",");
                }
                first = false;

                // 从数据库结果集中获取管理员的详细信息
                long id = rs.getLong("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String mobile = rs.getString("mobile");

                // 构建包含管理员信息的JSON对象
                jsonBuilder.append("{")
                    // 添加管理员ID
                    .append("\"id\":").append(id).append(",")
                    // 添加用户名（经过特殊字符转义）
                    .append("\"username\":\"").append(escapeJson(username)).append("\",")
                    // 添加密码（经过特殊字符转义）
                    .append("\"password\":\"").append(escapeJson(password)).append("\",")
                    // 添加邮箱（经过特殊字符转义）
                    .append("\"email\":\"").append(escapeJson(email)).append("\",")
                    // 添加手机号（经过特殊字符转义）
                    .append("\"mobile\":\"").append(escapeJson(mobile)).append("\",")
                    // 设置管理员标志为true
                    .append("\"isAdmin\":true,")
                    // 设置用户类型为超级管理员
                    .append("\"userType\":\"超级管理员\"")
                    .append("}");

                // 打印调试信息：显示找到的管理员信息
                System.out.println("Found admin: " + username + ", ID: " + id);
            }

            // 结束JSON数组的构建
            jsonBuilder.append("]");

            // 获取完整的JSON响应字符串
            String jsonResponse = jsonBuilder.toString();
            // 打印调试信息：显示完整的JSON响应
            System.out.println("JSON Response: " + jsonResponse);

            // 将构建好的JSON数据写入HTTP响应
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            // 发生异常时打印错误信息
            System.err.println("Error in listAdmins: " + e.getMessage());
            // 打印完整的异常堆栈跟踪
            e.printStackTrace();
            // 将错误信息转换为JSON格式并写入响应
            response.getWriter().write("[{\"error\":\"" + escapeJson(e.getMessage()) + "\"}]");
        } finally {
            // 确保所有数据库资源都被正确关闭
            if (rs != null) rs.close();      // 关闭结果集
            if (stmt != null) stmt.close();   // 关闭预处理语句
            if (conn != null) conn.close();   // 关闭数据库连接
        }
    }

    /**
     * 添加新管理员的方法
     * 处理添加新管理员的请求，包括获取表单数据和数据库操作
     * @param request HTTP请求对象，包含新管理员的信息
     * @param response HTTP响应对象，用于返回处理结果
     * @throws SQLException 当数据库操作出错时抛出
     * @throws IOException 当I/O操作出错时抛出
     */
    private void addAdmin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        // 从请求中获取新管理员的基本信息
        String username = request.getParameter("username");  // 获取用户名
        String password = request.getParameter("password");  // 获取密码
        String email = request.getParameter("email");       // 获取邮箱
        String mobile = request.getParameter("mobile");     // 获取手机号

        try (Connection conn = DriverManager.getConnection(DatabaseConfig.DB_URL, DatabaseConfig.USER, DatabaseConfig.PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     // SQL语句：插入新管理员记录，设置is_admin为1表示管理员身份
                     "INSERT INTO users (username, password, email, mobile, is_admin) VALUES (?, ?, ?, ?, 1)")) {

            // 设置SQL语句的参数
            stmt.setString(1, username);  // 设置用户名
            stmt.setString(2, password);  // 设置密码
            stmt.setString(3, email);     // 设置邮箱
            stmt.setString(4, mobile);    // 设置手机号

            // 执行SQL插入操作，获取影响的行数
            int result = stmt.executeUpdate();

            // 将操作结果转换为JSON格式并写入响应
            response.getWriter().write("{\"success\": " + (result > 0) + "}");

            // 记录操作日志
            LogUtils.logOperation(
                "添加管理员",                                                  // 操作类型
                "添加管理员: " + username,                                     // 操作描述
                (String) request.getSession().getAttribute("username"),      // 操作者
                request,                                                     // 请求对象
                "成功"                                                       // 操作结果
            );
        }
    }

    /**
     * 更新管理员信息的方法
     * 处理更新管理员信息的请求，包括基本信息更新和权限修改
     * @param data JSON数据对象，包含要更新的管理员信息
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @throws SQLException 数据库操作异常
     * @throws IOException IO操作异常
     */
    private void updateAdmin(JsonObject data, HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        // 从JSON数据中提取管理员信息
        long id = data.get("id").getAsLong();                // 管理员ID
        String username = data.get("username").getAsString(); // 用户名
        String email = data.get("email").getAsString();      // 邮箱
        String mobile = data.get("mobile").getAsString();    // 手机号
        boolean isAdmin = data.get("isAdmin").getAsBoolean();// 是否为管理员
        // 密码是可选的，如果存在则更新
        String password = data.has("password") ? data.get("password").getAsString() : null;

        // 打印调试信息
        System.out.println("Updating admin - ID: " + id + ", isAdmin: " + isAdmin);

        // 声明数据库连接和预处理语句
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // 获取数据库连接
            conn = DriverManager.getConnection(DatabaseConfig.DB_URL, DatabaseConfig.USER, DatabaseConfig.PASS);

            // 检查是否是最后一个管理员
            if (!isAdmin) {
                // SQL查询：统计除了当前管理员外的管理员数量
                String countSql = "SELECT COUNT(*) FROM users WHERE is_admin = 1 AND id != ?";
                try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
                    countStmt.setLong(1, id);  // 设置当前管理员ID
                    ResultSet rs = countStmt.executeQuery();  // 执行查询
                    // 如果没有其他管理员，不允许降级
                    if (rs.next() && rs.getInt(1) == 0) {
                        // 返回错误信息
                        response.getWriter().write("{\"success\": false, \"error\": \"不能将最后一个管理员改为普通用户！\"}");
                        return;
                    }
                }
            }

            // 构建动态更新SQL语句
            StringBuilder sql = new StringBuilder("UPDATE users SET email = ?, mobile = ?, is_admin = ?");
            // 如果提供了新密码，添加密码更新
            if (password != null && !password.trim().isEmpty()) {
                sql.append(", password = ?");
            }
            sql.append(" WHERE id = ?");

            // 准备预编译SQL语句
            stmt = conn.prepareStatement(sql.toString());

            // 设置SQL参数的值
            int paramIndex = 1;
            stmt.setString(paramIndex++, email);     // 设置邮箱
            stmt.setString(paramIndex++, mobile);    // 设置手机号
            stmt.setInt(paramIndex++, isAdmin ? 1 : 0);  // 设置管理员标志

            // 如果有新密码，则设置密码参数
            if (password != null && !password.trim().isEmpty()) {
                stmt.setString(paramIndex++, password);
            }
            // 设置管理员ID作为WHERE条件
            stmt.setLong(paramIndex, id);

            // 执行SQL更新操作，获取受影响的行数
            int result = stmt.executeUpdate();

            // 打印调试信息：显示更新结果
            System.out.println("Update result: " + result);

            // 将更新结果转换为JSON格式并写入响应
            response.getWriter().write("{\"success\": " + (result > 0) + "}");

            // 记录操作日志
            LogUtils.logOperation(
                "更新管理员",                                                  // 操作类型
                "更新管理员信息: " + username,                                 // 操作描述
                (String) request.getSession().getAttribute("username"),      // 操作者
                request,                                                     // 请求对象
                "成功"                                                       // 操作结果
            );
        } catch (Exception e) {
            // 发生异常时打印错误信息
            System.err.println("Error updating admin: " + e.getMessage());
            // 打印完整的异常堆栈跟踪
            e.printStackTrace();
            // 将错误信息转换为JSON格式并写入响应
            response.getWriter().write("{\"success\": false, \"error\": \"" + escapeJson(e.getMessage()) + "\"}");
        } finally {
            // 关闭数据库资源
            if (stmt != null) stmt.close();  // 关闭预处理语句
            if (conn != null) conn.close();  // 关闭数据库连接
        }
    }

    /**
     * 删除管理员的方法
     * 处理删除管理员的请求，验证是否为最后一个管理员，执行删除操作
     * @param request HTTP请求对象，包含要删除的管理员ID
     * @param response HTTP响应对象，用于返回处理结果
     * @throws SQLException 数据库操作异常
     * @throws IOException IO操作异常
     */
    private void deleteAdmin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        // 获取管理员ID
        long id = Long.parseLong(request.getParameter("id"));

        // 检查是否是最后一个管理员
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.DB_URL, DatabaseConfig.USER, DatabaseConfig.PASS)) {
            // SQL查询：统计管理员数量
            String countSql = "SELECT COUNT(*) FROM users WHERE is_admin = 1";
            try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
                ResultSet rs = countStmt.executeQuery();
                // 如果只有一个管理员，不允许删除
                if (rs.next() && rs.getInt(1) <= 1) {
                    response.getWriter().write("{\"success\": false, \"error\": \"不能删除最后一个管理员！\"}");
                    return;
                }
            }

            // 如果不是最后一个管理员，则执行删除
            // SQL语句：删除管理员记录
            String deleteSql = "DELETE FROM users WHERE id = ? AND is_admin = 1";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setLong(1, id);
                int result = deleteStmt.executeUpdate();
                response.getWriter().write("{\"success\": " + (result > 0) + "}");
            }
        }

        // 记录操作日志
        LogUtils.logOperation(
            "删除管理员",                                                  // 操作类型
            "删除管理员ID: " + id,                                         // 操作描述
            (String) request.getSession().getAttribute("username"),      // 操作者
            request,                                                     // 请求对象
            "成功"                                                       // 操作结果
        );
    }

    /**
     * 获取单个管理员信息的方法
     * 处理获取单个管理员信息的请求，包括获取管理员ID和数据库操作
     * @param request HTTP请求对象，包含管理员ID
     * @param response HTTP响应对象，用于返回处理结果
     * @throws SQLException 数据库操作异常
     * @throws IOException IO操作异常
     */
    private void getAdmin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        // 获取管理员ID
        long id = Long.parseLong(request.getParameter("id"));

        try (Connection conn = DriverManager.getConnection(DatabaseConfig.DB_URL, DatabaseConfig.USER, DatabaseConfig.PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     // SQL语句：根据ID获取管理员信息
                     "SELECT * FROM users WHERE id = ? AND is_admin = 1")) {

            // 设置管理员ID参数
            stmt.setLong(1, id);

            // 执行查询并获取结果集
            ResultSet rs = stmt.executeQuery();

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

                System.out.println("Admin JSON response: " + jsonBuilder.toString());

                // 将JSON数据写入HTTP响应
                response.getWriter().write(jsonBuilder.toString());
            } else {
                response.getWriter().write("{\"error\": \"Admin not found\"}");
            }
        } catch (Exception e) {
            System.err.println("Error in getAdmin: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    /**
     * 转义JSON字符串的方法
     * 将特殊字符转换为JSON安全的格式，防止JSON注入攻击
     * @param input 需要转义的输入字符串
     * @return 转义后的安全字符串
     */
    private String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        // 替换所有特殊字符为转义字符
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\b", "\\b")
                   .replace("\f", "\\f")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}
