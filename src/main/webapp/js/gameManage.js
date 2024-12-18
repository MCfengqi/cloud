/**
 * 游戏管理前端JavaScript文件
 * 用途：处理游戏管理界面的所有前端交互，包括：
 * 1. 展示游戏列表界面
 * 2. 处理游戏的添加操作
 * 3. 处理游戏的编辑操作
 * 4. 处理游戏的删除操作
 * 5. 处理游戏图片预览和验证
 * 6. 管理模态框的显示和隐藏
 * 7. 处理表单提交和数据验证
 * 8. 处理与后端的数据交互
 */

// 将showContent改名为showGameContent并导出到全局
window.showGameContent = function(contentType, event) {
    if (contentType === 'contentManage') {
        const content = `
            <div class="toolbar-container">
                <div class="button-container">
                    <button class="btn btn-primary" onclick="window.addGame()">
                        <span class="menu-icon">➕</span>添加游戏
                    </button>
                </div>
            </div>
            <table class="table" id="gameTable">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>游戏名称</th>
                        <th>游戏图片</th>
                        <th>游戏描述</th>
                        <th>游戏链接</th>
                        <th>游戏价格</th>
                        <th>创建时间</th>
                        <th>更新时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td colspan="8" style="text-align: center;">加载中...</td>
                    </tr>
                </tbody>
            </table>
        `;
        
        document.getElementById('contentBody').innerHTML = content;
        document.getElementById('contentTitle').textContent = '内容管理';
        window.loadGameList();
    }
};

// 将addGame函数导出到全局
window.addGame = function() {
    const content = `
        <h3>添加游戏</h3>
        <form id="addGameForm" class="game-form" onsubmit="window.submitAddGame(event)">
            <div class="form-group game-form-group">
                <label>游戏名称：</label>
                <input type="text" name="gamename" class="game-input" required>
            </div>
            <div class="form-group game-form-group">
                <label>游戏图片链接：</label>
                <input type="url" name="gameimg" class="game-input" required placeholder="请输入图片URL">
                <small class="form-text text-muted">请输入图片的完整URL地址（如: https://example.com/image.jpg）</small>
                <button type="button" class="btn btn-upload" onclick="window.open('https://img.mcfengqi.icu', '_blank')">
                    <span class="menu-icon">📤</span>上传图片
                </button>
            </div>
            <div class="form-group game-form-group">
                <label>游戏描述：</label>
                <textarea name="gametxt" class="game-input" required rows="4"></textarea>
            </div>
            <div class="form-group game-form-group">
                <label>游戏链接：</label>
                <input type="url" name="gamelink" class="game-input" required>
            </div>
            <div class="form-group game-form-group">
                <label>游戏价格：</label>
                <input type="number" name="gamemoney" class="game-input" required step="0.01" min="0">
            </div>
            <div class="button-group">
                <button type="submit" class="btn btn-primary">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeModal()">取消</button>
            </div>
        </form>
    `;
    openModal(content);
};

// 将submitAddGame函数导出到全局
window.submitAddGame = async function(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    const gameData = {
        action: 'add'
    };
    
    formData.forEach((value, key) => {
        gameData[key] = value;
    });
    
    // 验证图片URL
    if (!window.isValidUrl(gameData.gameimg)) {
        alert('请输入有效的图片URL地址');
        return;
    }
    
    try {
        // 预加载图片以验证可访问性
        await window.preloadImage(gameData.gameimg);
        
        // 图片验证通过后继续提交
        fetch('GameManageServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(gameData)
        })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('添加成功！');
                closeModal();
                window.loadGameList();
            } else {
                alert('添加失败：' + (result.error || '未知错误'));
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('系统错误：' + error.message);
        });
    } catch (error) {
        alert('图片无法访问，请检查URL是否正确');
    }
}

// 将其他函数也导出到全局
window.loadGameList = loadGameList;
window.editGame = editGame;
window.deleteGame = deleteGame;
window.submitEditGame = submitEditGame;
window.isValidUrl = function(string) {
    try {
        new URL(string);
        return true;
    } catch (_) {
        return false;
    }
};

