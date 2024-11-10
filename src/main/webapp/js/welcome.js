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

document.addEventListener('DOMContentLoaded', function() {
    try {
        showContent('dashboard');
    } catch (error) {
        console.error('Error during initialization:', error);
    }
});

function closeModal() {
    document.getElementById('userModal').style.display = 'none';
}

window.onclick = function(event) {
    const modal = document.getElementById('userModal');
    if (event.target == modal) {
        modal.style.display = 'none';
    }
}

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

function searchUsers() {
    const searchInput = document.getElementById('userSearchInput');
    const searchTerm = searchInput.value.trim();
    
    fetch(`UserManageServlet?action=list&search=${encodeURIComponent(searchTerm)}`)
        .then(response => response.text())
        .then(text => {
            console.log('Search response:', text);
            loadUserList();  // 重新加载用户列表
        })
        .catch(error => {
            console.error('Search error:', error);
            alert('搜索失败：' + error.message);
        });
}

function showAddUserForm() {
    const modalContent = document.getElementById('modalContent');
    modalContent.innerHTML = `
        <div class="add-container">
            <h3>添加用户</h3>
            <form id="addUserForm" onsubmit="addUser(event)">
                <div class="form-group">
                    <label>用户名</label>
                    <input type="text" name="username" required class="form-control">
                </div>
                <div class="form-group">
                    <label>密码</label>
                    <input type="password" name="password" required class="form-control">
                </div>
                <div class="form-group">
                    <label>邮箱</label>
                    <input type="email" name="email" required class="form-control">
                </div>
                <div class="form-group">
                    <label>手机号</label>
                    <input type="text" name="mobile" required class="form-control" pattern="^1[3-9]\\d{9}$">
                </div>
                <div class="button-group">
                    <button type="submit" class="btn btn-primary">添加</button>
                    <button type="button" class="btn btn-secondary" onclick="closeModal()">取消</button>
                </div>
            </form>
        </div>
    `;
    document.getElementById('userModal').style.display = 'block';
}

function addUser(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    formData.append('action', 'add');

    fetch('UserManageServlet', {
        method: 'POST',
        body: new URLSearchParams(formData)
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            alert('添加成功！');
            closeModal();
            loadUserList();
        } else {
            alert(result.error || '添加失败！');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('系统错误，请稍后重试！');
    });
}

function searchAdmins() {
    const searchInput = document.getElementById('adminSearchInput');
    const searchTerm = searchInput.value.trim();
    
    fetch(`AdminManageServlet?action=list&search=${encodeURIComponent(searchTerm)}`)
        .then(response => response.text())
        .then(text => {
            console.log('Search response:', text);
            loadAdminList();  // 重新加载管理员列表
        })
        .catch(error => {
            console.error('Search error:', error);
            alert('搜索失败：' + error.message);
        });
}

function showAddAdminForm() {
    const modalContent = document.getElementById('modalContent');
    modalContent.innerHTML = `
        <div class="add-container">
            <h3>添加管理员</h3>
            <form id="addAdminForm" onsubmit="addAdmin(event)">
                <div class="form-group">
                    <label>用户名</label>
                    <input type="text" name="username" required class="form-control">
                </div>
                <div class="form-group">
                    <label>密码</label>
                    <input type="password" name="password" required class="form-control">
                </div>
                <div class="form-group">
                    <label>邮箱</label>
                    <input type="email" name="email" required class="form-control">
                </div>
                <div class="form-group">
                    <label>手机号</label>
                    <input type="text" name="mobile" required class="form-control" pattern="^1[3-9]\\d{9}$">
                </div>
                <div class="button-group">
                    <button type="submit" class="btn btn-primary">添加</button>
                    <button type="button" class="btn btn-secondary" onclick="closeModal()">取消</button>
                </div>
            </form>
        </div>
    `;
    document.getElementById('userModal').style.display = 'block';
}

function addAdmin(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    formData.append('action', 'add');

    fetch('AdminManageServlet', {
        method: 'POST',
        body: new URLSearchParams(formData)
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            alert('添加成功！');
            closeModal();
            loadAdminList();
        } else {
            alert(result.error || '添加失败！');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('系统错误，请稍后重试！');
    });
}

function editUser(id) {
    if (!id) {
        console.error('Invalid user ID:', id);
        alert('无效的用户ID');
        return;
    }
    
    fetch(`UserManageServlet?action=get&id=${id}`)
        .then(response => response.text())
        .then(text => {
            console.log('Raw response:', text);
            const user = JSON.parse(text);
            
            const modalContent = document.getElementById('modalContent');
            modalContent.innerHTML = `
                <div class="edit-container">
                    <h3>编辑用户</h3>
                    <form id="editUserForm" onsubmit="updateUser(event, ${user.id})">
                        <div class="form-group">
                            <label>用户名</label>
                            <input type="text" name="username" value="${user.username}" required class="form-control">
                        </div>
                        <div class="form-group">
                            <label>新密码（不修改请留空）</label>
                            <input type="password" name="password" class="form-control" placeholder="输入新密码">
                        </div>
                        <div class="form-group">
                            <label>邮箱</label>
                            <input type="email" name="email" value="${user.email}" required class="form-control">
                        </div>
                        <div class="form-group">
                            <label>手机号</label>
                            <input type="text" name="mobile" value="${user.mobile}" required class="form-control" pattern="^1[3-9]\\d{9}$">
                        </div>
                        <div class="form-group">
                            <label>用户类型</label>
                            <select name="userType" class="form-control">
                                <option value="0" ${!user.isAdmin ? 'selected' : ''}>普通用户</option>
                                <option value="1" ${user.isAdmin ? 'selected' : ''}>超级管理员</option>
                            </select>
                        </div>
                        <div class="button-group">
                            <button type="submit" class="btn btn-primary">保存</button>
                            <button type="button" class="btn btn-secondary" onclick="closeModal()">取消</button>
                        </div>
                    </form>
                </div>
            `;
            document.getElementById('userModal').style.display = 'block';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('加载用户信息失败');
        });
}

