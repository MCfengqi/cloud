<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
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

        /* 主要��容区域 */
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
            height: 400px;
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
            -webkit-line-clamp: 3;
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
            <a href="#games">游戏中心</a>
            <a href="#news">新闻资讯</a>
            <a href="#about">关于我们</a>
            <a href="https://www.mcfengqi.icu/">我的网站</a>
            <a href="https://img.mcfengqi.icu/">我的图床</a>
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
                    String gameImg = rs.getString("gameimg");
                    String gameName = rs.getString("gamename");
                    String gameDesc = rs.getString("gametxt");
                    String gameLink = rs.getString("gamelink");
        %>
        <div class="game-card">
            <img src="<%= gameImg %>" alt="<%= gameName %>" class="game-image"
                 onerror="this.src='images/default-game.png'">
            <div class="game-info">
                <h3 class="game-title"><%= gameName %></h3>
                <p class="game-description"><%= gameDesc %></p>
                <a href="xiangqing.jsp?gameName=<%= gameName %>&gameDesc=<%= gameDesc %>&gameImg=<%= gameImg %>" class="game-button" >点击详情</a>
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
</body>
</html>
