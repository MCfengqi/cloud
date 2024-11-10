<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page errorPage="error.jsp" %>
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
        /* 内容包装器 */
        .content-wrapper {
            background: white;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        /* 工具栏样式 */
        .toolbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            gap: 10px;
        }

        /* 搜索框容器 */
        .search-box {
            display: flex;
            gap: 10px;
            flex: 1;
        }

        /* 搜索输入框 */
        .search-input {
            flex: 1;
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            min-width: 200px;
        }

        /* 表格容器 */
        .table-container {
            overflow-x: auto;
        }

        /* 表格样式优化 */
        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }

        .data-table th,
        .data-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }

        .data-table th {
            background-color: #f8f9fa;
            font-weight: 600;
            color: #333;
        }

        .data-table tr:hover {
            background-color: #f5f5f5;
        }

        /* 按钮组样式 */
        .btn-group {
            display: flex;
            gap: 5px;
        }

        /* 按钮样式优化 */
        .btn {
            display: inline-flex;
            align-items: center;
            gap: 5px;
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            transition: all 0.3s ease;
        }

        .btn-primary {
            background-color: #007bff;
            color: white;
        }

        .btn-success {
            background-color: #28a745;
            color: white;
        }

        .btn-warning {
            background-color: #ffc107;
            color: #000;
        }

        .btn-danger {
            background-color: #dc3545;
            color: white;
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
            try {
                const contentTitle = document.getElementById('contentTitle');
                const contentBody = document.getElementById('contentBody');
                
                if (!contentTitle || !contentBody) {
                    console.error('Required elements not found');
                    return;
                }
                
                switch(type) {
                    case 'userList':
                        contentTitle.textContent = '用户管理';
                        contentBody.innerHTML = `
                            <div class="content-wrapper">
                                <div class="toolbar">
                                    <div class="search-box">
                                        <input type="text" id="userSearchInput" class="search-input" placeholder="输入用户名搜索">
                                        <button class="btn btn-primary" onclick="searchUsers()">
                                            <span class="menu-icon">🔍</span>搜索
                                        </button>
                                    </div>
                                    <button class="btn btn-success" onclick="showAddUserForm()">
                                        <span class="menu-icon">➕</span>添加用户
                                    </button>
                                </div>
                                <div class="table-container">
                                    <table class="data-table" id="userTable">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>用户名</th>
                                                <th>密码</th>
                                                <th>邮箱</th>
                                                <th>手机号</th>
                                                <th>用户类型</th>
                                                <th>操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        `;
                        loadUserList();
                        break;
                    case 'adminList':
                        contentTitle.textContent = '管理员列表';
                        // ... 其他管理员列表相关代码 ...
                        break;
                    default:
                        contentTitle.textContent = '系统管理';
                        contentBody.innerHTML = '<h3>欢迎使用 CloudCity 系统</h3>';
                }
            } catch (error) {
                console.error('Error in showContent:', error);
            }
        }

        // 页面加载完成后初始化
        document.addEventListener('DOMContentLoaded', function() {
            try {
                showContent('dashboard');
            } catch (error) {
                console.error('Error during initialization:', error);
            }
        });

        // 添加用户列表相关函数
        function loadUserList() {
            try {
                const searchTerm = document.getElementById('userSearchInput')?.value || '';
                fetch('UserManageServlet?action=list&search=' + encodeURI(searchTerm))
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(users => {
                        const tbody = document.querySelector('#userTable tbody');
                        if (!tbody) {
                            console.error('Table body not found');
                            return;
                        }
                        tbody.innerHTML = users.map(user => `
                            <tr>
                                <td>${user.id}</td>
                                <td>${user.username}</td>
                                <td>${user.password}</td>
                                <td>${user.email}</td>
                                <td>${user.mobile}</td>
                                <td>${user.isAdmin ? '管理员' : '普通用户'}</td>
                                <td>
                                    <div class="btn-group">
                                        <button class="btn btn-primary" onclick="viewUser(${user.id})">
                                            <span class="menu-icon">👁️</span>查看
                                        </button>
                                        <button class="btn btn-warning" onclick="editUser(${user.id})">
                                            <span class="menu-icon">✏️</span>编辑
                                        </button>
                                        <button class="btn btn-danger" onclick="deleteUser(${user.id})">
                                            <span class="menu-icon">🗑️</span>删除
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        `).join('');
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('加载用户列表失败，请刷新页面重试');
                    });
            } catch (error) {
                console.error('Error in loadUserList:', error);
            }
        }

        function searchUsers() {
            loadUserList();
        }

        function showAddUserForm() {
            const contentBody = document.getElementById('contentBody');
            contentBody.innerHTML = `
                <div class="content-wrapper">
                    <h3>添加新用户</h3>
                    <form id="addUserForm" onsubmit="addUser(event)">
                        <div class="form-group">
                            <label>用户名</label>
                            <input type="text" name="username" required class="form-control" placeholder="请输入用户名">
                        </div>
                        <div class="form-group">
                            <label>密码</label>
                            <input type="password" name="password" required class="form-control" placeholder="请输入密码">
                        </div>
                        <div class="form-group">
                            <label>邮箱</label>
                            <input type="email" name="email" required class="form-control" placeholder="请输入邮箱">
                        </div>
                        <div class="form-group">
                            <label>手机号</label>
                            <input type="text" name="mobile" required class="form-control" pattern="^1[3-9]\\d{9}$" placeholder="请输入手机号">
                        </div>
                        <div class="form-group">
                            <label>
                                <input type="checkbox" name="isAdmin" value="true"> 是否是管理员
                            </label>
                        </div>
                        <div class="button-group">
                            <button type="submit" class="btn btn-primary">添加</button>
                            <button type="button" class="btn btn-secondary" onclick="showContent('userList')">取消</button>
                        </div>
                    </form>
                </div>
            `;
        }

        function deleteUser(id) {
            if (confirm('确定要删除这个用户吗？')) {
                fetch('UserManageServlet?action=delete', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `action=delete&id=${id}`
                })
                .then(response => response.json())
                .then(result => {
                    if (result.success) {
                        alert('删除成功！');
                        loadUserList();
                    } else {
                        alert('删除失败！');
                    }
                })
                .catch(error => console.error('Error:', error));
            }
        }

        function viewUser(id) {
            fetch('UserManageServlet?action=get&id=' + id)
                .then(response => response.json())
                .then(user => {
                    contentBody.innerHTML = `
                        <div class="content-wrapper">
                            <h3>用户详情</h3>
                            <div class="detail-group">
                                <label>ID</label>
                                <div class="detail-value">${user.id}</div>
                            </div>
                            <div class="detail-group">
                                <label>用户名</label>
                                <div class="detail-value">${user.username}</div>
                            </div>
                            <div class="detail-group">
                                <label>密码</label>
                                <div class="detail-value">${user.password}</div>
                            </div>
                            <div class="detail-group">
                                <label>邮箱</label>
                                <div class="detail-value">${user.email}</div>
                            </div>
                            <div class="detail-group">
                                <label>手机号</label>
                                <div class="detail-value">${user.mobile}</div>
                            </div>
                            <div class="detail-group">
                                <label>用户类型</label>
                                <div class="detail-value">${user.isAdmin ? '管理员' : '普通用户'}</div>
                            </div>
                            <div class="button-group">
                                <button class="btn btn-primary" onclick="editUser(${user.id})">编辑</button>
                                <button class="btn btn-secondary" onclick="showContent('userList')">返回</button>
                            </div>
                        </div>
                    `;
                })
                .catch(error => console.error('Error:', error));
        }

        // 添加编辑用户的函数
        function editUser(id) {
            fetch(`UserManageServlet?action=get&id=${id}`)
                .then(response => response.json())
                .then(user => {
                    const contentBody = document.getElementById('contentBody');
                    contentBody.innerHTML = `
                        <div class="content-wrapper">
                            <h3>编辑用户</h3>
                            <form id="editUserForm" onsubmit="updateUser(event, ${user.id})">
                                <div class="form-group">
                                    <label>用户名</label>
                                    <input type="text" value="${user.username}" disabled class="form-control">
                                </div>
                                <div class="form-group">
                                    <label>新密码（不修改请留空）</label>
                                    <input type="password" name="password" class="form-control" placeholder="输入新密码">
                                </div>
                                <div class="form-group">
                                    <label>邮箱</label>
                                    <input type="email" name="email" value="${user.email}" required class="form-control">
                                </div>
                                <div class="form-group">
                                    <label>手机号</label>
                                    <input type="text" name="mobile" value="${user.mobile}" required class="form-control" pattern="^1[3-9]\\d{9}$">
                                </div>
                                <div class="form-group">
                                    <label>
                                        <input type="checkbox" name="isAdmin" ${user.isAdmin ? 'checked' : ''} value="true"> 是否是管理员
                                    </label>
                                </div>
                                <div class="button-group">
                                    <button type="submit" class="btn btn-primary">保存</button>
                                    <button type="button" class="btn btn-secondary" onclick="showContent('userList')">取消</button>
                                </div>
                            </form>
                        </div>
                    `;
                })
                .catch(error => console.error('Error:', error));
        }

        // 添加更新用户的函数
        function updateUser(event, id) {
            event.preventDefault();
            const form = event.target;
            const formData = new FormData(form);
            formData.append('id', id);
            formData.append('action', 'update');

            fetch('UserManageServlet', {
                method: 'POST',
                body: new URLSearchParams(formData)
            })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    alert('更新成功！');
                    showContent('userList');
                } else {
                    alert(result.error || '更新失败！');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('系统错误，请稍后重试！');
            });
        }

        // 添加新用户的函数
        function addUser(event) {
            event.preventDefault();
            const form = event.target;
            const formData = new FormData(form);
            formData.append('action', 'add');

            fetch('UserManageServlet', {
                method: 'POST',
                body: new URLSearchParams(formData)
            })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    alert('添加用户成功！');
                    showContent('userList');
                } else {
                    alert(result.error || '添加用户失败！');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('系统错误，请稍后重试！');
            });
        }
    </script>
    
    <%
        } catch (Exception e) {
            e.printStackTrace();
            // 记录错误信息
            System.err.println("Error in welcome.jsp: " + e.getMessage());
            // 可以选择重定向到错误页面
            // response.sendRedirect("error.jsp");
        }
    %>
</body>
</html>