// 添加样式
const gameManageStyles = `
    .game-form {
        padding: 20px;
    }
    
    .game-form-group {
        margin-bottom: 15px;
    }
    
    .game-form-group label {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
    }
    
    .game-input {
        width: 100%;
        padding: 8px;
        border: 1px solid #ddd;
        border-radius: 4px;
        box-sizing: border-box;
        margin-bottom: 5px;
    }
    
    .game-input[type="url"] {
        font-family: monospace;
    }
    
    .text-muted {
        color: #6c757d;
        font-size: 12px;
        margin-top: 4px;
        display: block;
    }
    
    .preview-image {
        margin-top: 10px;
    }
    
    .preview-image img {
        max-width: 200px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }
    
    .button-group {
        margin-top: 20px;
        display: flex;
        gap: 10px;
        justify-content: flex-end;
    }
`;

// 添加样式到页面
const styleElement = document.createElement('style');
styleElement.textContent = gameManageStyles;
document.head.appendChild(styleElement);

// 添加modal相关函数
function openModal(content) {
    const modal = document.getElementById('userModal');
    const modalContent = document.getElementById('modalContent');
    if (modal && modalContent) {
        modalContent.innerHTML = content;
        modal.style.display = 'block';
    }
}

function closeModal() {
    const modal = document.getElementById('userModal');
    if (modal) {
        modal.style.display = 'none';
    }
}

// 将modal函数导出到全局
window.openModal = openModal;
window.closeModal = closeModal;

// 添加loadGameList函数的实现
function loadGameList() {
    console.log('Loading game list...');
    fetch('GameManageServlet?action=list')
        .then(response => {
            console.log('Response status:', response.status);
            return response.json();
        })
        .then(games => {
            console.log('Received games:', games);
            const tableBody = document.querySelector('#gameTable tbody');
            
            // 添加调试日志
            console.log('First game object:', games[0]);
            
            tableBody.innerHTML = games.map(game => `
                <tr>
                    <td>${game.gameid || ''}</td>
                    <td>${game.gamename || ''}</td>
                    <td>
                        <img src="${game.gameimg || ''}" 
                             alt="${game.gamename || ''}" 
                             style="max-width: 100px; max-height: 60px; transition: opacity 0.3s ease;"
                             onerror="handleImageError(this)"
                             onload="this.style.opacity=1">
                    </td>
                    <td>${game.gametxt || ''}</td>
                    <td>
                        <a href="${game.gamelink || '#'}" target="_blank" rel="noopener noreferrer">
                            ${game.gamelink ? '查看下载链接' : ''}
                        </a>
                    </td>
                    <td>${game.gamemoney || '0.00'}</td>
                    <td>${formatDate(game.created_at) || ''}</td>
                    <td>${formatDate(game.updated_at) || ''}</td>
                    <td>
                        <div class="btn-group">
                            <button onclick="editGame(${game.gameid})" class="btn btn-warning">
                                <span class="menu-icon">✏️</span>编辑
                            </button>
                            <button onclick="deleteGame(${game.gameid})" class="btn btn-danger">
                                <span class="menu-icon">🗑️</span>删除
                            </button>
                        </div>
                    </td>
                </tr>
            `).join('');
        })
        .catch(error => {
            console.error('Error:', error);
            const tableBody = document.querySelector('#gameTable tbody');
            tableBody.innerHTML = '<tr><td colspan="8" style="text-align: center; color: red;">加载失败</td></tr>';
        });
}

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

// 将formatDate导出到全局
window.formatDate = formatDate;

