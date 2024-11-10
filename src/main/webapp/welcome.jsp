<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page errorPage="error.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>欢迎 - CloudCity</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入外部CSS文件 -->
    <link rel="stylesheet" href="css/welcome.css">
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
                    <div class="menu-item-wrapper" onclick="toggleSubmenu('systemManage')">
                        <div class="menu-item-content">
                            <span class="menu-icon">⚙️</span>
                            <span>系统管理</span>
                        </div>
                        <span class="dropdown-icon">▼</span>
                    </div>
                    <ul class="submenu" id="systemManage">
                        <li class="submenu-item" onclick="showContent('userList', event)">
                            <span class="menu-icon">&#x1F465;</span>用户管理
                        </li>
                        <% if (isAdmin != null && isAdmin) { %>
                            <li class="submenu-item" onclick="showContent('adminList', event)">
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
                <h2 id="contentTitle">系统管理</h2>
            </div>
            <div class="content-body" id="contentBody">
                <!-- 初始内容 -->
                <h3>欢迎使用 CloudCity 系统</h3>
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

    <!-- 引入外部JavaScript文件 -->
    <script src="js/welcome.js"></script>
    <script src="js/userManage.js"></script>
    
    <%
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in welcome.jsp: " + e.getMessage());
        }
    %>
</body>
</html>
