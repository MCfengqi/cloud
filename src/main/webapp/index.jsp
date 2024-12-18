<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.math.BigDecimal" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>云城游戏门户</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
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

        /* 主要内区域 */
        .main-content {
            margin-top: 0px;
            padding: 20px;
            max-width: 1200px;
            margin-left: auto;
            margin-right: auto;
        }

        /* 游戏展示区域 */
        .game-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 20px;
            padding: 20px 0;
        }

        .game-card {
            background: #fff;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            transition: transform 0.3s;
            display: flex;
            flex-direction: column;
            height: 500px;
        }

        .game-card:hover {
            transform: translateY(-5px);
        }

        .game-image {
            width: 100%;
            height: 200px;
            object-fit: cover;
        }

        .game-info {
            padding: 15px;
            display: flex;
            flex-direction: column;
            flex: 1;
            position: relative;
        }

        .game-title {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .game-description {
            color: #666;
            font-size: 14px;
            margin-bottom: 15px;
            overflow: hidden;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            flex: 1;
        }

        .game-button {
            display: inline-block;
            padding: 8px 15px;
            background-color: #007bff;
            color: #fff;
            text-decoration: none;
            border-radius: 4px;
            transition: background-color 0.3s;
            text-align: center;
            position: absolute;
            bottom: 15px;
            left: 15px;
            right: 15px;
        }

        .game-button:hover {
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

        /* 添加轮播图样式 */
        .carousel {
            width: 100%;
            max-height: 600px; /* 设置最大高度 */
            overflow: hidden;
        }

        .carousel-inner {
            width: 100%;
            height: 100%;
        }

        .carousel-item {
            width: 100%;
            height: 600px; /* 固定高度 */
        }

        .carousel-item img {
            width: 100%;
            height: 100%;
            object-fit: cover; /* 保持比例填充 */
            object-position: center; /* 居中显示 */
        }

        /* 模态框样式 */
        .modal {
            display: none; /* 默认隐藏 */
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0, 0, 0, 0.5); /* 半透明背景 */
        }

        .modal-content {
            background-color: #fff;
            margin: 15% auto; /* 在页面中居中 */
            padding: 20px;
            border: 1px solid #888;
            width: 40%; /* 宽度 */
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }

        .game-price {
            font-size: 20px;
            color: #ff4d4f;
            font-weight: bold;
            margin: 15px 0;
            display: flex;
            align-items: center;
            background: rgba(255, 77, 79, 0.1);
            padding: 8px 12px;
            border-radius: 6px;
            position: relative;
            overflow: hidden;
            margin-bottom: 60px;
        }

        .game-price::before {
            content: '￥';
            font-size: 16px;
            margin-right: 2px;
            color: #ff4d4f;
        }

        .game-price::after {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(45deg, rgba(255, 77, 79, 0.05), rgba(255, 77, 79, 0.1));
            z-index: -1;
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
            <a href="#" id="logQueryBtn" class="nav-links">日志查询</a>
<%--            <a href="https://www.mcfengqi.icu/">我的网站</a>--%>
            <a href="dingdan.jsp">我的订单</a>
        </nav>
        <div class="auth-buttons">
            <a href="login.jsp" class="login-btn">登录</a>
            <a href="register.jsp" class="register-btn">注册</a>
        </div>
    </div>
</header>

<!-- 轮播图 -->
<div id="imageCarousel" class="carousel slide" data-bs-ride="carousel" data-bs-interval="2000" style="margin-top: 50px;">
    <!-- 指示器 -->
    <div class="carousel-indicators">
        <button type="button" data-bs-target="#imageCarousel" data-bs-slide-to="0" class="active"></button>
        <button type="button" data-bs-target="#imageCarousel" data-bs-slide-to="1"></button>
        <button type="button" data-bs-target="#imageCarousel" data-bs-slide-to="2"></button>
    </div>

    <!-- 轮播图片 -->
    <div class="carousel-inner">
        <div class="carousel-item active">
            <img src="https://img.mcfengqi.icu/LightPicture/2024/12/989d786430a14afa.jpg" class="d-block w-100" alt="游戏图片1">
        </div>
        <div class="carousel-item">
            <img src="https://img.mcfengqi.icu/LightPicture/2024/12/ad81b155043e66ad.jpg" class="d-block w-100" alt="游戏图片2">
        </div>
        <div class="carousel-item">
            <img src="https://img.mcfengqi.icu/LightPicture/2024/12/d3b164437abf8e16.jpg" class="d-block w-100" alt="游戏图片3">
        </div>
    </div>

    <!-- 控制按钮 -->
    <button class="carousel-control-prev" type="button" data-bs-target="#imageCarousel" data-bs-slide="prev">
        <span class="carousel-control-prev-icon"></span>
        <span class="visually-hidden">Previous</span>
    </button>
    <button class="carousel-control-next" type="button" data-bs-target="#imageCarousel" data-bs-slide="next">
        <span class="carousel-control-next-icon"></span>
        <span class="visually-hidden">Next</span>
    </button>
</div>

<!-- 主要内容区域 -->
<main class="main-content">
    <div class="game-grid">
        <%
            // 修改数据库连接信息
            String DB_URL = "jdbc:mysql://localhost:3306/cloudcity";
            String USER = "root";     // 修改用户名
            String PASS = "123456";   // 修改密码

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

                // 查询游戏列表
                String sql = "SELECT * FROM gamelist ORDER BY created_at DESC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                // 遍历结果集并显示游戏卡片
                while(rs.next()) {
                    int gameId = rs.getInt("gameid");
                    String gameImg = rs.getString("gameimg");
                    String gameName = rs.getString("gamename");
                    String gameDesc = rs.getString("gametxt");
                    String gameLink = rs.getString("gamelink");
                    BigDecimal gameMoney=rs.getBigDecimal("gamemoney");
        %>
        <div class="game-card">
            <img src="<%= gameImg %>" alt="<%= gameName %>" class="game-image"
                 onerror="this.src='images/default-game.png'">
            <div class="game-info">
                <h3 class="game-title"><%= gameName %></h3>
                <p class="game-description"><%= gameDesc %></p>
                <div class="game-price">价格：<%= gameMoney %></div>
                <a href="xiangqing.jsp?gameName=<%= gameName %>&gameDesc=<%= gameDesc %>&gameMoney=<%= gameMoney %>&gameImg=<%= gameImg %>&gameId=<%= gameId %>" class="game-button">点击详情</a>
            </div>
        </div>
        <%
                }

                // 关闭数据库连接
                rs.close();
                stmt.close();
                conn.close();

            } catch(Exception e) {
                e.printStackTrace();
                out.println("获取游戏列表失败请检查网络或数据库" + e.getMessage());
            }
        %>
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

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // 初始化轮播图
    document.addEventListener('DOMContentLoaded', function() {
        var myCarousel = document.getElementById('imageCarousel');
        var carousel = new bootstrap.Carousel(myCarousel, {
            interval: 2000,
            ride: true
        });
        // 强制开始自动播放
        carousel.cycle();
    });
</script>
<script>
    // 获取模态框
    var modal = document.getElementById("logModal");

    // 获取按钮
    var btn = document.getElementById("logQueryBtn");

    // 获取关闭按钮
    var span = document.getElementsByClassName("close")[0];

    // 当用户点击按钮时，打开模态框
    btn.onclick = function() {
        modal.style.display = "block";
        loadLogs(); // 加载日志内容
    }

    // 当用户点击关闭按钮时，关闭模态框
    span.onclick = function() {
        modal.style.display = "none";
    }

    // 当用户点击模态框外部时，关闭模态框
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

    // 加载日志内容的函数
    function loadLogs() {
        // 使用 AJAX 请求获取日志内容
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "getLogs.jsp", true); // 假设你有一个 getLogs.jsp 文件来处理日志查询
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4 && xhr.status == 200) {
                document.getElementById("logContent").innerHTML = xhr.responseText;
            }
        };
        xhr.send();
    }
</script>
</body>
</html>
