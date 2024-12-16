<%--
  Created by IntelliJ IDEA.
  User: 林龙
  Date: 2024/12/16
  Time: 上午8:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>游戏详情</title>
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


        .main-content {
            margin-top: 80px;
            padding: 20px;
            max-width: 1200px;
            margin-left: auto;
            margin-right: auto;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            display: flex; /* 使用 Flexbox 布局 */
        }

        .image-container {
            flex: 1; /* 左侧占据 1 份空间 */
            padding: 20px;
        }

        .details-container {
            flex: 1; /* 右侧占据 1 份空间 */
            padding: 20px;
        }

        .game-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .game-description {
            color: #666;
            font-size: 16px;
            margin-bottom: 15px;
        }

        .game-image {
            width: 100%;
            height: auto;
            border-radius: 8px;
            margin-bottom: 15px;
        }

        .game-link {
            display: inline-block;
            padding: 10px 15px;
            background-color: #007bff;
            color: #fff;
            text-decoration: none;
            border-radius: 4px;
            transition: background-color 0.3s;
        }

        .game-link:hover {
            background-color: #0056b3;
        }

        /* 页脚样式 */
        .footer {
            background-color: #333;
            color: #fff;
            padding: 30px 0;
            margin-top: 50px;
        }

        .footer-content {
            max-width: 1200px;
            margin: 0 auto;
            text-align: center;
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
            <a href="#about">关于我们</a>
            <a href="#contact">联系我们</a>
        </nav>
        <div class="auth-buttons">
            <a href="login.jsp" class="login-btn">登录</a>
            <a href="register.jsp" class="register-btn">注册</a>
        </div>
    </div>
</header>

<!-- 主要内容区域 -->
<main class="main-content">
    <%
        // 获取传递的参数
        String gameId = request.getParameter("gameId");
        String gameName = request.getParameter("gameName");
        String gameDesc = request.getParameter("gameDesc");
        String gameImg = request.getParameter("gameImg");
        String gameLinks = request.getParameter("gameLink");
    %>`
    <div class="image-container">
        <img src="<%= gameImg %>" alt="<%= gameName %>" class="game-image"/>
    </div>
    <div class="details-container">
        <h1 class="game-title">游戏详情</h1>
        <p class="game-description">游戏名称：<%= gameName %>
        </p>
        <p class="game-description">游戏描述：<%= gameDesc %>
        </p>
        <a href="<%= gameImg %>" class="game-link" target="_blank">游戏链接</a>
    </div>
</main>

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
