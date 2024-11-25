// 加载游戏列表
function loadGameList() {
    console.log('Loading game list...');
    const tbody = document.querySelector('#gameTable tbody');
    
    fetch('GameManageServlet?action=list')
        .then(response => {
            console.log('Response status:', response.status);
            return response.json();
        })
        .then(games => {
            console.log('Received games:', games);
            if (games.length === 0) {
                tbody.innerHTML = '<tr><td colspan="8" style="text-align: center;">没有游戏数据</td></tr>';
                return;
            }
            
            tbody.innerHTML = games.map(game => `
                <tr>
                    <td>${game.gameid}</td>
                    <td>${game.gamename}</td>
                    <td><img src="${game.gameimg}" alt="${game.gamename}" style="max-width: 100px;"></td>
                    <td>${game.gametxt}</td>
                    <td><a href="${game.gamelink}" target="_blank">访问链接</a></td>
                    <td>${formatDate(game.created_at)}</td>
                    <td>${formatDate(game.updated_at)}</td>
                    <td>
                        <div class="btn-group">
                            <button class="btn btn-warning" onclick="editGame(${game.gameid})">
                                <span class="menu-icon">✏️</span>编辑
                            </button>
                            <button class="btn btn-danger" onclick="deleteGame(${game.gameid})">
                                <span class="menu-icon">🗑️</span>删除
                            </button>
                        </div>
                    </td>
                </tr>
            `).join('');
        })
        .catch(error => {
            console.error('Error:', error);
            tbody.innerHTML = `<tr><td colspan="8" style="text-align: center; color: red;">
                加载失败: ${error.message}</td></tr>`;
        });
}

// 格式化日期
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

function submitAddGame(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    
    fetch('GameManageServlet?action=add', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            alert('添加成功！');
            closeModal();
            loadGameList();
        } else {
            alert('添加失败：' + (result.error || '未知错误'));
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('系统错误：' + error.message);
    });
}

// 添加游戏
function addGame() {
    const content = `
        <h3>添加游戏</h3>
        <form id="addGameForm" onsubmit="submitAddGame(event)">
            <div class="form-group">
                <label>游戏名称：</label>
                <input type="text" name="gamename" required>
            </div>
            <div class="form-group">
                <label>游戏图片：</label>
                <input type="file" name="gameimg" accept="image/*" required>
            </div>
            <div class="form-group">
                <label>游戏描述：</label>
                <textarea name="gametxt" required rows="4"></textarea>
            </div>
            <div class="form-group">
                <label>游戏链接：</label>
                <input type="url" name="gamelink" required>
            </div>
            <div class="button-group">
                <button type="submit" class="btn btn-primary">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeModal()">取消</button>
            </div>
        </form>
    `;
    openModal(content);
}

// 编辑游戏
function editGame(gameId) {
    fetch(`GameManageServlet?action=get&id=${gameId}`)
        .then(response => response.json())
        .then(game => {
            const content = `
                <h3>编辑游戏</h3>
                <form id="editGameForm" onsubmit="submitEditGame(event, ${gameId})">
                    <div class="form-group">
                        <label>游戏名称：</label>
                        <input type="text" name="gamename" value="${game.gamename}" required>
                    </div>
                    <div class="form-group">
                        <label>当前图片：</label>
                        <img src="${game.gameimg}" alt="${game.gamename}" style="max-width: 200px; display: block; margin: 10px 0;">
                        <label>更新图片：</label>
                        <input type="file" name="gameimg" accept="image/*">
                    </div>
                    <div class="form-group">
                        <label>游戏描述：</label>
                        <textarea name="gametxt" required rows="4">${game.gametxt}</textarea>
                    </div>
                    <div class="form-group">
                        <label>游戏链接：</label>
                        <input type="url" name="gamelink" value="${game.gamelink}" required>
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
            alert('加载游戏信息失败');
        });
}

// 删除游戏
function deleteGame(gameId) {
    if (confirm('确定要删除这个游戏吗？')) {
        fetch('GameManageServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `action=delete&id=${gameId}`
        })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                loadGameList();
            } else {
                alert('删除失败：' + result.error);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('删除失败');
        });
    }
}

// 提交编辑游戏表单
function submitEditGame(event, gameId) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    formData.append('id', gameId);
    formData.append('action', 'update');
    
    fetch('GameManageServlet', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            alert('更新成功！');
            closeModal();
            loadGameList();
        } else {
            alert('更新失败：' + (result.error || '未知错误'));
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('系统错误：' + error.message);
    });
} 