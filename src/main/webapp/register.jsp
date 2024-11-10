<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>注册</title>
    <style>
        /* 全局样式 */
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        /* 容器样式 */
        .container {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 300px;
            position: relative;
        }

        /* 表单组样式 */
        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
        }

        .form-group input {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .form-group button {
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

        .form-group button:hover {
            background-color: #0056b3;
        }

        /* 错误消息样式 */
        .message {
            color: red;
            margin-bottom: 15px;
        }

        .error {
            color: red;
            font-size: 12px;
            margin-top: 5px;
        }

        /* 链接样式 */
        .link {
            text-align: center;
            margin-top: 15px;
        }

        /* 页脚样式 */
        .footer {
            position: absolute;
            bottom: 10px;
            right: 10px;
            font-size: 12px;
            color: #999;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>注册</h2>
    <div class="message">${msg}</div>
    <form action="UserRegisterServlet" method="post" onsubmit="return validateForm()">
        <div class="form-group">
            <label for="registerUsername">用户名</label>
            <input type="text" id="registerUsername" name="username" required>
        </div>
        <div class="form-group">
            <label for="registerPassword">密码</label>
            <input type="password" id="registerPassword" name="password" required>
        </div>
        <div class="form-group">
            <label for="registerEmail">邮箱</label>
            <input type="email" id="registerEmail" name="email" required pattern="^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,7}$">
            <div class="error" id="emailError"></div>
        </div>
        <div class="form-group">
            <label for="registerMobile">电话</label>
            <input type="tel" id="registerMobile" name="mobile" required pattern="^\d{11}$">
            <div class="error" id="mobileError"></div>
        </div>
        <div class="form-group">
            <button type="submit">注册</button>
        </div>
    </form>
    <div class="link">
        <a href="login.jsp">已有账号？去登录</a>
    </div>
    <div class="footer">
        By MC风启
    </div>
</div>
<script>
    // 表单验证函数
    // function validateForm() {
    //     const email = document.getElementById('registerEmail').value;
    //     const mobile = document.getElementById('registerMobile').value;
    //
    //     const emailRegex = /^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,7}$/;
    //     const mobileRegex = /^\d{11}$/;
    //
    //     let isValid = true;
    //
    //     // 验证邮箱
    //     if (!emailRegex.test(email)) {
    //         document.getElementById('emailError').innerText = '无效的邮箱地址';
    //         isValid = false;
    //     } else {
    //         document.getElementById('emailError').innerText = '';
    //     }
    //
    //     // 验证手机号
    //     if (!mobileRegex.test(mobile)) {
    //         document.getElementById('mobileError').innerText = '无效的手机号';
    //         isValid = false;
    //     } else {
    //         document.getElementById('mobileError').innerText = '';
    //     }
    //
    //     return isValid;
    // }
</script>
</body>
</html>
