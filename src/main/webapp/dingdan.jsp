<%--
  Created by IntelliJ IDEA.
  User: 林龙
  Date: 2024/12/18
  Time: 上午8:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.math.BigDecimal" %>
<html>
<head>
    <title>我的订单</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Microsoft YaHei', Arial, sans-serif;
            background-color: #f4f4f4;
            line-height: 1.6;
        }

        /* 头部样式 */
        .header {
            background-color: #fff;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            padding: 15px 50px;
            position: fixed;
            width: 100%;
            top: 0;
            z-index: 1000;
        }

        .header-content {
            display: flex;
            justify-content: space-between;
            align-items: center;
            max-width: 1200px;
            margin: 0 auto;
        }

        .logo {
            display: flex;
            align-items: center;
            gap: 10px;
            text-decoration: none;
            color: #333;
        }

        .logo span {
            font-size: 24px;
            font-weight: bold;
        }

        .nav-links {
            display: flex;
            gap: 30px;
        }

        .nav-links a {
            text-decoration: none;
            color: #333;
            font-size: 16px;
            transition: color 0.3s;
        }

        .nav-links a:hover {
            color: #007bff;
        }

        .auth-buttons {
            display: flex;
            gap: 15px;
        }

        .auth-buttons a {
            text-decoration: none;
            padding: 8px 20px;
            border-radius: 4px;
            transition: all 0.3s;
        }

        .login-btn {
            color: #007bff;
            border: 1px solid #007bff;
        }

        .login-btn:hover {
            background-color: #007bff;
            color: #fff;
        }

        .register-btn {
            background-color: #007bff;
            color: #fff;
            border: 1px solid #007bff;
        }

        .register-btn:hover {
            background-color: #0056b3;
            border-color: #0056b3;
        }

        /* 页脚样式 */
        .footer {
            background-color: #333;
            color: #fff;
            padding: 30px 0;
            position: fixed;
            bottom: 0;
            width: 100%;
            z-index: 100;
        }

        .footer-content {
            max-width: 1200px;
            margin: 0 auto;
            text-align: center;
        }

        /* 添加订单列表样式 */
        .container {
            max-width: 1200px;
            margin: 100px auto 150px;
            padding: 20px;
        }

        .order-item {
            background: #fff;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            display: flex;
            gap: 20px;
        }

        .game-image-container {
            flex: 0 0 200px;
            height: 200px;
        }

        .game-image-container img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            border-radius: 4px;
        }

        .order-info {
            flex: 1;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }

        .game-name {
            font-size: 20px;
            font-weight: bold;
            margin-bottom: 10px;
            display: inline-block;
        }

        .game-desc {
            color: #666;
            margin-bottom: 15px;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }

        .order-details {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 10px;
            color: #666;
            font-size: 14px;
        }

        .order-amount {
            font-size: 18px;
            color: #ff4d4f;
            font-weight: bold;
        }

        .order-status {
            padding: 4px 12px;
            border-radius: 4px;
            background: #f6ffed;
            color: #52c41a;
            border: 1px solid #b7eb8f;
        }

        /* 添加订单编号样式 */
        .order-number {
            color: #999;
            font-size: 14px;
            padding: 4px 8px;
            background-color: #f8f8f8;
            border-radius: 4px;
            display: inline-block;
            float: right;
            margin-top: 3px;
        }

        .order-number::before {
            content: "📋";
            margin-right: 5px;
            font-size: 16px;
        }

        /* 添加清除浮动 */
        .title-line {
            overflow: hidden;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<!-- 头部导航 -->
<header class="header">
    <div class="header-content">
        <a href="index.jsp" class="logo">
            <span>🎮 云城游戏</span>
        </a>
        <nav class="nav-links">
            <a href="index.jsp">游戏中心</a>
            <a href="#news">新闻资讯</a>
            <a href="dingdan.jsp">我的订单</a>
            <a href="https://img.mcfengqi.icu" target="_blank">我的图床</a>
            <a href="about.jsp">关于我们</a>
        </nav>
        <div class="auth-buttons">
            <a href="login.jsp" class="login-btn">登录</a>
            <a href="register.jsp" class="register-btn">注册</a>
        </div>
    </div>
</header>

<div class="container">
    <%
        String DB_URL = "jdbc:mysql://localhost:3306/cloudcity";
        String USER = "root";
        String PASS = "123456";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT * FROM game_order ORDER BY datetime DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
    %>
    <div class="order-item">
        <div class="game-image-container">
            <img src="<%= rs.getString("gameimg") %>" alt="游戏图片">
        </div>
        <div class="order-info">
            <div>
                <div class="title-line">
                    <h3 class="game-name"><%= rs.getString("gamename") %></h3>
                    <p class="order-number">订单编号：<%=rs.getInt("id")%></p>
                </div>
                <p class="game-desc"><%= rs.getString("gametxt") %></p>
                <div class="order-details">
                    <span>订单时间：<%= rs.getTimestamp("datetime") %></span>
                    <span>支付方式：<%= rs.getString("paytype") %></span>
                </div>
            </div>
            <div>
                <span class="order-amount">￥<%= rs.getBigDecimal("amount") %></span>
                <span class="order-status"><%= rs.getString("status") %></span>
            </div>
        </div>
    </div>
    <%
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch(Exception e) {
            e.printStackTrace();
            out.println("获取订单列表失败：" + e.getMessage());
        }
    %>
</div>

<!-- 页脚 -->
<footer class="footer">
    <div class="footer-content">
        <p>© 2024 云城游戏门户. All rights reserved.</p>
        <p>
            <a href="https://beian.miit.gov.cn/" target="_blank" style="color: #fff; text-decoration: none;">
                陕ICP备2024054614号
            </a>
            &nbsp;|&nbsp;
            <a href="http://www.beian.gov.cn/portal/registerSystemInfo" target="_blank" style="color: #fff; text-decoration: none;">
                <img src="https://img.mcfengqi.icu/LightPicture/2024/11/75fb7a50447cf897.png" alt="公安备案图标" style="vertical-align: middle; margin-right: 3px;">
                陕公网安备61019102000653
            </a>
        </p>
    </div>
</footer>
</body>
</html>