// 添加editGame函数的实现
function editGame(gameId) {
    fetch(`GameManageServlet?action=get&id=${gameId}`)
        .then(response => response.json())
        .then(game => {
            const content = `
                <div class="edit-game-container">
                    <h3>编辑游戏</h3>
                    <form id="editGameForm" class="game-form" onsubmit="window.submitEditGame(event, ${gameId})">
                        <div class="form-scroll-container">
                            <div class="form-group game-form-group">
                                <label>游戏名称：</label>
                                <input type="text" name="gamename" class="game-input" value="${game.gamename}" required>
                            </div>
                            <div class="form-group game-form-group">
                                <label>游戏图片链接：</label>
                                <input type="url" name="gameimg" class="game-input" value="${game.gameimg}" required placeholder="请输入图片URL">
                                <small class="form-text text-muted">请输入图片的完整URL地址（如: https://example.com/image.jpg）</small>
                                <button type="button" class="btn btn-upload" onclick="window.open('https://img.mcfengqi.icu', '_blank')">
                                    <span class="menu-icon">📤</span>上传图片
                                </button>
                                <div class="preview-image">
                                    <img src="${game.gameimg}" alt="${game.gamename}" 
                                         onerror="this.onerror=null; this.src='data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgdmlld0JveD0iMCAwIDEwMCAxMDAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PHJlY3Qgd2lkdGg9IjEwMCIgaGVpZ2h0PSIxMDAiIGZpbGw9IiNlZWUiLz48dGV4dCB4PSI1MCIgeT0iNTAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzk5OSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZHk9Ii4zZW0iPk5vIEltYWdlPC90ZXh0Pjwvc3ZnPg=='">
                                </div>
                            </div>
                            <div class="form-group game-form-group">
                                <label>游戏描述：</label>
                                <textarea name="gametxt" class="game-input" required rows="4">${game.gametxt}</textarea>
                            </div>
                            <div class="form-group game-form-group">
                                <label>游戏链接：</label>
                                <input type="url" name="gamelink" class="game-input" value="${game.gamelink}" required>
                            </div>
                            <div class="form-group game-form-group">
                                <label>游戏价格：</label>
                                <input type="number" name="gamemoney" class="game-input" value="${game.gamemoney || '0.00'}" required step="0.01" min="0">
                            </div>
                        </div>
                        <div class="button-group">
                            <button type="submit" class="btn btn-primary">保存</button>
                            <button type="button" class="btn btn-secondary" onclick="closeModal()">取消</button>
                        </div>
                    </form>
                </div>
            `;
            openModal(content);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('加载游戏信息失败');
        });
}

// 添加submitEditGame函数的实现
function submitEditGame(event, gameId) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    
    // 将FormData转换为普通对象
    const gameData = {
        action: 'update',
        id: gameId
    };
    formData.forEach((value, key) => {
        gameData[key] = value;
    });
    
    // 验证图片URL
    if (!window.isValidUrl(gameData.gameimg)) {
        alert('请输入有效的图片URL地址');
        return;
    }
    
    fetch('GameManageServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(gameData)
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            alert('更新成功！');
            closeModal();
            window.loadGameList();
        } else {
            alert('更新失败：' + (result.error || '未知错误'));
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('系统错误：' + error.message);
    });
}

// 修改deleteGame函数的实现
function deleteGame(gameId) {
    if (!confirm('确定要删除这个游戏吗？')) {
        return;
    }
    
    const gameData = {
        action: 'delete',
        id: gameId
    };
    
    // 显示加载提示
    const loadingToast = document.createElement('div');
    loadingToast.className = 'loading-toast';
    loadingToast.textContent = '正在删除...';
    document.body.appendChild(loadingToast);
    
    fetch('GameManageServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(gameData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('网络响应错误');
        }
        return response.json();
    })
    .then(result => {
        // 移除加载提示
        loadingToast.remove();
        
        if (result.success) {
            alert(result.message || '删除成功！');
            window.loadGameList();
        } else {
            alert('删除失败：' + (result.error || '未知错误'));
        }
    })
    .catch(error => {
        // 移除加载提示
        loadingToast.remove();
        
        console.error('Error:', error);
        alert('系统错误：' + error.message);
    });
}

// 添加加载提示的样式
const loadingStyles = `
    .loading-toast {
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background-color: rgba(0, 0, 0, 0.7);
        color: white;
        padding: 15px 30px;
        border-radius: 5px;
        z-index: 9999;
    }
`;

// 添加样式到页面
const loadingStyleElement = document.createElement('style');
loadingStyleElement.textContent = loadingStyles;
document.head.appendChild(loadingStyleElement);

// 添加图片验证函数
window.isValidUrl = function(url) {
    try {
        new URL(url);
        return true;
    } catch (e) {
        return false;
    }
}

// 添加图片预加载函数
window.preloadImage = function(url) {
    return new Promise((resolve, reject) => {
        const img = new Image();
        img.onload = () => resolve(url);
        img.onerror = () => {
            console.warn(`图片加载失败: ${url}`);
            resolve(url); // 即使图片加载失败也允许继续
        };
        img.src = url;
    });
}

