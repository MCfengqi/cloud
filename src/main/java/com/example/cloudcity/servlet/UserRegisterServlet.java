/**
 * 用户注册Servlet
 * 用途：处理用户注册相关的所有后端请求，包括：
 * 1. 用户注册信息的验证
 * 2. 用户名重复检查
 * 3. 新用户信息的保存
 * 4. 注册结果的反馈
 * 5. 错误信息的处理
 */
package com.example.cloudcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;

import com.example.cloudcity.utils.DatabaseConfig;

@WebServlet("/UserRegisterServlet")
public class UserRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置响应内容类型
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        // 获取表单数据
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        // 增强输入验证
        if (username == null || username.trim().length() < 3) {
            request.setAttribute("msg", "用户名不能少于3个字符！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (password == null || password.length() < 6) {
            request.setAttribute("msg", "密码不能少于6个字符！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            request.setAttribute("msg", "请输入有效的邮箱地址！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (mobile == null || !mobile.matches("^1[3-9]\\d{9}$")) {
            request.setAttribute("msg", "请输入有效的手机号码！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        try {
            String result = registerUser(username, password, email, mobile);
            if (result.equals("success")) {
                request.setAttribute("msg", "注册成功！");
                response.sendRedirect("login.jsp");
            } else {
                request.setAttribute("msg", result);
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("msg", "系统错误，请稍后重试！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            e.printStackTrace();
        }
    }

    private String registerUser(String username, String password, String email, String mobile) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement checkStmt = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                DatabaseConfig.DB_URL, 
                DatabaseConfig.USER, 
                DatabaseConfig.PASS);
            
            // 首先检查用户名是否已存在
            String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
            checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            var rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return "用户名已存在！";
            }

            // 如果用户名不存在，继续注册流程
            String sql = "INSERT INTO users (username, password, email, mobile) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password); // TODO: 在生产环境中应该加密密码
            pstmt.setString(3, email);
            pstmt.setString(4, mobile);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0 ? "success" : "注册失败，请重试！";
            
        } catch (SQLSyntaxErrorException e) {
            e.printStackTrace();
            return "数据库表结构错误，请确保users表已正确创建！";
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            return "该用户名已被注册！";
        } catch (SQLException e) {
            e.printStackTrace();
            return "数据库错误：" + e.getMessage();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "数据库驱动加载失败！";
        } finally {
            try {
                if (checkStmt != null) checkStmt.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
