// 加载用户列表
function loadUserList() {
    console.log('开始加载用户列表');
    const tbody = document.querySelector('#userTable tbody');
    tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">正在加载数据...</td></tr>';
    
    fetch('UserManageServlet?action=list')
        .then(response => {
            console.log('Response status:', response.status);
            return response.text();
        })
        .then(text => {
            console.log('Raw response:', text);
            const users = JSON.parse(text);
            console.log('Parsed users:', users);
            
            if (!Array.isArray(users)) {
                throw new Error('返回的数据格式不正确');
            }
            
            if (users.length === 0) {
                tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">没有找到用户数据</td></tr>';
                return;
            }
            
            tbody.innerHTML = users.map(user => {
                const id = user.id;
                const username = user.username || '';
                const password = user.password || '';
                const email = user.email || '';
                const mobile = user.mobile || '';
                const userType = user.userType || '';
                
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
                                <button class="btn btn-primary" onclick="viewUser(${id})">
                                    <span class="menu-icon">👁️</span>查看
                                </button>
                                <button class="btn btn-warning" onclick="editUser(${id})">
                                    <span class="menu-icon">✏️</span>编辑
                                </button>
                                <button class="btn btn-danger" onclick="deleteUser(${id})">
                                    <span class="menu-icon">🗑️</span>删除
                                </button>
                            </div>
                        </td>
                    </tr>
                `;
            }).join('');
            
            console.log('用户列表渲染完成');
        })
        .catch(error => {
            console.error('Error:', error);
            tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: red;">
                加载失败: ${error.message}</td></tr>`;
        });
}

// 查看用户详情
function viewUser(id) {
    if (typeof id === 'undefined' || id === null) {
        console.error('Invalid user ID:', id);
        alert('无效的用户ID');
        return;
    }
    
    fetch(`UserManageServlet?action=get&id=${id}`)
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
        .then(user => {
            if (!user || user.error) {
                throw new Error(user.error || '获取用户信息失败');
            }
            
            const modalContent = document.getElementById('modalContent');
            modalContent.innerHTML = `
                <div class="view-container">
                    <h3>用户详情</h3>
                    <div class="detail-group">
                        <label>ID</label>
                        <div class="detail-value">${user.id}</div>
                    </div>
                    <div class="detail-group">
                        <label>用户名</label>
                        <div class="detail-value">${user.username}</div>
                    </div>
                    <div class="detail-group">
                        <label>密码</label>
                        <div class="detail-value">${user.password}</div>
                    </div>
                    <div class="detail-group">
                        <label>邮箱</label>
                        <div class="detail-value">${user.email}</div>
                    </div>
                    <div class="detail-group">
                        <label>手机号</label>
                        <div class="detail-value">${user.mobile}</div>
                    </div>
                    <div class="detail-group">
                        <label>用户类型</label>
                        <div class="detail-value">${user.userType}</div>
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
            alert('加载用户详情失败：' + error.message);
        });
}

// 编辑用户
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
                            <input type="text" value="${user.username}" disabled class="form-control">
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
                            <label>
                                <input type="checkbox" name="isAdmin" ${user.isAdmin ? 'checked' : ''} value="true"> 是否是管理员
                            </label>
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

// 删除用户
function deleteUser(id) {
    if (typeof id === 'undefined' || id === null) {
        console.error('Invalid user ID:', id);
        alert('无效的用户ID');
        return;
    }
    
    if (confirm('确定要删除这个用户吗？')) {
        fetch(`UserManageServlet?action=delete&id=${id}`, {
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
                loadUserList();
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

// 关闭弹窗
function closeModal() {
    document.getElementById('userModal').style.display = 'none';
}

// 更新用户信息
function updateUser(event, id) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    formData.append('id', id);
    formData.append('action', 'update');

    fetch('UserManageServlet', {
        method: 'POST',
        body: new URLSearchParams(formData)
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