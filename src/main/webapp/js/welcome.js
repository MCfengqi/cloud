// 添加全局的 showContent 函数
window.showContent = function(contentType, event) {
    if (event) {
        event.stopPropagation();
    }
    
    console.log('Showing content:', contentType);

    // 等待所有脚本加载完成
    setTimeout(() => {
        // 根据不同的内容类型显示不同的内容
        switch(contentType) {
            case 'adminList':
                if (typeof window.showAdminContent === 'function') {
                    window.showAdminContent('adminList', event);
                } else {
                    console.error('showAdminContent is not loaded');
                }
                break;
            case 'userList':
                if (typeof window.showUserContent === 'function') {
                    window.showUserContent('userList', event);
                } else {
                    console.error('showUserContent is not loaded');
                }
                break;
            case 'logList':
                if (typeof window.showLogContent === 'function') {
                    window.showLogContent('logList', event);
                } else {
                    console.error('showLogContent is not loaded');
                }
                break;
            case 'contentManage':
                if (typeof window.showGameContent === 'function') {
                    window.showGameContent('contentManage', event);
                } else {
                    console.error('showGameContent is not loaded');
                }
                break;
            case 'orderManage':
                if (typeof window.showOrderContent === 'function') {
                    window.showOrderContent('orderManage', event);
                } else {
                    console.error('showOrderContent is not loaded');
                }
                break;
            default:
                console.log('Unknown content type:', contentType);
        }
    }, 0);
};

// 添加子菜单切换函数
function toggleSubmenu(submenuId) {
    const submenu = document.getElementById(submenuId);
    if (submenu) {
        submenu.classList.toggle('active');
        const parentWrapper = submenu.previousElementSibling;
        if (parentWrapper) {
            parentWrapper.classList.toggle('active');
        }
    }
}

// 导出 toggleSubmenu 到全局
window.toggleSubmenu = toggleSubmenu;

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

// 将 showContent 函数导出到全局作用域
window.showContent = showContent;