function viewAdmin(id) {
    if (!id) {
        console.error('Invalid admin ID:', id);
        alert('无效的管理员ID');
        return;
    }
    
    fetch(`AdminManageServlet?action=get&id=${id}`)
        .then(response => response.text())
        .then(text => {
            console.log('View response:', text);
            try {
                return JSON.parse(text);
            } catch (e) {
                console.error('JSON parse error:', e);
                throw new Error('服务器响应格式错误');
            }
        })
        .then(admin => {
            if (!admin || admin.error) {
                throw new Error(admin.error || '获取管理员信息失败');
            }
            
            const modalContent = document.getElementById('modalContent');
            modalContent.innerHTML = `
                <div class="view-container">
                    <h3>管理员详情</h3>
                    <div class="detail-group">
                        <label>ID</label>
                        <div class="detail-value">${admin.id}</div>
                    </div>
                    <div class="detail-group">
                        <label>用户名</label>
                        <div class="detail-value">${admin.username}</div>
                    </div>
                    <div class="detail-group">
                        <label>邮箱</label>
                        <div class="detail-value">${admin.email}</div>
                    </div>
                    <div class="detail-group">
                        <label>手机号</label>
                        <div class="detail-value">${admin.mobile}</div>
                    </div>
                    <div class="detail-group">
                        <label>用户类型</label>
                        <div class="detail-value">${admin.userType || '超级管理员'}</div>
                    </div>
                    <div class="button-group">
                        <button class="btn btn-secondary" onclick="closeModal()">关闭</button>
                    </div>
                </div>
            `;
            document.getElementById('userModal').style.display = 'block';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('加载管理员详情失败：' + error.message);
        });
}

function editAdmin(id) {
    if (!id) {
        console.error('Invalid admin ID:', id);
        alert('无效的管理员ID');
        return;
    }
    
    fetch(`AdminManageServlet?action=get&id=${id}`)
        .then(response => response.text())
        .then(text => {
            console.log('Raw response:', text);
            const admin = JSON.parse(text);
            
            const modalContent = document.getElementById('modalContent');
            modalContent.innerHTML = `
                <div class="edit-container">
                    <h3>编辑管理员</h3>
                    <form id="editAdminForm" onsubmit="updateAdmin(event, ${admin.id})">
                        <div class="form-group">
                            <label>用户名</label>
                            <input type="text" name="username" value="${admin.username}" required class="form-control">
                        </div>
                        <div class="form-group">
                            <label>新密码（不修改请留空）</label>
                            <input type="password" name="password" class="form-control" placeholder="输入新密码">
                        </div>
                        <div class="form-group">
                            <label>邮箱</label>
                            <input type="email" name="email" value="${admin.email}" required class="form-control">
                        </div>
                        <div class="form-group">
                            <label>手机号</label>
                            <input type="text" name="mobile" value="${admin.mobile}" required class="form-control" pattern="^1[3-9]\\d{9}$">
                        </div>
                        <div class="form-group">
                            <label>用户类型</label>
                            <select name="userType" class="form-control">
                                <option value="0" ${!admin.isAdmin ? 'selected' : ''}>普通用户</option>
                                <option value="1" ${admin.isAdmin ? 'selected' : ''}>超级管理员</option>
                            </select>
                        </div>
                        <div class="button-group">
                            <button type="submit" class="btn btn-primary">保存</button>
                            <button type="button" class="btn btn-secondary" onclick="closeModal()">取消</button>
                        </div>
                    </form>
                </div>
            `;
            document.getElementById('userModal').style.display = 'block';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('加载管理员信息失败');
        });
}

function deleteAdmin(id) {
    if (!id) {
        console.error('Invalid admin ID:', id);
        alert('无效的管理员ID');
        return;
    }
    
    if (confirm('确定要删除这个管理员吗？')) {
        fetch('AdminManageServlet?action=delete&id=' + id, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        })
        .then(response => response.text())
        .then(text => {
            console.log('Delete response:', text);
            try {
                return JSON.parse(text);
            } catch (e) {
                console.error('JSON parse error:', e);
                throw new Error('服务器响应格式错误');
            }
        })
        .then(result => {
            if (result.success) {
                alert('删除成功！');
                loadAdminList();
            } else {
                alert(result.error || '删除失败！');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('操作失败：' + error.message);
        });
    }
}

function updateAdmin(event, id) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    
    const userType = formData.get('userType');
    console.log('Selected user type:', userType);
    
    const data = {
        id: id,
        action: 'update',
        email: formData.get('email'),
        mobile: formData.get('mobile'),
        password: formData.get('password'),
        isAdmin: userType === '1'
    };
    
    console.log('Sending data:', data);

    fetch('AdminManageServlet', {
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
            loadAdminList();
        } else {
            alert(result.error || '更新失败！');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('系统错误，请稍后重试！');
    });
}

function updateUser(event, id) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    
    const userType = formData.get('userType');
    console.log('Selected user type:', userType);
    
    const data = {
        id: id,
        action: 'update',
        email: formData.get('email'),
        mobile: formData.get('mobile'),
        password: formData.get('password'),
        isAdmin: userType === '1'
    };
    
    console.log('Sending data:', data);

    fetch('UserManageServlet', {
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
            loadUserList();
        } else {
            alert(result.error || '更新失败！');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('系统错误，请稍后重试！');
    });
}