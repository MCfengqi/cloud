function loadAdminList() {
    const tbody = document.querySelector('#adminTable tbody');
    
    fetch('AdminListServlet')
        .then(response => response.json())
        .then(admins => {
            if (admins.length === 0) {
                tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">暂无管理员数据</td></tr>';
                return;
            }
            
            tbody.innerHTML = admins.map(admin => `
                <tr>
                    <td>${admin.id}</td>
                    <td>${admin.username}</td>
                    <td>${admin.email || ''}</td>
                    <td>${admin.mobile || ''}</td>
                    <td>${admin.online ? '在线' : '离线'}</td>
                    <td>${admin.lastLoginTime || ''}</td>
                    <td>
                        <div class="btn-group">
                            <button class="btn btn-warning" onclick="editAdmin(${admin.id})">
                                <span class="menu-icon">✏️</span>编辑
                            </button>
                            <button class="btn btn-danger" onclick="deleteAdmin(${admin.id})">
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

function addAdmin() {
    const content = `
        <h3>添加管理员</h3>
        <form id="addAdminForm" onsubmit="submitAddAdmin(event)">
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
                    <option value="1" selected>管理员</option>
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

function editAdmin(id) {
    fetch(`AdminListServlet?action=get&id=${id}`)
        .then(response => response.json())
        .then(admin => {
            const content = `
                <h3>编辑管理员</h3>
                <form id="editAdminForm" onsubmit="submitEditAdmin(event, ${id})">
                    <div class="form-group">
                        <label>用户名：</label>
                        <input type="text" name="username" value="${admin.username}" required>
                    </div>
                    <div class="form-group">
                        <label>邮箱：</label>
                        <input type="email" name="email" value="${admin.email}" required>
                    </div>
                    <div class="form-group">
                        <label>手机号：</label>
                        <input type="text" name="mobile" value="${admin.mobile}" required>
                    </div>
                    <div class="form-group">
                        <label>用户类型：</label>
                        <select name="userType">
                            <option value="0">普通用户</option>
                            <option value="1" selected>管理员</option>
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
            alert('加载管理员信息失败');
        });
}

function deleteAdmin(id) {
    if (confirm('确定要删除这个管理员吗？')) {
        fetch('AdminListServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                action: 'delete',
                id: id
            })
        })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                showContent('adminList');
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

function searchAdmins() {
    const searchText = document.getElementById('adminSearchInput').value;
    // 实现搜索逻辑...
}

function submitAddAdmin(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    
    const data = {
        action: 'add',
        username: formData.get('username'),
        password: formData.get('password'),
        email: formData.get('email'),
        mobile: formData.get('mobile'),
        isAdmin: true
    };
    
    fetch('AdminListServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            alert('添加成功！');
            closeModal();
            showContent('adminList');
        } else {
            alert('添加失败：' + (result.error || '未知错误'));
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('系统错误：' + error.message);
    });
}

function submitEditAdmin(event, id) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    
    const data = {
        action: 'update',
        id: id,
        username: formData.get('username'),
        email: formData.get('email'),
        mobile: formData.get('mobile')
    };
    
    fetch('AdminListServlet', {
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
            showContent('adminList');
        } else {
            alert('更新失败：' + (result.error || '未知错误'));
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('系统错误：' + error.message);
    });
} 