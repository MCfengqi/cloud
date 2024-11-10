<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 清除所有会话数据
    session.invalidate();
    // 重定向到登录页面
    response.sendRedirect("login.jsp");
%>
