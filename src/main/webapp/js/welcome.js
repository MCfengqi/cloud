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

async function showContent(contentType, event) {
    if (event) {
        event.preventDefault();
    }
    console.log('showContent called with type:', contentType);
    console.log('Event:', event);

    // 清除其他内容
    document.getElementById('contentBody').innerHTML = '';
    
    switch(contentType) {
        case 'contentManage':
            console.log('Loading content management...');
            if (typeof window.showGameContent === 'function') {
                window.showGameContent(contentType, event);
            }
            break;
        case 'userList':
            console.log('Loading user management...');
            if (typeof window.showUserContent === 'function') {
                window.showUserContent(contentType, event);
            }
            break;
        case 'adminList':
            console.log('Loading admin management...');
            if (typeof window.showAdminContent === 'function') {
                window.showAdminContent(contentType, event);
            }
            break;
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

// 将 showContent 函数导出到全局作用域
window.showContent = showContent;