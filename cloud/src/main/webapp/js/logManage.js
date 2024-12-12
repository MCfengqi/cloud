/**
 * 日志管理前端JavaScript文件
 * 用途：处理日志查询界面的所有前端交互，包括：
 * 1. 展示日志列表界面
 * 2. 处理日志的查询和筛选
 * 3. 处理日期范围选择
 * 4. 处理与后端的数据交互
 */

// 立即执行函数来避免全局命名空间污染
(function() {
    // 日志管理功能的实现
    function showLogContent(contentType, event) {
        console.log('Showing log content');
        
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
                        <button class="btn btn-primary" onclick="searchLogs()">
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
            
            console.log('Setting content and title');
            document.getElementById('contentBody').innerHTML = content;
            document.getElementById('contentTitle').textContent = '日志查询';
            
            console.log('Loading log list');
            loadLogList();
        }
    }

    function loadLogList(searchParams = {}) {
        console.log('Loading logs with params:', searchParams);
        
        const queryString = new URLSearchParams(searchParams).toString();
        const url = `LogManageServlet?action=list${queryString ? '&' + queryString : ''}`;
        console.log('Request URL:', url);
        
        fetch(url)
            .then(response => {
                console.log('Response status:', response.status);
                return response.json();
            })
            .then(logs => {
                console.log('Received logs:', logs);
                const tbody = document.querySelector('#logTable tbody');
                if (!logs || logs.length === 0) {
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
                console.error('Error loading logs:', error);
                const tbody = document.querySelector('#logTable tbody');
                tbody.innerHTML = '<tr><td colspan="7" style="text-align: center; color: red;">加载失败</td></tr>';
            });
    }

    function searchLogs() {
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;
        const searchText = document.getElementById('searchInput').value;
        
        const searchParams = {
            startDate,
            endDate,
            searchText
        };
        
        loadLogList(searchParams);
    }

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

    // 导出函数到全局作用域
    window.showLogContent = showLogContent;
    window.loadLogList = loadLogList;
    window.searchLogs = searchLogs;
    window.formatDate = formatDate;
})(); 