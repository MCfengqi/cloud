// 菜单相关函数
function toggleSubmenu(id) {
    const menuItem = document.querySelector('.menu-item-wrapper').parentElement;
    const submenu = document.getElementById(id);
    const dropdownIcon = menuItem.querySelector('.dropdown-icon');
    
    menuItem.classList.toggle('active');
    
    if (submenu.classList.contains('show')) {
        submenu.classList.remove('show');
        dropdownIcon.style.transform = 'rotate(0deg)';
    } else {
        submenu.classList.add('show');
        dropdownIcon.style.transform = 'rotate(180deg)';
    }
}

// 内容显示函数
function showContent(type) {
    try {
        const contentTitle = document.getElementById('contentTitle');
        const contentBody = document.getElementById('contentBody');
        
        if (!contentTitle || !contentBody) {
            console.error('Required elements not found');
            return;
        }
        
        switch(type) {
            case 'userList':
                contentTitle.textContent = '用户管理';
                contentBody.innerHTML = `
                    <div class="content-wrapper">
                        <div class="toolbar">
                            <div class="search-box">
                                <input type="text" id="userSearchInput" class="search-input" placeholder="输入用户名搜索">
                                <button class="btn btn-primary" onclick="searchUsers()">
                                    <span class="menu-icon">🔍</span>搜索
                                </button>
                                <button class="btn btn-success" onclick="showAddUserForm()">
                                    <span class="menu-icon">➕</span>添加用户
                                </button>
                            </div>
                        </div>
                        <div class="table-container">
                            <table class="data-table" id="userTable">
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
                                    <tr><td colspan="7" style="text-align: center;">正在加载数据...</td></tr>
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
                            <div class="search-box">
                                <input type="text" id="adminSearchInput" class="search-input" placeholder="输入管理员名搜索">
                                <button class="btn btn-primary" onclick="searchAdmins()">
                                    <span class="menu-icon">🔍</span>搜索
                                </button>
                                <button class="btn btn-success" onclick="showAddAdminForm()">
                                    <span class="menu-icon">➕</span>添加管理员
                                </button>
                            </div>
                        </div>
                        <div class="table-container">
                            <table class="data-table" id="adminTable">
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
                                    <tr><td colspan="7" style="text-align: center;">正在加载数据...</td></tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                `;
                loadAdminList();
                break;
            default:
                contentTitle.textContent = '系统管理';
                contentBody.innerHTML = '<h3>欢迎使用 CloudCity 系统</h3>';
        }
    } catch (error) {
        console.error('Error in showContent:', error);
    }
}

// 页面初始化
document.addEventListener('DOMContentLoaded', function() {
    try {
        showContent('dashboard');
    } catch (error) {
        console.error('Error during initialization:', error);
    }
});

// 弹窗相关函数
function closeModal() {
    document.getElementById('userModal').style.display = 'none';
}

window.onclick = function(event) {
    const modal = document.getElementById('userModal');
    if (event.target == modal) {
        modal.style.display = 'none';
    }
}

// 加载管理员列表
function loadAdminList() {
    console.log('开始加载管理员列表');
    const tbody = document.querySelector('#adminTable tbody');
    tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">正在加载数据...</td></tr>';
    
    fetch('AdminManageServlet?action=list')
        .then(response => {
            console.log('Response status:', response.status);
            return response.text();
        })
        .then(text => {
            console.log('Raw response:', text);
            const admins = JSON.parse(text);
            console.log('Parsed admins:', admins);
            
            if (!Array.isArray(admins)) {
                throw new Error('返回的数据格式不正确');
            }
            
            if (admins.length === 0) {
                tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">没有找到管理员数据</td></tr>';
                return;
            }
            
            tbody.innerHTML = admins.map(admin => {
                const id = admin.id;
                const username = admin.username || '';
                const password = admin.password || '';
                const email = admin.email || '';
                const mobile = admin.mobile || '';
                const userType = admin.userType || '';
                
                return `
                    <tr>
                        <td style="color: #333;">${id}</td>
                        <td style="color: #333;">${username}</td>
                        <td style="color: #333;">${password}</td>
                        <td style="color: #333;">${email}</td>
                        <td style="color: #333;">${mobile}</td>
                        <td style="color: #333;">${userType}</td>
                        <td>
                            <div class="btn-group">
                                <button class="btn btn-primary" onclick="viewAdmin(${id})">
                                    <span class="menu-icon">👁️</span>查看
                                </button>
                                <button class="btn btn-warning" onclick="editAdmin(${id})">
                                    <span class="menu-icon">✏️</span>编辑
                                </button>
                                <button class="btn btn-danger" onclick="deleteAdmin(${id})">
                                    <span class="menu-icon">🗑️</span>删除
                                </button>
                            </div>
                        </td>
                    </tr>
                `;
            }).join('');
            
            console.log('管理员列表渲染完成');
        })
        .catch(error => {
            console.error('Error:', error);
            tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: red;">
                加载失败: ${error.message}</td></tr>`;
        });
} 