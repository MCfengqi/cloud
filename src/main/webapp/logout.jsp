<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.cloudcity.utils.LogUtils" %>
<%
    // 在用户退出前记录日志
    String username = (String) session.getAttribute("username");
    if(username != null) {
        LogUtils.logOperation(
            "用户退出",
            "用户 " + username + " 退出系统",
            username,
            request,
            "成功"
        );
    }
    // 清除所有会话数据
    session.invalidate();
    // 重定向到登录页面
    response.sendRedirect("login.jsp");
%>
