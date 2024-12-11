/**
 * 日志管理前端JavaScript文件
 * 用途：处理日志查询界面的所有前端交互，包括：
 * 1. 展示日志列表界面
 * 2. 处理日志的查询和筛选
 * 3. 处理日期范围选择
 * 4. 处理与后端的数据交互
 */

// 将showContent改名为showLogContent并导出到全局
window.showLogContent = function(contentType, event) {
    if (contentType === 'logList') {
        const content = `
            <div class="toolbar-container">
                <div class="search-container">
                    <div class="date-range">
                        <input type="date" id="startDate" class="date-input">
                        <span>至</span>
                        <input type="date" id="endDate" class="date-input">
                    </div>
                    <input type="text" id="searchInput" placeholder="搜索日志内容..." class="search-input">
                    <button class="btn btn-primary" onclick="window.searchLogs()">
                        <span class="menu-icon">🔍</span>搜索
                    </button>
                </div>
            </div>
            <table class="table" id="logTable">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>操作类型</th>
                        <th>操作内容</th>
                        <th>操作用户</th>
                        <th>IP地址</th>
                        <th>操作时间</th>
                        <th>操作结果</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td colspan="7" style="text-align: center;">加载中...</td>
                    </tr>
                </tbody>
            </table>
        `;
        
        document.getElementById('contentBody').innerHTML = content;
        document.getElementById('contentTitle').textContent = '日志查询';
        window.loadLogList();
    }
};

// 加载日志列表
window.loadLogList = function(searchParams = {}) {
    const queryString = new URLSearchParams(searchParams).toString();
    fetch(`LogManageServlet?action=list${queryString ? '&' + queryString : ''}`)
        .then(response => response.json())
        .then(logs => {
            console.log('Received logs data:', logs);
            const tbody = document.querySelector('#logTable tbody');
            if (logs.length === 0) {
                tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">没有日志数据</td></tr>';
                return;
            }
            
            tbody.innerHTML = logs.map(log => {
                return `
                    <tr>
                        <td>${log.id || ''}</td>
                        <td>${log.operation_type || ''}</td>
                        <td>${log.operation_content || ''}</td>
                        <td>${log.username || ''}</td>
                        <td>${log.ip_address || ''}</td>
                        <td>${log.created_at ? formatDate(log.created_at) : ''}</td>
                        <td>${log.result || ''}</td>
                    </tr>
                `;
            }).join('');
        })
        .catch(error => {
            console.error('Error:', error);
            const tbody = document.querySelector('#logTable tbody');
            tbody.innerHTML = '<tr><td colspan="7" style="text-align: center; color: red;">加载失败</td></tr>';
        });
};

// 搜索日志
window.searchLogs = function() {
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;
    const searchText = document.getElementById('searchInput').value;
    
    const searchParams = {
        startDate,
        endDate,
        searchText
    };
    
    window.loadLogList(searchParams);
};

// 添加日期格式化函数
function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
}

// 导出日期格式化函数到全局
window.formatDate = formatDate;

// 添加样式
const logManageStyles = `
    .search-container {
        display: flex;
        gap: 10px;
        margin-bottom: 20px;
        align-items: center;
    }
    
    .date-range {
        display: flex;
        align-items: center;
        gap: 10px;
    }
    
    .date-input {
        padding: 8px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }
    
    .search-input {
        flex: 1;
        padding: 8px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }
`;

// 添加样式到页面
const styleElement = document.createElement('style');
styleElement.textContent = logManageStyles;
document.head.appendChild(styleElement); 