// 添加图片错误处理函数
window.handleImageError = function(img) {
    // 防止循环调用
    img.onerror = null;
    
    // 设置默认图片为 Base64 编码的占位图
    img.src = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgdmlld0JveD0iMCAwIDEwMCAxMDAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PHJlY3Qgd2lkdGg9IjEwMCIgaGVpZ2h0PSIxMDAiIGZpbGw9IiNlZWUiLz48dGV4dCB4PSI1MCIgeT0iNTAiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzk5OSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZHk9Ii4zZW0iPk5vIEltYWdlPC90ZXh0Pjwvc3ZnPg==';
    img.style.opacity = '0.5';
    img.style.border = '1px solid #ddd';
    img.style.padding = '5px';
};

// 添加样式
const additionalStyles = `
    .game-table-image {
        max-width: 100px;
        max-height: 60px;
        transition: opacity 0.3s ease;
        border-radius: 4px;
        object-fit: cover;
    }
    
    .game-table-image.error {
        opacity: 0.5;
        border: 1px solid #ddd;
        padding: 5px;
    }
    
    .btn-group {
        display: flex;
        gap: 5px;
        justify-content: center;
    }
    
    .btn {
        padding: 5px 10px;
        border-radius: 4px;
        border: none;
        cursor: pointer;
        display: flex;
        align-items: center;
        gap: 5px;
    }
    
    .btn-warning {
        background-color: #ffc107;
        color: #000;
    }
    
    .btn-danger {
        background-color: #dc3545;
        color: #fff;
    }
`;

// 添加新样式到页面
const additionalStyleElement = document.createElement('style');
additionalStyleElement.textContent = additionalStyles;
document.head.appendChild(additionalStyleElement);

// 添加新的样式
const editFormStyles = `
    .edit-game-container {
        max-height: 80vh;
        display: flex;
        flex-direction: column;
    }
    
    .modal-content {
        max-height: 80vh;
        display: flex;
        flex-direction: column;
        margin: 10vh auto !important;
    }
    
    .form-scroll-container {
        flex: 1;
        overflow-y: auto;
        padding-right: 10px;
        margin-bottom: 15px;
        max-height: calc(70vh - 120px);
    }
    
    .form-scroll-container::-webkit-scrollbar {
        width: 6px;
    }
    
    .form-scroll-container::-webkit-scrollbar-track {
        background: #f1f1f1;
        border-radius: 3px;
    }
    
    .form-scroll-container::-webkit-scrollbar-thumb {
        background: #888;
        border-radius: 3px;
    }
    
    .form-scroll-container::-webkit-scrollbar-thumb:hover {
        background: #555;
    }
    
    .preview-image {
        margin: 10px 0;
        text-align: center;
    }
    
    .preview-image img {
        max-width: 100%;
        max-height: 200px;
        object-fit: contain;
        border: 1px solid #ddd;
        border-radius: 4px;
        padding: 5px;
    }
    
    .button-group {
        position: sticky;
        bottom: 0;
        background: white;
        padding: 15px 0 0;
        border-top: 1px solid #eee;
        margin-top: auto;
    }
    
    .modal-content {
        max-height: 80vh;
        display: flex;
        flex-direction: column;
    }
    
    .game-form {
        display: flex;
        flex-direction: column;
        height: 100%;
    }
`;

// 添加新样式到页面
const editFormStyleElement = document.createElement('style');
editFormStyleElement.textContent = editFormStyles;
document.head.appendChild(editFormStyleElement);

// 修改上传按钮样式
const uploadButtonStyles = `
    .btn-upload {
        margin-top: 10px;
        background-color: #007bff;
        color: white;
        border: none;
        padding: 8px 15px;
        border-radius: 4px;
        cursor: pointer;
        display: inline-flex;
        align-items: center;
        gap: 5px;
        transition: background-color 0.2s;
    }
    
    .btn-upload:hover {
        background-color: #0056b3;
    }
    
    .btn-upload .menu-icon {
        font-size: 16px;
    }
`;

// 添加新样式到页面
const uploadStyleElement = document.createElement('style');
uploadStyleElement.textContent = uploadButtonStyles;
document.head.appendChild(uploadStyleElement);
  