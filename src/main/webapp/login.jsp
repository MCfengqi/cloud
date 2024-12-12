<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>登录 - CloudCity</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        .container {
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
        }
        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
            font-size: 24px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-size: 14px;
        }
        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 14px;
            transition: border-color 0.3s;
        }
        input[type="text"]:focus,
        input[type="password"]:focus {
            border-color: #4CAF50;
            outline: none;
        }
        button {
            width: 100%;
            padding: 12px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s;
        }
        button:hover {
            background-color: #0056b3;
        }
        .message {
            text-align: center;
            margin-bottom: 20px;
            padding: 10px;
            border-radius: 4px;
            font-size: 14px;
        }
        .success {
            color: #4CAF50;
            background-color: #e8f5e9;
            border: 1px solid #c8e6c9;
        }
        .error {
            color: #f44336;
            background-color: #ffebee;
            border: 1px solid #ffcdd2;
        }
        .links {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
        }
        .links a {
            color: #4CAF50;
            text-decoration: none;
            transition: color 0.3s;
        }
        .links a:hover {
            color: #45a049;
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>用户登录</h2>
        
        <% if(request.getAttribute("msg") != null) { 
            String msg = (String)request.getAttribute("msg");
            String messageClass = msg.contains("成功") ? "success" : "error";
        %>
            <div class="message <%= messageClass %>">
                <%= msg %>
            </div>
        <% } %>
        
        <form action="UserLoginServlet" method="post">
            <div class="form-group">
                <label for="username">用户名</label>
                <input type="text" id="username" name="username" required placeholder="请输入用户名">
            </div>
            <div class="form-group">
                <label for="password">密码</label>
                <input type="password" id="password" name="password" required placeholder="请输入密码">
            </div>
            <div class="form-group">
                <button type="submit">登录</button>
            </div>
        </form>
        
        <div class="links">
            <p>还没有账号？<a href="register.jsp">立即注册</a></p>
        </div>
    </div>
</body>
</html>
