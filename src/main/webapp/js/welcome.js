function toggleSubmenu(submenuId) {
    console.log('Toggling submenu:', submenuId);
    const submenu = document.getElementById(submenuId);
    const menuWrapper = submenu.parentElement.querySelector('.menu-item-wrapper');
    
    if (submenu && menuWrapper) {
        console.log('Before toggle - submenu classList:', submenu.classList.toString());
        console.log('Before toggle - submenu style.display:', submenu.style.display);
        
        submenu.classList.toggle('active');
        menuWrapper.classList.toggle('active');
        
        console.log('After toggle - submenu classList:', submenu.classList.toString());
        console.log('After toggle - submenu style.display:', submenu.style.display);
        
        submenu.style.display = submenu.classList.contains('active') ? 'block' : 'none';
    } else {
        console.error('Required elements not found:');
        console.error('submenu:', submenu);
        console.error('menuWrapper:', menuWrapper);
    }
}

async function showContent(type, event) {
    console.log('showContent called with type:', type);
    console.log('Event:', event);
    
    if (event) {
        event.stopPropagation();
    }

    const contentTitle = document.getElementById('contentTitle');
    const contentBody = document.getElementById('contentBody');

    if (!contentTitle || !contentBody) {
        console.error('Required elements not found:');
        console.error('contentTitle:', contentTitle);
        console.error('contentBody:', contentBody);
        return;
    }

    try {
        switch (type) {
            case 'userList':
                contentTitle.textContent = '用户管理';
                contentBody.innerHTML = `
                    <div class="content-wrapper">
                        <div class="toolbar-container">
                            <div class="search-container">
                                <input type="text" id="userSearchInput" placeholder="请输入要搜索的用户信息...">
                            </div>
                            <div class="button-container">
                                <button class="btn btn-primary" onclick="searchUsers()">
                                    <span class="menu-icon">🔍</span>搜索
                                </button>
                                <button class="btn btn-primary" onclick="addUser()">
                                    <span class="menu-icon">➕</span>添加用户
                                </button>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table id="userTable" class="table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>用户名</th>
                                        <th>密码</th>
                                        <th>邮箱</th>
                                        <th>手机号</th>
                                        <th>用户类型</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td colspan="7" style="text-align: center;">加载中...</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                `;
                loadUserList();
                break;
                
            case 'adminList':
                contentTitle.textContent = '管理员管理';
                contentBody.innerHTML = `
                    <div class="content-wrapper">
                        <div class="toolbar">
                            <button class="btn btn-primary" onclick="addAdmin()">
                                <span class="menu-icon">➕</span>添加管理员
                            </button>
                        </div>
                        <div class="table-responsive">
                            <table id="adminTable" class="table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>管理员名称</th>
                                        <th>邮箱</th>
                                        <th>手机号</th>
                                        <th>状态</th>
                                        <th>最后登录时间</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td colspan="7" style="text-align: center;">加载中...</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                `;
                loadAdminList();
                break;
                
            case 'contentManage':
                try {
                    console.log('Loading content management...');
                    contentTitle.textContent = '内容管理';
                    contentBody.innerHTML = `
                        <div class="content-wrapper">
                            <div class="toolbar">
                                <button class="btn btn-primary" onclick="addGame()">
                                    <span class="menu-icon">➕</span>添加游戏
                                </button>
                            </div>
                            <div class="table-responsive">
                                <table id="gameTable" class="table">
                                    <thead>
                                        <tr>
                                            <th>游戏ID</th>
                                            <th>游戏名称</th>
                                            <th>游戏图片</th>
                                            <th>游戏描述</th>
                                            <th>游戏链接</th>
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
                            </div>
                        </div>
                    `;
                    console.log('Content management HTML set up, loading game list...');
                    if (typeof loadGameList === 'function') {
                        loadGameList();
                    } else {
                        console.error('loadGameList function is not defined');
                        throw new Error('loadGameList function is not defined');
                    }
                } catch (error) {
                    console.error('Error in content management:', error);
                    contentBody.innerHTML = `<div class="error">加载内容管理失败: ${error.message}</div>`;
                }
                break;
                
            default:
                contentTitle.textContent = '欢迎使用';
                contentBody.innerHTML = '<h3>欢迎使用云城游戏管理系统</h3>';
        }
    } catch (error) {
        console.error('Error loading content:', error);
        contentBody.innerHTML = '<p class="error">加载内容时发生错误</p>';
    }
}

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

window.onclick = function(event) {
    const modal = document.getElementById('userModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
}