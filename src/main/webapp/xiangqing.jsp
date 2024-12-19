<%@ page import="java.math.BigDecimal" %><%--
  Created by IntelliJ IDEA.
  User: 林龙
  Date: 2024/12/16
  Time: 上午8:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>游戏详情</title>
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
            min-height: 100vh;
            display: flex;
            flex-direction: column;
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


        .main-content {
            margin-top: 80px;
            padding: 20px;
            padding-bottom: 30px;
            max-width: 1200px;
            margin-left: auto;
            margin-right: auto;
            margin-bottom: 80px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            display: flex;
            min-height: 400px;
            max-height: calc(100vh - 200px);
        }

        .image-container {
            flex: 1;
            padding: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .details-container {
            flex: 1;
            padding: 20px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }

        .game-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .game-description {
            color: #666;
            font-size: 16px;
            margin-bottom: 15px;
        }

        .game-image {
            width: 100%;
            height: auto;
            max-height: 350px;
            object-fit: contain;
            border-radius: 8px;
            margin-bottom: 15px;
        }

        .game-link {
            display: inline-block;
            padding: 10px 15px;
            background-color: #007bff;
            color: #fff;
            text-decoration: none;
            border-radius: 4px;
            transition: background-color 0.3s;
        }

        .game-link:hover {
            background-color: #0056b3;
        }

        .cart-button {
            display: inline-block;
            padding: 10px 15px;
            background-color: #28a745;
            color: #fff;
            text-decoration: none;
            border-radius: 4px;
            transition: background-color 0.3s;
            margin-left: 10px;
            border: none;
            cursor: pointer;
        }

        .cart-button:hover {
            background-color: #218838;
        }

        .cart-button2 {
            display: inline-block;
            padding: 10px 15px;
            background-color: #17a2b8;
            color: #fff;
            text-decoration: none;
            border-radius: 4px;
            transition: background-color 0.3s;
            margin-left: 10px;
            border: none;
            cursor: pointer;
        }

        .cart-button2:hover {
            background-color: #138496;
        }

        .cart-modal {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 100%;
            max-width: 800px;
            background-color: white;
            box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease-in-out;
            z-index: 1000;
            border-radius: 8px;
            opacity: 0;
        }

        .cart-modal.show {
            opacity: 1;
        }

        .cart-modal-content {
            padding: 20px;
            max-height: 80vh;
            overflow-y: auto;
            margin: 0 auto;
        }

        .cart-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 2px solid #eee;
        }

        .cart-close {
            font-size: 24px;
            cursor: pointer;
            color: #666;
            width: 30px;
            height: 30px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            transition: background-color 0.3s;
        }

        .cart-close:hover {
            background-color: #f0f0f0;
        }

        .cart-items {
            max-height: 300px;
            overflow-y: auto;
        }

        .cart-item {
            display: flex;
            align-items: center;
            padding: 15px;
            border-bottom: 1px solid #eee;
            background: #fff;
            transition: background-color 0.3s;
        }

        .cart-item:hover {
            background-color: #f8f9fa;
        }

        .cart-item img {
            width: 80px;
            height: 80px;
            object-fit: cover;
            margin-right: 15px;
            border-radius: 4px;
        }

        .cart-item-details {
            flex-grow: 1;
        }

        .cart-item-remove {
            color: #dc3545;
            cursor: pointer;
            padding: 5px 10px;
        }

        /* 页脚样式 */
        .footer {
            background-color: #333;
            color: #fff;
            padding: 30px 0;
            position: fixed;
            bottom: 0;
            width: 100%;
            z-index: 100;
        }

        .footer-content {
            max-width: 1200px;
            margin: 0 auto;
            text-align: center;
        }

        /* 模态框样式 */
        .modal {
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0, 0, 0, 0.7);
            transition: opacity 0.3s ease;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .modal-content {
            background-color: #fff;
            padding: 20px;
            border: 1px solid #888;
            width: 80%;
            max-width: 600px;
            border-radius: 8px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
            animation: slideIn 0.3s;
        }

        @keyframes slideIn {
            from {
                transform: translateY(-30px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }

        .cart-buttons {
            display: flex;
            gap: 10px;
            margin-top: 20px;
            align-items: center;
            flex-wrap: wrap;
        }

        .cart-buttons button {
            flex: 1;
            min-width: 150px;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 5px;
        }

        .cart-total {
            margin-top: 20px;
            padding: 15px;
            border-top: 2px solid #eee;
            text-align: right;
        }

        .cart-empty {
            padding: 20px;
            text-align: center;
            color: #666;
        }

        .price {
            color: #ff4d4f;
            font-weight: bold;
            margin-top: 5px;
            background: rgba(255, 77, 79, 0.1);
            padding: 5px 10px;
            border-radius: 4px;
            display: inline-block;
        }

        .game-price {
            font-size: 24px;
            color: #ff4d4f;
            font-weight: bold;
            margin: 15px 0;
            display: flex;
            align-items: center;
            background: rgba(255, 77, 79, 0.1);
            padding: 10px 15px;
            border-radius: 8px;
            position: relative;
            overflow: hidden;
        }

        .game-price::before {
            content: '￥';
            font-size: 18px;
            margin-right: 2px;
            color: #ff4d4f;
        }

        .game-price-label {
            font-size: 14px;
            color: #ff7875;
            margin-right: 10px;
            font-weight: normal;
        }

        .game-price::after {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(45deg, rgba(255, 77, 79, 0.05), rgba(255, 77, 79, 0.1));
            z-index: -1;
        }

        .toast-message {
            position: fixed;
            bottom: 20px;
            left: 50%;
            transform: translateX(-50%);
            background-color: rgba(40, 167, 69, 0.9);
            color: white;
            padding: 12px 24px;
            border-radius: 4px;
            z-index: 1000;
            animation: fadeInOut 2s ease-in-out;
        }

        @keyframes fadeInOut {
            0% { opacity: 0; transform: translate(-50%, 20px); }
            15% { opacity: 1; transform: translate(-50%, 0); }
            85% { opacity: 1; transform: translate(-50%, 0); }
            100% { opacity: 0; transform: translate(-50%, -20px); }
        }

        .checkout-button {
            background-color: #ff4d4f;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 10px;
            transition: background-color 0.3s;
        }

        .checkout-button:hover {
            background-color: #ff7875;
        }

        .payment-modal {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            z-index: 1100;
            width: 90%;
            max-width: 400px;
            opacity: 0;
            transition: opacity 0.3s ease;
        }

        .payment-modal.show {
            opacity: 1;
        }

        .payment-options {
            display: flex;
            gap: 15px;
            margin: 20px 0;
        }

        .payment-option {
            flex: 1;
            padding: 15px;
            border: 2px solid #eee;
            border-radius: 8px;
            cursor: pointer;
            text-align: center;
            transition: all 0.3s;
        }

        .payment-option:hover {
            border-color: #1890ff;
            background: rgba(24, 144, 255, 0.1);
        }

        .payment-option.selected {
            border-color: #1890ff;
            background: rgba(24, 144, 255, 0.1);
        }

        .payment-option img {
            width: 40px;
            height: 40px;
            margin-bottom: 8px;
        }

        .payment-confirm-btn {
            width: 100%;
            padding: 12px;
            background: #1890ff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            transition: background 0.3s;
        }

        .payment-confirm-btn:hover {
            background: #40a9ff;
        }

        .payment-confirm-btn:disabled {
            background: #ccc;
            cursor: not-allowed;
        }

        .qr-modal {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%) scale(0.8);
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            z-index: 1200;
            text-align: center;
            opacity: 0;
            transition: all 0.3s ease;
            min-width: 320px;
            max-width: 90%;
        }

        .qr-modal.show {
            opacity: 1;
            transform: translate(-50%, -50%) scale(1);
        }

        .qr-modal img {
            max-width: 200px;
            margin: 20px 0;
            border: 1px solid #eee;
            border-radius: 4px;
            padding: 10px;
        }

        .qr-modal p {
            color: #666;
            margin-top: 15px;
            font-size: 14px;
        }

        .modal-overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            z-index: 1050;
            opacity: 0;
            transition: opacity 0.3s ease;
        }

        .modal-overlay.show {
            opacity: 1;
        }

        .payment-modal .close-btn {
            position: absolute;
            top: 10px;
            right: 10px;
            width: 30px;
            height: 30px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            color: #666;
            cursor: pointer;
            border-radius: 50%;
            transition: all 0.3s;
            text-decoration: none;
            line-height: 1;
        }

        .payment-modal .close-btn:hover {
            background-color: #f0f0f0;
            color: #ff4d4f;
        }

        .payment-modal h2 {
            margin-bottom: 20px;
            padding-right: 30px;
        }

        .close-btn2 {
            position: absolute;
            top: 10px;
            right: 10px;
            width: 30px;
            height: 30px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            color: #666;
            cursor: pointer;
            border-radius: 50%;
            transition: all 0.3s;
            text-decoration: none;
            line-height: 1;
            background: transparent;
            border: none;
            padding: 0;
            z-index: 1;
        }

        .close-btn2:hover {
            background-color: #f0f0f0;
            color: #ff4d4f;
        }

        .qr-modal h2 {
            margin-bottom: 20px;
            padding-right: 30px;
        }

        .complete-payment-btn {
            background: #52c41a;
            color: white;
            border: none;
            padding: 12px 24px;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 20px;
            font-size: 16px;
            transition: all 0.3s;
        }

        .complete-payment-btn:hover {
            background: #73d13d;
            transform: translateY(-2px);
            box-shadow: 0 2px 8px rgba(82, 196, 26, 0.2);
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
    <%
        // 获取传递的参数
        String gameId = request.getParameter("gameId");
        String gameName = request.getParameter("gameName");
        String gameDesc = request.getParameter("gameDesc");
        String gameImg = request.getParameter("gameImg");
        String gameLinks = request.getParameter("gameLink");
        String gameMoney = request.getParameter("gameMoney");

    %>
    <div class="image-container">
        <img src="<%= gameImg %>" alt="<%= gameName %>" class="game-image"/>
    </div>
    <div class="details-container">
        <h1 class="game-title">游戏详情</h1>
        <p class="game-description">游戏名称：<%= gameName %></p>
        <p class="game-description2">游戏描述：<%= gameDesc %></p>
        <div class="game-price">
            <span class="game-price-label">游戏价格</span>
            <%= gameMoney %>
        </div>
        <a href="<%= gameImg %>" class="game-link" target="_blank">游戏链接</a>

        <div class="cart-buttons">
            <button class="cart-button" onclick="addToCart('<%= gameId %>')">
                🛒 加入购物车
            </button>
            <button class="cart-button2">
                🛒 查看购物车
            </button>
        </div>
    </div>
</main>

<!-- 购物车弹窗 -->
<div class="cart-modal" id="cartModal">
    <div class="cart-modal-content">
        <div class="cart-header">
            <h2>购物车</h2>
            <span class="cart-close" onclick="closeCart()">&times;</span>
        </div>
        <div class="cart-items" id="cartItems">
            <!-- 购物车商品将在这里动态显示 -->
        </div>
    </div>
</div>

<!-- 支付方式选择弹窗 -->
<div class="modal-overlay" id="modalOverlay"></div>
<div class="payment-modal" id="paymentModal">
    <a href="javascript:void(0)" class="close-btn" onclick="hidePaymentModal()" title="关闭">×</a>
    <h2>请选择支付方式</h2>
    <div class="payment-options">
        <div class="payment-option" data-payment="wechat">
            <img src="images/img_2.png" alt="微信支付">
            <div>微信支付</div>
        </div>
        <div class="payment-option" data-payment="alipay">
            <img src="images/img_1.png" alt="支付宝">
            <div>支付宝</div>
        </div>
        <div class="payment-option" data-payment="qq">
            <img src="images/img.png" alt="QQ支付">
            <div>QQ支付</div>
        </div>
    </div>
    <button class="payment-confirm-btn" id="confirmPayment" disabled>确认支付</button>
</div>

<!-- 支付二维码弹窗 -->
<div class="qr-modal" id="qrModal">
    <button type="button" class="close-btn2" onclick="hideQRModal()" title="关闭">×</button>
    <h2>扫码支付</h2>
    <img id="qrCode" src="" alt="支付二维码">
    <p>请使用选择的支付方式完成支付</p>
    <button class="complete-payment-btn" onclick="completePayment()">完成支付</button>
</div>

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
<script>
    // 购物车组用于存储商品
    let cartItems = [];

    // 添加到购物车的函数
    function addToCart(gameId) {
        const gameName = "<%= gameName %>";
        const gameImg = "<%= gameImg %>";
        const gamePrice = "<%= gameMoney %>";

        // 检查商品是否已经在购物车中
        const existingItem = cartItems.find(item => item.id === gameId);
        if (existingItem) {
            alert('该游戏已经在购物车中！');
            return;
        }

        cartItems.push({
            id: gameId,
            name: gameName,
            image: gameImg,
            price: gamePrice
        });

        updateCartDisplay();

        // 显示添加成功的提示
        const toast = document.createElement('div');
        toast.className = 'toast-message';
        toast.textContent = '✅ 游戏已添加到购物车！';
        document.body.appendChild(toast);

        // 2秒后自动移除提示
        setTimeout(() => {
            toast.remove();
        }, 2000);
    }

    // 更新购物车显示
    function updateCartDisplay() {
        const cartItemsContainer = document.getElementById('cartItems');
        cartItemsContainer.innerHTML = '';

        let totalPrice = 0;

        cartItems.forEach((item, index) => {
            const price = typeof item.price === 'string' ?
                parseFloat(item.price.replace(/[^\d.]/g, '')) :
                parseFloat(item.price) || 0;
            totalPrice += price;

            cartItemsContainer.innerHTML += `
                <div class="cart-item">
                    <img src="<%=gameImg%>" alt="<%=gameName%>">
                    <div class="cart-item-details">
                        <h3><%=gameName%></h3>
                        <p class="price">￥<%=gameMoney%></p>
                    </div>
                    <span class="cart-item-remove" onclick="removeFromCart(${index})">删除</span>
                </div>
            `;
        });

        if (cartItems.length > 0) {
            cartItemsContainer.innerHTML += `
                <div class="cart-total">
                    <h3>总计：￥<%=gameMoney%></h3>
                    <button class="checkout-button" onclick="checkout()">
                        结算 (${cartItems.length}件商品)
                    </button>
                </div>
            `;
        } else {
            cartItemsContainer.innerHTML = '<div class="cart-empty">购物车是空的</div>';
        }
    }

    // 从购物车中移除商品
    function removeFromCart(index) {
        cartItems.splice(index, 1);
        updateCartDisplay();
    }

    // 显示购物车
    function showCart() {
        const cartModal = document.getElementById('cartModal');
        cartModal.style.display = 'block';
        document.body.style.overflow = 'hidden'; // 防止背景滚动
        setTimeout(() => {
            cartModal.classList.add('show');
        }, 10);
    }

    // 关闭购物车
    function closeCart() {
        const cartModal = document.getElementById('cartModal');
        cartModal.classList.remove('show');
        document.body.style.overflow = ''; // 恢复背景滚动
        setTimeout(() => {
            cartModal.style.display = 'none';
        }, 300);
    }

    // 添加结算功能
    function checkout() {
        if (cartItems.length === 0) {
            alert('购物车是空的！');
            return;
        }
        showPaymentModal();
    }

    // 显示支付方式选择弹窗
    function showPaymentModal() {
        closeCart();  // 先关闭购物车弹窗
        const overlay = document.getElementById('modalOverlay');
        const modal = document.getElementById('paymentModal');
        overlay.style.display = 'block';
        modal.style.display = 'block';
        document.body.style.overflow = 'hidden';

        // 触发重排以启用动画
        setTimeout(() => {
            overlay.classList.add('show');
            modal.classList.add('show');
        }, 10);
    }

    // 隐藏支付方式选择弹窗
    function hidePaymentModal() {
        const overlay = document.getElementById('modalOverlay');
        const modal = document.getElementById('paymentModal');
        overlay.classList.remove('show');
        modal.classList.remove('show');
        document.body.style.overflow = '';

        setTimeout(() => {
            overlay.style.display = 'none';
            modal.style.display = 'none';
        }, 300);
    }

    // 显示二维码弹窗
    function showQRModal(paymentType) {
        const qrUrls = {
            wechat: 'images/wx.jpg',
            alipay: 'images/zfb.jpg',
            qq: 'images/qq.jpg'
        };

        // 先设置二维码图片源，确保图片存在
        const qrImg = document.getElementById('qrCode');
        qrImg.src = qrUrls[paymentType];

        // 等待图片加载完成后再显示模态框
        qrImg.onload = function() {
            const qrModal = document.getElementById('qrModal');
            const overlay = document.getElementById('modalOverlay');

            overlay.style.display = 'block';
            qrModal.style.display = 'block';

            // 触发重排以启用动画
            setTimeout(() => {
                qrModal.classList.add('show');
                overlay.classList.add('show');
            }, 10);
        };

        // 处理图片加载失败的情况
        // qrImg.onerror = function() {
        //     alert('二维码图片加载失败，请重试');
        //     hidePaymentModal();
        // };
    }

    // 隐藏二维码弹窗
    function hideQRModal() {
        const qrModal = document.getElementById('qrModal');
        const overlay = document.getElementById('modalOverlay');

        qrModal.classList.remove('show');
        overlay.classList.remove('show');

        setTimeout(() => {
            qrModal.style.display = 'none';
            overlay.style.display = 'none';
            document.getElementById('qrCode').src = '';
        }, 300);

        // 重置支付选项
        document.querySelectorAll('.payment-option').forEach(opt => {
            opt.classList.remove('selected');
            opt.style.borderColor = '#eee';
            opt.style.backgroundColor = 'transparent';
        });
        document.getElementById('confirmPayment').disabled = true;
    }

    // 初始化支付选项的点击事件
    document.querySelectorAll('.payment-option').forEach(option => {
        option.addEventListener('click', function() {
            // 移除所有选项的选中状态
            document.querySelectorAll('.payment-option').forEach(opt => {
                opt.classList.remove('selected');
            });
            // 添加当前选项的选中状态
            this.classList.add('selected');
            // 启用确认按钮
            document.getElementById('confirmPayment').disabled = false;
            // 添加选中效果
            this.style.borderColor = '#1890ff';
            this.style.backgroundColor = 'rgba(24, 144, 255, 0.1)';
        });
    });

    // 确认支付按钮点击事件
    document.getElementById('confirmPayment').addEventListener('click', function() {
        const selectedOption = document.querySelector('.payment-option.selected');
        if (selectedOption) {
            const paymentType = selectedOption.dataset.payment;
            // 先隐藏支付选择模态框
            hidePaymentModal();
            // 短暂延迟后显示二维码
            setTimeout(() => {
                showQRModal(paymentType);
            }, 300);
        }
    });

    // 点击遮罩层关闭弹窗
    document.getElementById('modalOverlay').addEventListener('click', function(event) {
        // 只有当点击的是遮罩层本身时才关闭弹窗
        if (event.target === this) {
            const paymentModal = document.getElementById('paymentModal');
            const qrModal = document.getElementById('qrModal');

            if (paymentModal.style.display === 'block') {
                hidePaymentModal();
            } else if (qrModal.style.display === 'block') {
                hideQRModal();
            }
        }
    });

    // 添加点击外部关闭功能
    document.addEventListener('click', function(event) {
        const cartModal = document.getElementById('cartModal');
        if (event.target === cartModal) {
            closeCart();
        }
    });

    // 修改购物车按钮点击事件
    document.querySelector('.cart-button2').onclick = function(event) {
        event.stopPropagation(); // 防止事件冒泡
        showCart();
    }

    function completePayment() {
        const selectedPayment = document.querySelector('.payment-option.selected');
        if (!selectedPayment) {
            alert('请选择支付方式！');
            return;
        }
        
        const paymentType = selectedPayment.dataset.payment;
        const orderData = {
            total: '<%= gameMoney %>',
            amount: '<%= gameMoney %>',
            status: '已支付',
            paytype: paymentType,
            gamename: '<%= gameName %>',
            gameimg: '<%= gameImg %>',
            gametxt: '<%= gameDesc %>',
            user_id: 1  // 这里需要替换为实际的用户ID
        };
        
        // 发送订单数据到服务器
        fetch('OrderServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderData)
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('支付成功！');
                window.location.href = 'dingdan.jsp';
            } else {
                alert('支付失败：' + data.error);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('系统错误，请稍后重试');
        });
    }
</script>
</html>
