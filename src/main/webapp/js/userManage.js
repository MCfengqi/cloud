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

// 加载用户列表
function loadUserList() {
    console.log('Loading user list...');
    const tbody = document.querySelector('#userTable tbody');
    
    fetch('UserManageServlet?action=list')
        .then(response => {
            console.log('Response status:', response.status);
            return response.json();
        })
        .then(users => {
            console.log('Received users:', users);
            if (users.length === 0) {
                tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">没有用户数据</td></tr>';
                return;
            }
            
            tbody.innerHTML = users.map(user => `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.password}</td>
                    <td>${user.email || ''}</td>
                    <td>${user.mobile || ''}</td>
                    <td>${user.isAdmin ? '管理员' : '普通用户'}</td>
                    <td>
                        <div class="btn-group">
                            <button class="btn btn-warning" onclick="editUser(${user.id})">
                                <span class="menu-icon">✏️</span>编辑
                            </button>
                            <button class="btn btn-danger" onclick="deleteUser(${user.id})">
                                <span class="menu-icon">🗑️</span>删除
                            </button>
                        </div>
                    </td>
                </tr>
            `).join('');
        })
        .catch(error => {
            console.error('Error:', error);
            tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: red;">
                加载失败: ${error.message}</td></tr>`;
        });
}

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
        id: id,
        action: 'update',
        username: formData.get('username'),
        password: formData.get('password'),
        email: formData.get('email'),
        mobile: formData.get('mobile'),
        isAdmin: formData.get('userType') === '1'
    };
    
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
        isAdmin: formData.get('userType') === '1'
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