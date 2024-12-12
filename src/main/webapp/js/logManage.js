/**
 * 日志管理前端JavaScript文件
 * 用途：处理日志查询界面的所有前端交互，包括：
 * 1. 展示日志列表界面
 * 2. 处理日志的查询和筛选
 * 3. 处理日期范围选择
 * 4. 处理与后端的数据交互
 */

(function() {
    function showLogContent(contentType, event) {
        console.log('Showing log content');

        if (contentType === 'logList') {
            const content = `
                <div class="log-container">
                    <div class="toolbar-container">
                        <div class="search-container">
                            <div class="date-range">
                                <input type="date" id="startDate" class="date-input">
                                <span class="date-separator">至</span>
                                <input type="date" id="endDate" class="date-input">
                            </div>
                            <div class="search-box">
                                <input type="text" id="searchInput" placeholder="搜索日志内容..." class="search-input">
                                <button class="search-button" onclick="searchLogs()">
                                    <i class="fas fa-search"></i>
                                    搜索
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="table-wrapper">
                        <table class="log-table" id="logTable">
                            <thead>
                                <tr>
                                    <th>日志ID</th>
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
                                    <td colspan="7" class="loading-cell">加载中...</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            `;

            document.getElementById('contentBody').innerHTML = content;
            document.getElementById('contentTitle').textContent = '日志查询';

            // 添加样式
            const style = document.createElement('style');
            style.textContent = `
                .log-container {
                    padding: 20px;
                    background: #f8f9fa;
                    border-radius: 10px;
                    min-height: calc(100vh - 120px);
                }

                .toolbar-container {
                    margin-bottom: 20px;
                    padding: 20px;
                    background: #fff;
                    border-radius: 8px;
                    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
                }

                .search-container {
                    display: flex;
                    gap: 20px;
                    align-items: center;
                    flex-wrap: wrap;
                }

                .date-range {
                    display: flex;
                    align-items: center;
                    gap: 10px;
                }

                .date-input {
                    padding: 10px 15px;
                    border: 2px solid #e8e8e8;
                    border-radius: 6px;
                    font-size: 14px;
                    color: #333;
                    background-color: #f8f9fa;
                    transition: all 0.3s ease;
                    cursor: pointer;
                }

                .date-input:hover {
                    border-color: #4a90e2;
                }

                .date-input:focus {
                    outline: none;
                    border-color: #4a90e2;
                    box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.1);
                    background-color: #fff;
                }

                .date-separator {
                    color: #666;
                    font-size: 14px;
                    padding: 0 5px;
                }

                .search-box {
                    display: flex;
                    flex-grow: 1;
                    gap: 10px;
                }

                .search-input {
                    flex-grow: 1;
                    padding: 10px 15px;
                    border: 2px solid #e8e8e8;
                    border-radius: 6px;
                    font-size: 14px;
                    color: #333;
                    background-color: #f8f9fa;
                    transition: all 0.3s ease;
                }

                .search-input:hover {
                    border-color: #4a90e2;
                }

                .search-input:focus {
                    outline: none;
                    border-color: #4a90e2;
                    box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.1);
                    background-color: #fff;
                }

                .search-button {
                    padding: 10px 24px;
                    background: linear-gradient(145deg, #4a90e2, #357abd);
                    color: white;
                    border: none;
                    border-radius: 6px;
                    font-size: 14px;
                    cursor: pointer;
                    display: flex;
                    align-items: center;
                    gap: 8px;
                    transition: all 0.3s ease;
                    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                }

                .search-button:hover {
                    background: linear-gradient(145deg, #357abd, #4a90e2);
                    transform: translateY(-1px);
                    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
                }

                .search-button:active {
                    transform: translateY(1px);
                    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                }

                .table-wrapper {
                    background: #fff;
                    border-radius: 8px;
                    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
                    overflow: hidden;
                }

                .log-table {
                    width: 100%;
                    border-collapse: collapse;
                    border-spacing: 0;
                }

                .log-table thead {
                    background: #f5f7fa;
                }

                .log-table th {
                    padding: 15px;
                    text-align: left;
                    font-weight: 600;
                    color: #2c3e50;
                    border-bottom: 2px solid #e8e8e8;
                    white-space: nowrap;
                }

                .log-table td {
                    padding: 12px 15px;
                    border-bottom: 1px solid #edf2f7;
                    color: #4a5568;
                }

                .log-table tbody tr:hover {
                    background-color: #f8fafc;
                }

                .loading-cell {
                    text-align: center;
                    padding: 30px !important;
                    color: #666;
                    font-style: italic;
                }

                @media (max-width: 768px) {
                    .search-container {
                        flex-direction: column;
                        align-items: stretch;
                    }
                    
                    .date-range {
                        flex-direction: column;
                    }
                    
                    .search-box {
                        flex-direction: column;
                    }
                    
                    .search-button {
                        width: 100%;
                        justify-content: center;
                    }

                    .table-wrapper {
                        overflow-x: auto;
                    }
                }
            `;
            document.head.appendChild(style);

            loadLogList();
        }
    }

    function loadLogList(searchParams = {}) {
        console.log('Loading logs with params:', searchParams);

        const queryString = new URLSearchParams(searchParams).toString();
        const url = `LogManageServlet?action=list${queryString ? '&' + queryString : ''}`;

        fetch(url)
            .then(response => response.json())
            .then(logs => {
                const tbody = document.querySelector('#logTable tbody');
                if (!logs || logs.length === 0) {
                    tbody.innerHTML = '<tr><td colspan="7" class="loading-cell">没有找到日志数据</td></tr>';
                    return;
                }

                tbody.innerHTML = logs.map(log => `
                    <tr>
                        <td>${log.id || ''}</td>
                        <td>${log.operation_type || ''}</td>
                        <td>${log.operation_content || ''}</td>
                        <td>${log.username || ''}</td>
                        <td>${log.ip_address || ''}</td>
                        <td>${log.created_at ? formatDate(log.created_at) : ''}</td>
                        <td>${log.result || ''}</td>
                    </tr>
                `).join('');
            })
            .catch(error => {
                console.error('Error loading logs:', error);
                const tbody = document.querySelector('#logTable tbody');
                tbody.innerHTML = '<tr><td colspan="7" class="loading-cell" style="color: #e53e3e;">加载失败</td></tr>';
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

    window.showLogContent = showLogContent;
    window.loadLogList = loadLogList;
    window.searchLogs = searchLogs;
    window.formatDate = formatDate;
})();