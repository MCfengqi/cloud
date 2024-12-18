/**
 * 游戏订单管理前端JavaScript文件
 * 用途：处理游戏订单管理界面的所有前端交互，包括：
 * 1. 展示订单列表界面
 * 2. 处理订单的查询和筛选
 * 3. 处理订单状态的更新
 * 4. 处理与后端的数据交互
 */

// 首先定义函数
function showOrderContent(contentType, event) {
    console.log('showOrderContent called with:', contentType);
    if (contentType === 'orderManage') {
        const content = `
            <div class="toolbar-container">
                <div class="search-container">
                    <input type="text" id="orderSearchInput" placeholder="搜索订单..." class="search-input" onkeyup="searchOrders()">
                    <select id="orderStatusFilter" class="status-select" onchange="searchOrders()">
                        <option value="" class="status-option status-all">全部状态</option>
                        <option value="已支付" class="status-option status-success">已支付</option>
                        <option value="待付款" class="status-option status-warning">待付款</option>
                        <option value="已完成" class="status-option status-info">已完成</option>
                        <option value="已取消" class="status-option status-danger">已取消</option>
                    </select>
                </div>
            </div>
            <table class="table" id="orderTable">
                <thead>
                    <tr>
                        <th>订单ID</th>
                        <th>用户ID</th>
                        <th>游戏名称</th>
                        <th>游戏图片</th>
                        <th>游戏介绍</th>
                        <th>订单总额</th>
                        <th>实付金额</th>
                        <th>支付方式</th>
                        <th>订单状态</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td colspan="10" style="text-align: center;">加载中...</td>
                    </tr>
                </tbody>
            </table>
        `;
        
        document.getElementById('contentBody').innerHTML = content;
        document.getElementById('contentTitle').textContent = '订单管理';
        loadOrderList();
    }
}

function loadOrderList() {
    fetch('GameOrderManageServlet?action=list')
        .then(response => response.json())
        .then(orders => {
            console.log('Received orders:', orders);
            const tbody = document.querySelector('#orderTable tbody');
            if (orders.length === 0) {
                tbody.innerHTML = '<tr><td colspan="10" style="text-align: center;">没有订单数据</td></tr>';
                return;
            }
            
            tbody.innerHTML = orders.map(order => `
                <tr>
                    <td>${order.id || ''}</td>
                    <td>${order.user_id || ''}</td>
                    <td>${order.gamename || ''}</td>
                    <td>
                        <img src="${order.gameimg || ''}" 
                             alt="${order.gamename || ''}" 
                             style="max-width: 100px; max-height: 60px;">
                    </td>
                    <td>
                        <div class="game-description" title="${order.gametxt || ''}">
                            ${order.gametxt ? order.gametxt.substring(0, 50) + '...' : ''}
                        </div>
                    </td>
                    <td>￥${order.total || '0.00'}</td>
                    <td>￥${order.amount || '0.00'}</td>
                    <td>${order.paytype || ''}</td>
                    <td>${getOrderStatusBadge(order.status)}</td>
                    <td>${formatOrderDate(order.datetime) || ''}</td>
                    <td>
                        <div class="btn-group">
                            <button onclick="editOrder(${order.id})" class="btn btn-warning">
                                <span class="menu-icon">✏️</span>编辑
                            </button>
                            <button onclick="deleteOrder(${order.id})" class="btn btn-danger">
                                <span class="menu-icon">🗑️</span>删除
                            </button>
                        </div>
                    </td>
                </tr>
            `).join('');
        })
        .catch(error => {
            console.error('Error:', error);
            const tbody = document.querySelector('#orderTable tbody');
            tbody.innerHTML = '<tr><td colspan="10" style="text-align: center; color: red;">加载失败</td></tr>';
        });
}

function formatOrderDate(dateString) {
    if (!dateString) return '';
    try {
        const date = new Date(dateString);
        if (isNaN(date.getTime())) {
            return dateString;
        }
        return date.toLocaleString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: false
        });
    } catch (e) {
        console.error('Date formatting error:', e);
        return dateString;
    }
}

function getOrderStatusBadge(status) {
    const statusMap = {
        '已支付': '<span class="badge badge-success">已支付</span>',
        '待付款': '<span class="badge badge-warning">待付款</span>',
        '已完成': '<span class="badge badge-info">已完成</span>',
        '已取消': '<span class="badge badge-danger">已取消</span>',
        '全部状态': '<span class="badge badge-secondary">全部状态</span>'
    };
    return statusMap[status] || `<span class="badge badge-secondary">${status}</span>`;
}

