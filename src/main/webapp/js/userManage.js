function addUser() {
    const content = `
        <h3>添加用户</h3>
        <form id="addUserForm" onsubmit="submitAddUser(event)">
            <div class="form-group">
                <label>用户名：</label>
                <input type="text" name="username" required>
            </div>
            <div class="form-group">
                <label>密码：</label>
                <input type="password" name="password" required>
            </div>
            <div class="form-group">
                <label>邮箱：</label>
                <input type="email" name="email" required>
            </div>
            <div class="form-group">
                <label>手机号：</label>
                <input type="text" name="mobile" required>
            </div>
            <div class="form-group">
                <label>用户类型：</label>
                <select name="userType">
                    <option value="0">普通用户</option>
                    <option value="1">管理员</option>
                </select>
            </div>
            <div class="button-group">
                <button type="submit" class="btn btn-primary">保存</button>
                <button type="button" class="btn btn-secondary" onclick="closeModal()">取消</button>
            </div>
        </form>
    `;
    openModal(content);
}

function editUser(id) {
    fetch(`UserManageServlet?action=get&id=${id}`)
        .then(response => response.json())
        .then(user => {
            const content = `
                <h3>编辑用户</h3>
                <form id="editUserForm" onsubmit="updateUser(event, ${id})">
                    <div class="form-group">
                        <label>用户名：</label>
                        <input type="text" name="username" value="${user.username}" required>
                    </div>
                    <div class="form-group">
                        <label>新密码：</label>
                        <input type="password" name="password" placeholder="留空表示不修改">
                    </div>
                    <div class="form-group">
                        <label>邮箱：</label>
                        <input type="email" name="email" value="${user.email}" required>
                    </div>
                    <div class="form-group">
                        <label>手机号：</label>
                        <input type="text" name="mobile" value="${user.mobile}" required>
                    </div>
                    <div class="form-group">
                        <label>用户类型：</label>
                        <select name="userType">
                            <option value="0" ${!user.isAdmin ? 'selected' : ''}>普通用户</option>
                            <option value="1" ${user.isAdmin ? 'selected' : ''}>管理员</option>
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
            alert('加载用户信息失败');
        });
}

// 将showContent改名为showUserContent并导出到全局
window.showUserContent = function(contentType, event) {
    if (contentType === 'userList') {
        const content = `
            <div class="toolbar-container">
                <div class="button-container">
                    <button class="btn btn-primary" onclick="window.addUser()">
                        <span class="menu-icon">➕</span>添加用户
                    </button>
                </div>
            </div>
            <table class="table" id="userTable">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>用户名</th>
                        <th>密码</th>
                        <th>邮箱</th>
                        <th>手机号</th>
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
        document.getElementById('contentTitle').textContent = '用户管理';
        window.loadUserList();
    }
};

// 加载用户列表
window.loadUserList = function() {
    fetch('UserManageServlet?action=list')
        .then(response => response.json())
        .then(users => {
            console.log('Received users data:', users); // 添加调试日志
            const tbody = document.querySelector('#userTable tbody');
            if (users.length === 0) {
                tbody.innerHTML = '<tr><td colspan="8" style="text-align: center;">没有用户数据</td></tr>';
                return;
            }
            
            tbody.innerHTML = users.map(user => {
                console.log('Processing user:', user); // 添加调试日志
                return `
                    <tr>
                        <td>${user.id || ''}</td>
                        <td>${user.username || ''}</td>
                        <td>${user.password || ''}</td>
                        <td>${user.email || ''}</td>
                        <td>${user.mobile || ''}</td>
                        <td>${user.created_at ? formatDate(user.created_at) : ''}</td>
                        <td>${user.updated_at ? formatDate(user.updated_at) : ''}</td>
                        <td>
                            <div class="btn-group">
                                <button onclick="editUser(${user.id})" class="btn btn-warning">
                                    <span class="menu-icon">✏️</span>编辑
                                </button>
                                <button onclick="deleteUser(${user.id})" class="btn btn-danger">
                                    <span class="menu-icon">🗑️</span>删除
                                </button>
                            </div>
                        </td>
                    </tr>
                `;
            }).join('');
        })
        .catch(error => {
            console.error('Error:', error);
            const tbody = document.querySelector('#userTable tbody');
            tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; color: red;">加载失败</td></tr>';
        });
};

// 搜索用户
function searchUsers() {
    const searchText = document.getElementById('userSearchInput').value.toLowerCase();
    const tbody = document.querySelector('#userTable tbody');
    const rows = tbody.getElementsByTagName('tr');
    
    for (let row of rows) {
        const cells = row.getElementsByTagName('td');
        let found = false;
        
        for (let cell of cells) {
            if (cell.textContent.toLowerCase().includes(searchText)) {
                found = true;
                break;
            }
        }
        
        row.style.display = found ? '' : 'none';
    }
}

// 删除用户
function deleteUser(id) {
    if (confirm('确定要删除这个用户吗？')) {
        fetch('UserManageServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `action=delete&id=${id}`
        })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                loadUserList();  // 重新加载用户列表
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

// 更新用户信息
function updateUser(event, id) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    
    const data = {
        action: 'update',
        id: id,
        username: formData.get('username'),
        password: formData.get('password'),
        email: formData.get('email'),
        mobile: formData.get('mobile'),
        is_admin: formData.get('userType') === '1'
    };
    
    // 如果密码为空，则不更新密码
    if (!data.password) {
        delete data.password;
    }
    
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
            alert('更新失败：' + (result.error || '未知错误'));
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('系统错误：' + error.message);
    });
}

// 提交添加用户表单
function submitAddUser(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    
    // 获取并验证表单数据
    const username = formData.get('username').trim();
    const password = formData.get('password').trim();
    const email = formData.get('email').trim();
    const mobile = formData.get('mobile').trim();
    const isAdmin = formData.get('userType') === '1';
    
    if (!username || !password || !email || !mobile) {
        alert('请填写所有必填字段');
        return;
    }
    
    const data = {
        action: 'add',
        username: username,
        password: password,
        email: email,
        mobile: mobile,
        is_admin: isAdmin
    };
    
    console.log('Submitting user data:', data);
    
    fetch('UserManageServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(result => {
        if (result.success) {
            alert('添加成功！');
            closeModal();
            loadUserList();
        } else {
            alert('添加失败：' + (result.error || '未知错误'));
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('系统错误：' + error.message);
    });
}

// 确保所有函数都导出到全局作用域
window.addUser = addUser;
window.editUser = editUser;
window.deleteUser = deleteUser;
window.updateUser = updateUser;
window.submitAddUser = submitAddUser;
window.searchUsers = searchUsers;

// 添加日期格式化函数
function formatDate(dateString) {
    if (!dateString) return '';
    try {
        // 尝试解析日期字符串
        const date = new Date(dateString);
        
        // 检查日期是否有效
        if (isNaN(date.getTime())) {
            console.warn('Invalid date:', dateString);
            return dateString;
        }
        
        // 格式化日期
        return new Intl.DateTimeFormat('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: false
        }).format(date);
    } catch (e) {
        console.error('Date formatting error:', e);
        return dateString;
    }
}

// 导出日期格式化函数到全局
window.formatDate = formatDate; 