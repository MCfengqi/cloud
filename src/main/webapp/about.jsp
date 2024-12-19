<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>关于我们 - 云城游戏</title>
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

        /* 主要内容区域 */
        .main-content {
            margin-top: 100px;
            padding: 40px;
            max-width: 1200px;
            margin-left: auto;
            margin-right: auto;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 60px;
        }

        .about-section {
            margin-bottom: 40px;
        }

        .about-section h2 {
            color: #333;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #007bff;
        }

        .developer-info {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 30px;
            margin-top: 30px;
        }

        .team-member {
            background: #ffffff;
            border-radius: 15px;
            padding: 30px;
            text-align: center;
            box-shadow: 0 10px 20px rgba(0,0,0,0.05);
            transition: all 0.3s ease;
            border: 1px solid #eaeaea;
        }

        .team-member:hover {
            transform: translateY(-10px);
            box-shadow: 0 15px 30px rgba(0,0,0,0.1);
        }

        .member-info img {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            margin-bottom: 20px;
            border: 4px solid #fff;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            transition: transform 0.3s ease;
        }

        .team-member:hover img {
            transform: scale(1.05);
        }

        .member-info h3 {
            color: #2c3e50;
            font-size: 24px;
            margin-bottom: 10px;
            font-weight: 600;
        }

        .member-info p {
            color: #666;
            margin: 8px 0;
            font-size: 16px;
            line-height: 1.6;
        }

        .member-info a {
            color: #007bff;
            text-decoration: none;
            transition: color 0.3s ease;
        }

        .member-info a:hover {
            color: #0056b3;
            text-decoration: underline;
        }

        .member-info p:nth-child(3) {
            background: #e3f2fd;
            color: #1976d2;
            padding: 5px 15px;
            border-radius: 20px;
            display: inline-block;
            font-weight: 500;
            margin: 10px 0;
        }

        .contact-info {
            margin-top: 15px;
            padding-top: 15px;
            border-top: 1px solid #eee;
        }

        .contact-info a {
            color: #007bff;
            text-decoration: none;
        }

        .contact-info a:hover {
            text-decoration: underline;
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

    <!-- 主要内容区域 -->
    <main class="main-content">
        <section class="about-section">
            <h2>关于云城游戏</h2>
            <p>云城游戏是一个致力于为玩家提供优质游戏体验的在线游戏平台。我们的目标是打造一个安全、便捷、有趣的游戏交流社区平台。</p>
        </section>

        <section class="about-section">
            <h2>开发团队</h2>
            <div class="developer-info">
                <div class="team-member">
                    <div class="member-info">
                        <img src="https://img.mcfengqi.icu/LightPicture/2024/12/444fd65b53e8633e.jpg" alt="党林龙的头像" style="width: 100px; height: 100px; border-radius: 50%; margin-bottom: 15px;">
                        <h3>党林龙</h3>
                        <p>前端开发</p>
                        <p>负责项目的前端开发和UI设计</p>
                        <p>邮箱：<a href="mailto:qxsj@vip.qq.com">qxsj@vip.qq.com</a></p>
                        <p>个人网站：<a href="https://www.mcfengqi.icu" target="_blank">www.mcfengqi.icu</a></p>
                    </div>
                </div>
                <div class="team-member">
                    <div class="member-info">
                        <img src="https://img.mcfengqi.icu/LightPicture/2024/12/47192fae4356cb49.png" alt="杜旭晨的头像" style="width: 100px; height: 100px; border-radius: 50%; margin-bottom: 15px;">
                        <h3>杜旭晨</h3>
                        <p>后端开发</p>
                        <p>负责项目的后端开发和数据库设计</p>
                        <p>邮箱：<a href="mailto:qxsj@vip.qq.com">qxsj@vip.qq.com</a></p>
                        <p>个人网站：<a href="https://www.mcfengqi.icu" target="_blank">www.mcfengqi.icu</a></p>
                    </div>
                </div>
            </div>
        </section>

        <section class="about-section">
            <h2>技术栈</h2>
            <p>前端：HTML5, CSS3, JavaScript</p>
            <p>后端：Java, Servlet, JSP</p>
            <p>数据库：MySQL</p>
            <p>端口：8080</p>
            <p>服务器：Tomcat10.0.23</p>
        </section>
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
