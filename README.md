# CloudCity 游戏管理系统

## 项目简介
CloudCity 是一个基于 Java Web 技术栈开发的游戏管理系统，使用 Servlet 处理 HTTP 请求，MySQL 作为数据存储。

## 技术栈
- Java Servlet
- MySQL
- JSP
- JDBC
- JavaScript
- CSS3
- HTML5

## 功能特性
1. **用户管理**
   - 普通用户和管理员分离管理
   - 普通用户列表显示和管理
   - 管理员列表显示和管理
   - 用户信息的增删改查
   - 用户类型区分（普通用户/超级管理员）

2. **权限控制**
   - 基于 Session 的登录验证
   - 管理员特权功能
   - 访问权限控制

3. **界面功能**
   - 响应式布局设计
   - 动态菜单展开/收起
   - 模态框显示详细信息
   - 用户友好的操作界面

4. **数据管理**
   - 用户数据的实时更新
   - 最后登录时间记录
   - 数据安全性保护

## 数据库配置
- 数据库名：cloudcity
- 用户名：cloudcity
- 密码：cloudcity
- 默认管理员账号：admin
- 默认管理员密码：admin123

## 项目结构
CloudCity/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── cloudcity/
│       │               └── servlet/
│       │                   ├── UserLoginServlet.java     // 用户登录处理
│       │                   ├── UserRegisterServlet.java  // 用户注册处理
│       │                   ├── UserManageServlet.java    // 用户管理处理
│       │                   └── AdminManageServlet.java   // 管理员管理处理
│       ├── resources/
│       │   └── db/
│       │       └── init.sql                 // 数据库初始化脚本
│       └── webapp/
│           ├── css/
│           │   └── welcome.css              // 主样式文件
│           ├── js/
│           │   ├── welcome.js               // 主要功能脚本
│           │   └── userManage.js            // 用户管理脚本
│           ├── WEB-INF/
│           │   └── web.xml                  // Web应用配置文件
│           ├── index.jsp                    // 首页
│           ├── login.jsp                    // 登录页
│           ├── register.jsp                 // 注册页
│           ├── welcome.jsp                  // 主界面
│           ├── logout.jsp                   // 登出处理
│           └── error.jsp                    // 错误页面
├── pom.xml                                  // Maven 配置文件
└── README.md                                // 项目说明文档

## 文件说明
1. **Servlet 文件**
   - `UserLoginServlet.java`: 处理用户登录请求
   - `UserRegisterServlet.java`: 处理用户注册请求
   - `UserManageServlet.java`: 处理普通用户的管理
   - `AdminManageServlet.java`: 处理管理员的管理

2. **JSP 页面**
   - `index.jsp`: 系统首页
   - `login.jsp`: 用户登录页面
   - `register.jsp`: 用户注册页面
   - `welcome.jsp`: 登录后的主界面
   - `error.jsp`: 错误信息显示页面
   - `logout.jsp`: 处理用户登出

3. **静态资源**
   - `css/welcome.css`: 主要样式定义
   - `js/welcome.js`: 主要功能脚本
   - `js/userManage.js`: 用户管理相关脚本

4. **配置文件**
   - `pom.xml`: Maven 项目配置
   - `web.xml`: Web 应用配置
   - `init.sql`: 数据库初始化脚本

## 数据库结构
`users` 表结构：
- `id`: 用户ID（主键）
- `username`: 用户名（唯一）
- `password`: 密码
- `email`: 电子邮箱
- `mobile`: 手机号码
- `is_admin`: 是否是管理员（0-普通用户，1-管理员）
- `created_at`: 创建时间
- `updated_at`: 最后登录时间

## 安装部署
1. 创建 MySQL 数据库并执行 init.sql 脚本
2. 确保数据库配置正确：
   - 数据库名：cloudcity
   - 用户名：cloudcity
   - 密码：cloudcity
3. 部署到 Tomcat 服务器
4. 访问系统首页

## 使用说明
1. 普通用户注册/登录
2. 管理员登录（默认账号：admin/admin123）
3. 根据权限使用相应功能
4. 安全退出系统

## 注意事项
- 确保数据库配置正确
- 定期备份重要数据
- 遵循安全使用规范

## 更新日志

### 2024-11-10
1. 系统界面优化
   - 修改系统标题为"云城游戏管理系统"
   - 优化了系统 logo 和标题的显示效果
   - 统一了菜单图标的样式和布局
   - 改进了退出登录按钮的位置和样式

2. 用户管理功能增强
   - 实现了用户类型的动态切换功能
   - 优化了用户编辑界面的布局和交互
   - 改进了用户信息的更新逻辑
   - 增强了用户数据的验证机制

3. 管理员管理功能优化
   - 添加了管理员类型的编辑功能
   - 实现了最后一个管理员的保护机制
   - 优化了管理员列表的显示效果
   - 改进了管理员信息的更新逻辑

4. 代码结构优化
   - 分离了 CSS 样式到独立文件
   - 分离了 JavaScript 代码到独立文件
   - 优化了代码组织结构
   - 改进了代码可维护性

5. 系统功能改进
   - 优化了错误处理机制
   - 添加了详细的日志输出
   - 改进了数据验证逻辑
   - 增强了系统稳定性

### 2024-11-09
1. 初始功能实现
   - 用户登录和注册功能
   - 基本的用户管理功能
   - 管理员权限控制
   - 系统基础界面