function searchOrders() {
    const searchText = document.getElementById('orderSearchInput').value.toLowerCase();
    const statusFilter = document.getElementById('orderStatusFilter').value;
    const tbody = document.querySelector('#orderTable tbody');
    const rows = tbody.getElementsByTagName('tr');
    
    for (let row of rows) {
        const cells = row.getElementsByTagName('td');
        let found = false;
        
        if (cells.length > 0) {  // 确保不是空行
            let matchesStatus = true;
            if (statusFilter) {  // 只有当选择了特定状态时才进行状态过滤
                const statusCell = cells[8].textContent;  // 订单状态列
                matchesStatus = statusCell.includes(statusFilter);
            }
            
            let matchesSearch = false;
            if (searchText) {  // 只有当输入了搜索文本时才进行搜索
                for (let cell of cells) {
                    if (cell.textContent.toLowerCase().includes(searchText)) {
                        matchesSearch = true;
                        break;
                    }
                }
            } else {
                matchesSearch = true;  // 如果没有搜索文本，视为匹配搜索条件
            }
            
            found = matchesSearch && matchesStatus;
        }
        
        row.style.display = found ? '' : 'none';
    }
}

function editOrder(orderId) {
    fetch(`GameOrderManageServlet?action=get&id=${orderId}`)
        .then(response => response.json())
        .then(order => {
            const content = `
                <h3>编辑订单</h3>
                <form id="editOrderForm" class="order-form" onsubmit="submitEditOrder(event, ${orderId})">
                    <div class="form-group">
                        <label>订单状态：</label>
                        <select name="status" class="status-select" required>
                            <option value="已支付" class="status-option status-success" ${order.status === '已支付' ? 'selected' : ''}>已支付</option>
                            <option value="待付款" class="status-option status-warning" ${order.status === '待付款' ? 'selected' : ''}>待付款</option>
                            <option value="已完成" class="status-option status-info" ${order.status === '已完成' ? 'selected' : ''}>已完成</option>
                            <option value="已取消" class="status-option status-danger" ${order.status === '已取消' ? 'selected' : ''}>已取消</option>
                        </select>
                    </div>
                    <div class="button-group">
                        <button type="submit" class="btn btn-primary">保存</button>
                        <button type="button" class="btn btn-secondary" onclick="closeModal()">取消</button>
                    </div>
                </form>
            `;
            openModal(content);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('加载订单信息失败');
        });
}

function deleteOrder(orderId) {
    if (confirm('确定要删除个订单吗？')) {
        fetch('GameOrderManageServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                action: 'delete',
                id: orderId
            })
        })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('删除成功！');
                loadOrderList();
            } else {
                alert('删除失败：' + (result.error || '未知错误'));
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('系统错误：' + error.message);
        });
    }
}

function submitEditOrder(event, orderId) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    
    const data = {
        action: 'update',
        id: orderId,
        status: formData.get('status')
    };
    
    fetch('GameOrderManageServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            alert('更新成功！');
            closeModal();
            loadOrderList();
        } else {
            alert('更新失败：' + (result.error || '未知错误'));
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('系统错误：' + error.message);
    });
}

// 添加样式
const additionalOrderStyles = `
    .status-select {
        padding: 8px 12px;
        border: 1px solid #ddd;
        border-radius: 4px;
        background-color: #fff;
        font-size: 14px;
        cursor: pointer;
        min-width: 120px;
        transition: all 0.3s ease;
    }

    .status-select:hover {
        border-color: #007bff;
    }

    .status-select:focus {
        outline: none;
        border-color: #007bff;
        box-shadow: 0 0 0 2px rgba(0,123,255,0.25);
    }

    .status-option {
        padding: 8px 12px;
        font-size: 14px;
    }

    .status-all {
        color: #6c757d;
        font-weight: bold;
    }

    .status-success {
        color: #28a745;
    }

    .status-warning {
        color: #ffc107;
    }

    .status-info {
        color: #17a2b8;
    }

    .status-danger {
        color: #dc3545;
    }

    .badge {
        padding: 6px 10px;
        border-radius: 4px;
        font-size: 12px;
        font-weight: bold;
        color: white;
        display: inline-block;
        line-height: 1;
        text-align: center;
        white-space: nowrap;
        vertical-align: baseline;
    }

    .badge-secondary {
        background-color: #6c757d;
    }

    .badge-success {
        background-color: #28a745;
    }

    .badge-warning {
        background-color: #ffc107;
        color: #000;
    }

    .badge-info {
        background-color: #17a2b8;
    }

    .badge-danger {
        background-color: #dc3545;
    }
`;

// 添加样式到页面
const orderStyleElement = document.createElement('style');
orderStyleElement.textContent = additionalOrderStyles;
document.head.appendChild(orderStyleElement);

// 立即导出所有函数到全局
window.showOrderContent = showOrderContent;
window.loadOrderList = loadOrderList;
window.searchOrders = searchOrders;
window.editOrder = editOrder;
window.deleteOrder = deleteOrder;
window.submitEditOrder = submitEditOrder;
window.formatOrderDate = formatOrderDate;
window.getOrderStatusBadge = getOrderStatusBadge; 