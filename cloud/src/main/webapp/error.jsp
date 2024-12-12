<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>错误 - CloudCity</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f5f5f5;
        }
        .error-container {
            text-align: center;
            padding: 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .error-message {
            color: #dc3545;
            margin: 20px 0;
        }
        .back-link {
            color: #007bff;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>系统错误</h1>
        <p class="error-message">抱歉，系统发生了错误。</p>
        <a href="index.jsp" class="back-link">返回首页</a>
    </div>
</body>
</html> 