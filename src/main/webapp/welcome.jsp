<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page errorPage="error.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>云城游戏管理系统</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入外部CSS文件 -->
    <link rel="stylesheet" href="css/welcome.css">
    <link rel="stylesheet" href="css/modal.css">
    <!-- 添加必要的样式 -->
    <style>
        .submenu {
            display: none;
            padding-left: 20px;
            list-style: none;
            margin: 0;
            transition: all 0.3s ease;
        }
        
        .submenu.active {
            display: block !important;
        }
        
        .submenu-item {
            padding: 8px 15px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        
        .submenu-item:hover {
            background-color: rgba(255, 255, 255, 0.1);
        }
        
        .menu-item {
            list-style: none;
            margin: 5px 0;
        }
        
        .menu-item-wrapper {
            padding: 10px;
            cursor: pointer;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .menu-item-content {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .menu-icon {
            margin-right: 8px;
        }
        
        .dropdown-icon {
            transition: transform 0.3s ease;
        }
        
        .menu-item-wrapper.active .dropdown-icon {
            transform: rotate(180deg);
        }
        
        .content-wrapper {
            padding: 20px;
        }
        
        .table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 1rem;
            background-color: #fff;
        }
        
        .table th,
        .table td {
            padding: 12px;
            text-align: center; /* 居中对齐 */
            border: 1px solid #dee2e6;
            vertical-align: middle;
        }
        
        .table th {
            background-color: #f8f9fa;
            font-weight: bold;
        }
        
        .table tbody tr:nth-of-type(odd) {
            background-color: rgba(0, 0, 0, .05);
        }
        
        .btn {
            padding: 5px 10px;
            margin: 2px;
            cursor: pointer;
            border: none;
            border-radius: 3px;
        }
        
        .btn-primary { background-color: #007bff; color: white; }
        .btn-warning { background-color: #ffc107; color: black; }
        .btn-danger { background-color: #dc3545; color: white; }
        
        /* 模态框样式 */
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.5);
        }
        
        .modal-content {
            background-color: white;
            margin: 15% auto;
            padding: 20px;
            width: 70%;
            max-width: 500px;
            border-radius: 5px;
        }
        
        .close-btn {
            float: right;
            cursor: pointer;
            font-size: 20px;
        }
        
        /* 修改菜单和子菜单的样式 */
        .menu {
            list-style: none;
            padding: 0;
            margin: 0;
        }
        
        .menu-item {
            list-style: none;
            margin: 5px 0;
            position: relative; /* 添加相对定位 */
        }
        
        .menu-item-wrapper {
            padding: 10px;
            cursor: pointer;
            display: flex;
            justify-content: space-between;
            align-items: center;
            background-color: rgba(255, 255, 255, 0.05);
            border-radius: 4px;
        }
        
        .menu-item-wrapper:hover {
            background-color: rgba(255, 255, 255, 0.1);
        }
        
        .menu-item-content {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        /* 子菜单样式 */
        .submenu {
            display: none;
            list-style: none;
            padding: 0;
            margin: 0;
            background-color: rgba(0, 0, 0, 0.1);
            border-radius: 4px;
            overflow: hidden;
            transition: max-height 0.3s ease-out;
            max-height: 0;
        }
        
        .submenu.active {
            display: block !important;
            max-height: 200px; /* 设置一个足够大的高度 */
            margin-top: 5px;
        }
        
        .submenu-item {
            padding: 10px 20px 10px 40px;
            cursor: pointer;
            transition: background-color 0.2s;
            color: #fff;
            display: flex;
            align-items: center;
        }
        
        .submenu-item:hover {
            background-color: rgba(255, 255, 255, 0.1);
        }
        
        .menu-icon {
            margin-right: 8px;
            font-size: 16px;
        }
        
        .dropdown-icon {
            transition: transform 0.3s ease;
            font-size: 12px;
            color: #fff;
        }
        
        .menu-item-wrapper.active .dropdown-icon {
            transform: rotate(180deg);
        }
        
        /* 确保sidebar样式正确 */
        .sidebar {
            width: 250px;
            background-color: #2c3e50;
            color: white;
            padding: 20px;
            display: flex;
            flex-direction: column;
            height: 100vh;
        }
        
        .system-title {
            margin-bottom: 30px;
            text-align: center;
        }
        
        /* 修改sidebar-header样式 */
        .sidebar-header {
            margin-top: auto;
            padding: 20px 0;
            border-top: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        .user-info {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 10px 0;
        }
        
        .last-login {
            font-size: 12px;
            color: rgba(255, 255, 255, 0.7);
            padding-left: 28px;
        }
        
        /* 添加新的菜单图标样式 */
        .game-icon {
            color: #fff;
            font-size: 16px;
        }
        
        .btn-group {
            display: flex;
            justify-content: center;
            gap: 5px;
        }
        
        /* 修改sidebar样式 */
        .sidebar {
            width: 250px;
            background-color: #2c3e50;
            color: white;
            padding: 20px;
            display: flex;
            flex-direction: column;
            height: 100vh;
        }
        
        /* 修改菜单容器样式，添加flex-grow使其占据剩余空间 */
        .menu-container {
            flex-grow: 1;
            display: flex;
            flex-direction: column;
        }
        
        /* 修改底部用户信息样式 */
        .sidebar-header {
            margin-top: 20px;  /* 减小顶部间距 */
            padding: 15px 0;
            border-top: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        .toolbar {
            display: flex;
            align-items: center;  /* 垂直居中对齐 */
            gap: 10px;           /* 元素之间的间距 */
            margin-bottom: 20px;
            padding: 10px;
            background-color: #f8f9fa;
            border-radius: 4px;
        }
        
        .search-box {
            display: flex;
            align-items: center;
            gap: 10px;
            flex: 1;
        }
        
        .search-box input {
            height: 38px;      /* 与按钮高度保持一致 */
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            flex: 1;
        }
        
        .btn {
            height: 38px;      /* 统一按钮高度 */
            padding: 0 16px;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 5px;
            white-space: nowrap;
        }
        
        .toolbar-container {
            margin-bottom: 20px;
            display: flex;
            flex-direction: column;
            gap: 10px;
        }
        
        .search-container {
            width: 100%;
        }
        
        .search-container input {
            width: 100%;
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            box-sizing: border-box;
        }
        
        .button-container {
            display: flex;
            gap: 10px;
            justify-content: flex-start;
        }
        
        .btn {
            height: 38px;
            padding: 0 16px;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 5px;
            white-space: nowrap;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.2s;
        }
        
        .btn-primary {
            background-color: #007bff;
            color: white;
            border: none;
        }
        
        .btn-primary:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <%
        try {
            // 检查用户是否登录
            String username = (String) session.getAttribute("username");
            Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
            
            if (username == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            // 打印调试信息
            System.out.println("Username: " + username);
            System.out.println("IsAdmin: " + isAdmin);
    %>
    
    <div class="layout">
        <!-- 左侧导航 -->
        <div class="sidebar">
            <div class="system-title">
                <span class="logo">🎮</span>
                <h1>云城游戏管理系统</h1>
            </div>

            <div class="menu-container">
                <!-- 菜单部分 -->
                <ul class="menu">
                    <!-- 添加管理员列表菜单项 -->
                    <li class="menu-item">
                        <div class="menu-item-wrapper" onclick="window.showContent('adminList', event)">
                            <div class="menu-item-content">
                                <span class="menu-icon">👑</span>
                                <span>管理员列表</span>
                            </div>
                        </div>
                    </li>
                    
                    <li class="menu-item">
                        <div class="menu-item-wrapper" onclick="toggleSubmenu('systemManage')">
                            <div class="menu-item-content">
                                <span class="menu-icon">⚙️</span>
                                <span>系统管理</span>
                            </div>
                            <span class="dropdown-icon">▼</span>
                        </div>
                        <ul class="submenu" id="systemManage">
                            <li class="submenu-item" onclick="window.showContent('userList', event)">
                                <span class="menu-icon">👥</span>
                                <span>用户管理</span>
                            </li>
                            <li class="submenu-item" onclick="window.showContent('logList', event)">
                                <span class="menu-icon">📋</span>
                                <span>日志查询</span>
                            </li>
                        </ul>
                    </li>
                    <li class="menu-item">
                        <div class="menu-item-wrapper" onclick="toggleSubmenu('gameManage')">
                            <div class="menu-item-content">
                                <span class="menu-icon">🎮</span>
                                <span>游戏管理</span>
                            </div>
                            <span class="dropdown-icon">▼</span>
                        </div>
                        <ul class="submenu" id="gameManage">
                            <li class="submenu-item" onclick="window.showContent('contentManage', event)">
                                <span class="menu-icon">📝</span>
                                <span>内容管理</span>
                            </li>
                        </ul>
                    </li>
                    <li class="menu-item">
                        <div class="menu-item-wrapper" onclick="location.href='logout.jsp'">
                            <div class="menu-item-content">
                                <span class="menu-icon">🚪</span>
                                <span>退出登录</span>
                            </div>
                        </div>
                    </li>
                    <li class="menu-item">
                        <div class="menu-item-wrapper" onclick="location.href='index.jsp'">
                            <div class="menu-item-content">
                                <span class="menu-icon">🏠</span>
                                <span>返回网站</span>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>

            <!-- 用户信息移到底部 -->
            <div class="sidebar-header">
                <div class="user-info">
                    <span class="user-icon">
                        <%= isAdmin != null && isAdmin ? "👑" : "&#x1F464;" %>
                    </span>
                    <span>欢迎您，<%= isAdmin != null && isAdmin ? "管理员 " : "用户 " %><%= username %></span>
                </div>
                <div class="last-login">
                    <% 
                        java.sql.Timestamp lastLoginTime = (java.sql.Timestamp)session.getAttribute("lastLoginTime");
                        if (lastLoginTime != null) {
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            out.println("最后登录：" + sdf.format(lastLoginTime));
                        }
                    %>
                </div>
            </div>
        </div>
        
        <!-- 右侧内容区域 -->
        <div class="content">
            <div class="content-header">
                <h2 id="contentTitle">云城游戏管理系统</h2>
            </div>
            <div class="content-body" id="contentBody">
                <!-- 初始内容 -->
                <h3>欢迎使用云城游戏管理系统</h3>
            </div>
        </div>
    </div>

    <!-- 添加弹窗 HTML 结构 -->
    <div id="userModal" class="modal">
        <div class="modal-content">
            <span class="close-btn" onclick="closeModal()">&times;</span>
            <div id="modalContent"></div>
        </div>
    </div>

    <!-- 修改脚本引入顺序 -->
    <script src="js/welcome.js"></script>
    <script src="js/userManage.js"></script>
    <script src="js/adminManage.js"></script>
    <script src="js/gameManage.js"></script>
    <script src="js/logManage.js"></script>
    
    <%
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in welcome.jsp: " + e.getMessage());
        }
    %>

    <!-- 页脚
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
    </footer> -->
</body>
</html>
