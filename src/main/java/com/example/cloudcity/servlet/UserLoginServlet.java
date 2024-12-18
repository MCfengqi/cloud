/**
 * 用户登录Servlet
 * 用途：处理用户登录相关的所有后端请求，包括：
 * 1. 用户登录验证和认证
 * 2. 登录状态的维护和管理
 * 3. 登录日志的记录和追踪
 * 4. 登录安全控制和防护
 * 5. 会话管理和维护
 */
package com.example.cloudcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet; // 导入HttpServlet类
import jakarta.servlet.http.HttpServletRequest; // 导入HttpServletRequest类
import jakarta.servlet.http.HttpServletResponse; // 导入HttpServletResponse类
import jakarta.servlet.http.HttpSession; // 导入HttpSession类

import java.io.IOException; // 导入IOException类用于处理IO异常
import java.sql.Connection; // 导入Connection类用于数据库连接
import java.sql.DriverManager; // 导入DriverManager类用于加载数据库驱动
import java.sql.PreparedStatement; // 导入PreparedStatement类用于预编译SQL语句
import java.sql.ResultSet; // 导入ResultSet类用于存储查询结果
import java.sql.SQLException; // 导入SQLException类用于处理SQL异常
import com.example.cloudcity.utils.LogUtils; // 导入LogUtils类用于日志记录
import com.example.cloudcity.utils.DatabaseConfig;

@WebServlet("/UserLoginServlet")
public class UserLoginServlet extends HttpServlet { // 定义UserLoginServlet类继承自HttpServlet
    /** 序列化版本号 */
    private static final long serialVersionUID = 1L;

    /**
     * 处理POST请求
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8"); // 设置响应内容类型为HTML
        request.setCharacterEncoding("UTF-8"); // 设置请求字符编码为UTF-8

        String username = request.getParameter("username"); // 获取请求中的用户名参数
        String password = request.getParameter("password"); // 获取请求中的密码参数

        System.out.println("Login attempt - Username: " + username); // 打印登录尝试信息

        try {
            UserInfo userInfo = validateLogin(username, password); // 验证用户登录信息
            if (userInfo != null) {
                // 登录成功，创建session
                HttpSession session = request.getSession(true); // 获取当前会话或创建新会话
                session.setAttribute("username", username); // 设置会话属性username
                session.setAttribute("isAdmin", userInfo.isAdmin); // 设置会话属性isAdmin
                session.setAttribute("lastLoginTime", userInfo.lastLoginTime); // 设置会话属性lastLoginTime

                System.out.println("Login successful - Username: " + username + ", IsAdmin: " + userInfo.isAdmin); // 打印登录成功信息
                System.out.println("Session ID: " + session.getId()); // 打印会话ID

                LogUtils.logOperation(
                    "用户登录",
                    "用户 " + username + " 登录系统",
                    username,
                    request,
                    "成功"
                );

                response.sendRedirect("welcome.jsp"); // 重定向到欢迎页面
            } else {
                System.out.println("Login failed - Invalid credentials"); // 打印登录失败信息
                request.setAttribute("msg", "用户名或密码错误！"); // 设置请求属性msg
                request.getRequestDispatcher("login.jsp").forward(request, response); // 转发到登录页面
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage()); // 打印登录错误信息
            e.printStackTrace(); // 打印堆栈跟踪信息
            request.setAttribute("msg", "系统错误，请稍后重试！"); // 设置请求属性msg
            request.getRequestDispatcher("login.jsp").forward(request, response); // 转发到登录页面
        }
    }

    /**
     * 用户信息内部类
     * 用于存储用户的基本信息和登录状态
     */
    private static class UserInfo {
        /** 用户名 */
        String username;
        /** 是否为管理员 */
        boolean isAdmin;
        /** 最后登录时间 */
        java.sql.Timestamp lastLoginTime;

        /**
         * 构造函数
         * @param username 用户名
         * @param isAdmin 是否为管理员
         * @param lastLoginTime 最后登录时间
         */
        UserInfo(String username, boolean isAdmin, java.sql.Timestamp lastLoginTime) {
            this.username = username; // 初始化用户名
            this.isAdmin = isAdmin; // 初始化是否为管理员
            this.lastLoginTime = lastLoginTime; // 初始化最后登录时间
        }
    }

    /**
     * 验证用户登录信息
     * @param username 用户名
     * @param password 密码
     * @return UserInfo对象，如果验证失败返回null
     */
    private UserInfo validateLogin(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 加载MySQL驱动
            conn = DriverManager.getConnection(DatabaseConfig.DB_URL, DatabaseConfig.USER, DatabaseConfig.PASS); // 获取数据库连接

            // 验证用户名和密码
            String sql = "SELECT username, is_admin, updated_at FROM users WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(sql); // 准备预编译SQL语句
            pstmt.setString(1, username); // 设置用户名参数
            pstmt.setString(2, password); // 设置密码参数

            rs = pstmt.executeQuery(); // 执行查询并获取结果集
            if (rs.next()) {
                // 更新最后登录时间
                String updateSql = "UPDATE users SET updated_at = CURRENT_TIMESTAMP WHERE username = ?";
                updateStmt = conn.prepareStatement(updateSql); // 准备预编译SQL语句
                updateStmt.setString(1, username); // 设置用户名参数
                updateStmt.executeUpdate(); // 执行更新操作

                return new UserInfo(
                    rs.getString("username"), // 获取用户名
                    rs.getBoolean("is_admin"), // 获取是否为管理员
                    rs.getTimestamp("updated_at") // 获取最后登录时间
                );
            }
            return null; // 如果没有匹配的用户，返回null

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // 打印堆栈跟踪信息
            return null; // 返回null表示验证失败
        } finally {
            try {
                if (rs != null) rs.close(); // 关闭ResultSet
                if (pstmt != null) pstmt.close(); // 关闭PreparedStatement
                if (updateStmt != null) updateStmt.close(); // 关闭PreparedStatement
                if (conn != null) conn.close(); // 关闭Connection
            } catch (SQLException e) {
                e.printStackTrace(); // 打印堆栈跟踪信息
            }
        }
    }
}
