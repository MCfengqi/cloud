package com.example.cloudcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/UserLoginServlet")
public class UserLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // 数据库连接信息
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity";
    private static final String USER = "cloudcity";
    private static final String PASS = "cloudcity";
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        System.out.println("Login attempt - Username: " + username);
        
        try {
            UserInfo userInfo = validateLogin(username, password);
            if (userInfo != null) {
                // 登录成功，创建session
                HttpSession session = request.getSession(true);
                session.setAttribute("username", username);
                session.setAttribute("isAdmin", userInfo.isAdmin);
                
                System.out.println("Login successful - Username: " + username + ", IsAdmin: " + userInfo.isAdmin);
                System.out.println("Session ID: " + session.getId());
                
                response.sendRedirect("welcome.jsp");
            } else {
                System.out.println("Login failed - Invalid credentials");
                request.setAttribute("msg", "用户名或密码错误！");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("msg", "系统错误，请稍后重试！");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    
    // 创建一个内部类来保存用户信息
    private static class UserInfo {
        String username;
        boolean isAdmin;
        
        UserInfo(String username, boolean isAdmin) {
            this.username = username;
            this.isAdmin = isAdmin;
        }
    }
    
    private UserInfo validateLogin(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
            String sql = "SELECT username, is_admin FROM users WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return new UserInfo(
                    rs.getString("username"),
                    rs.getBoolean("is_admin")
                );
            }
            return null;
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
