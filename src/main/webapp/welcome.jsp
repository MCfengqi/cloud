<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>欢迎 - CloudCity</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            min-height: 100vh;
        }
        .layout {
            display: flex;
            min-height: 100vh;
        }
        /* 左侧导航样式 */
        .sidebar {
            width: 250px;
            background-color: #2c3e50;
            color: white;
            padding: 20px 0;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }
        .sidebar-header {
            padding: 20px;
            border-top: 1px solid #34495e;
            margin-top: auto;
        }
        .user-info {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
            font-size: 14px;
        }
        .user-icon {
            margin-right: 10px;
            font-size: 20px;
            color: white;
        }
        .menu {
            list-style: none;
            padding: 20px 0;
            padding-left: 0;
        }
        .menu-title {
            font-size: 16px;
            padding: 15px 20px;
            color: white;
            border-bottom: 1px solid #34495e;
            margin-bottom: 10px;
        }
        .menu-item {
            padding: 12px 15px;
            cursor: pointer;
            transition: all 0.3s;
            font-size: 14px;
        }
        .menu-item > div {
            display: flex;
            align-items: center;
        }
        .menu-item:hover {
            background-color: #34495e;
        }
        .menu-item.active {
            background-color: #3498db;
        }
        .submenu {
            list-style: none;
            padding-left: 20px;
            background-color: #34495e;
            display: none;
        }
        .submenu.show {
            display: block;
        }
        .submenu-item {
            padding: 10px 15px;
            cursor: pointer;
            color: #bdc3c7;
            transition: all 0.3s;
        }
        .submenu-item:hover {
            color: white;
            background-color: #2c3e50;
        }
        /* 右侧内容区域样式 */
        .content {
            flex: 1;
            padding: 20px;
            background-color: white;
        }
        .content-header {
            padding: 20px;
            background-color: white;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .content-body {
            padding: 20px;
            background-color: white;
            border-radius: 4px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        /* 表格样式 */
        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .data-table th, .data-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }
        .data-table th {
            background-color: #f8f9fa;
            font-weight: bold;
        }
        /* 按钮样式 */
        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: all 0.3s;
            margin-right: 8px;
        }
        .btn-primary { background-color: #007bff; color: white; }
        .btn-primary:hover { background-color: #0056b3; }
        .btn-success { background-color: #2ecc71; color: white; }
        .btn-danger { background-color: #e74c3c; color: white; }
        .btn:hover { opacity: 0.9; }
        .form-container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }
        .form-control {
            width: 100%;
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            margin-top: 5px;
            font-size: 14px;
        }
        .form-control:focus {
            border-color: #3498db;
            outline: none;
            box-shadow: 0 0 5px rgba(52, 152, 219, 0.3);
        }
        .button-group {
            margin-top: 20px;
            display: flex;
            gap: 10px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #333;
            font-weight: 500;
        }
        /* 添加图标样式 */
        .menu-icon {
            margin-right: 8px;
            font-size: 16px;
            color: white;
        }
        /* 添加搜索框样式 */
        .search-box {
            margin-bottom: 20px;
            display: flex;
            gap: 10px;
        }
        .search-input {
            flex: 1;
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }
        .search-input:focus {
            border-color: #007bff;
            outline: none;
        }
        .detail-group {
            margin-bottom: 15px;
        }
        .detail-group label {
            display: block;
            color: #666;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .detail-value {
            padding: 8px;
            background-color: #f8f9fa;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
    </style>
</head>
<body>
    <%
        // 检查用户是否登录
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        
        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>
    
    <div class="layout">
        <!-- 左侧导航 -->
        <div class="sidebar">
            <!-- 菜单部分 -->
            <ul class="menu">
                <li class="menu-title">系统首页</li>
                <li class="menu-item">
                    <div onclick="toggleSubmenu('systemManage')">
                        <span class="menu-icon">⚙️</span>系统管理
                    </div>
                    <ul class="submenu" id="systemManage">
                        <li class="submenu-item" onclick="showContent('userList')">
                            <span class="menu-icon">&#x1F465;</span>用户管理
                        </li>
                        <% if (isAdmin != null && isAdmin) { %>
                            <li class="submenu-item" onclick="showContent('adminList')">
                                <span class="menu-icon">&#x1F464;</span>管理员管理
                            </li>
                        <% } %>
                    </ul>
                </li>
                <li class="menu-item" onclick="location.href='logout.jsp'">
                    <span class="menu-icon">&#x1F6AA;</span>退出登录
                </li>
            </ul>

            <!-- 用户信息移到底部 -->
            <div class="sidebar-header">
                <div class="user-info">
                    <span class="user-icon">
                        <%= isAdmin != null && isAdmin ? "👑" : "&#x1F464;" %>
                    </span>
                    <span>欢迎您，<%= isAdmin != null && isAdmin ? "管理员 " : "用户 " %><%= username %></span>
                </div>
            </div>
        </div>
        
        <!-- 右侧内容区域 -->
        <div class="content">
            <div class="content-header">
                <h2 id="contentTitle">系统管理</h2>
            </div>
            <div class="content-body" id="contentBody">
                <!-- 初始内容 -->
                <h3>欢迎使用 CloudCity 系统</h3>
            </div>
        </div>
    </div>

    <!-- 将所有 JavaScript 代码移到页面底部 -->
    <script>
        // 定义 toggleSubmenu 函数
        function toggleSubmenu(id) {
            const submenu = document.getElementById(id);
            if (submenu) {
                submenu.style.display = submenu.style.display === 'none' || submenu.style.display === '' ? 'block' : 'none';
            }
        }

        // 定义 showContent 函数
        function showContent(type) {
            const contentTitle = document.getElementById('contentTitle');
            const contentBody = document.getElementById('contentBody');
            
            switch(type) {
                case 'userList':
                    contentTitle.textContent = '用户列表';
                    // ... 其他用户列表相关代码 ...
                    break;
                case 'adminList':
                    contentTitle.textContent = '管理员列表';
                    // ... 其他管理员列表相关代码 ...
                    break;
                default:
                    contentTitle.textContent = '系统管理';
                    contentBody.innerHTML = '<h3>欢迎使用 CloudCity 系统</h3>';
            }
        }

        // 页面加载完成后初始化
        document.addEventListener('DOMContentLoaded', function() {
            // 显示默认内容
            showContent('dashboard');
        });
    </script>
</body>
</html>
