# CloudCity 游戏管理系统
![image](https://github.com/user-attachments/assets/543056b1-7978-4403-bfe8-d531a78eacfa)
![image](https://github.com/user-attachments/assets/d5813c68-bef0-4d83-81cf-ae68a384a3a3)
![image](https://github.com/user-attachments/assets/381a70d4-6d89-4454-bbab-19821041ab87)

一个基于Java Web技术栈开发的游戏门户网站系统。

## 项目简介

云城游戏门户是一个现代化的游戏分发平台，提供游戏展示、购买、下载等功能。系统采用响应式设计，支持多终端访问。

## 技术栈

- **后端**：
  - Java 11
  - Jakarta Servlet 5.0
  - MySQL 8.0
  - Apache Commons DBUtils 1.7
  - Gson 2.8.9
  - SLF4J（日志框架）

- **前端**：
  - Bootstrap 5.3
  - HTML5/CSS3
  - JavaScript
  - jQuery

- **构建工具**：
  - Maven

## 功能特性

- 用户认证与授权
- 游戏展示与搜索
- 订单管理
- 游戏购买
- 操作日志记录
- 响应式界面设计

## 数据库设计

系统包含以下主要数据表：
- users：用户信息管理
- gamelist：游戏列表
- game_order：订单管理
- operation_logs：操作日志

## 数据库配置
- 数据库名：cloudcity
- 用户名：root
- 密码：123456
- 默认管理员账号：adminFQ
- 默认管理员密码：123456

## 项目结构
Cloud City Project

## 项目简介
Cloud City 是一个基于 Java Web 技术栈开发的游戏内容管理平台，提供游戏内容的展示、管理和用户系统功能。

## 技术栈
- **后端框架**: Jakarta Servlet 5.0
- **数据库**: MySQL 8.0
- **构建工具**: Maven
- **主要依赖**:
  - MySQL Connector (v8.0.27)
  - Apache Commons DBUtils (v1.7)
  - Gson (v2.8.9)
  - SLF4J (v1.7.32)

## 系统功能
### 用户管理系统
- 用户注册与登录
- 用户权限管理（管理员/普通用户）
- 用户信息管理（用户名、邮箱、手机号等）

### 游戏内容管理
- 游戏内容展示
- 游戏信息管理（名称、图片、简介、下载链接）
- 游戏内容CRUD操作

### 日志管理系统
- 系统操作日志记录
- 日志查询与管理
- 支持多维度日志分析

### 前台功能
1. **游戏展示**
   - 游戏列表展示
   - 游戏详情页面
   - 游戏价格显示
   - 游戏图片预览

2. **订单系统**
   - 游戏购买
   - 多种支付方式（支付宝/微信/QQ支付）
   - 订单状态管理
   - 订单历史查询

3. **用户功能**
   - 用户注册/登录
   - 个人订单管理
   - 个人信息修改

### 后台功能
1. **游戏管理**
   - 游戏内容管理（增删改查）
   - 游戏信息编辑
   - 游戏图片管理
   - 游戏价格设置

2. **订单管理**
   - 订单状态管理
   - 订单查询功能
   - 订单统计分析
   - 支付记录查看

3. **用户管理**
   - 用户列表管理
   - 用户权限控制
   - 用户状态管理

4. **系统管理**
   - 管理员账户管理
   - 系统日志查询
   - 操作记录追踪

## 数据库设计
### users 表
- 用户基本信息存储
- 包含字段：id, username, password, email, mobile, is_admin 等
- 支持用户权限区分

### gamelist 表
- 游戏内容信息存储
- 包含字段：gameid, gamename, gameimg, gametxt, gamelink 等
- 支持游戏内容的完整管理

### operation_logs 表
- 系统操作日志存储
- 主要字段：
  - `id`: 日志ID（主键，自增）
  - `operation_type`: 操作类型（如：用户登录、添加用户等）
  - `operation_content`: 操作内容详情
  - `username`: 操作用户名
  - `ip_address`: 操作IP地址
  - `created_at`: 操作时间
  - `result`: 操作结果（成功/失败）
- 索引设计：
  - 主键索引：id
  - 创建时间索引：idx_created_at
  - 用户名索引：idx_username
- 字符集：utf8mb4
- 存储引擎：InnoDB

### game_order 表
```sql
CREATE TABLE `game_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `total` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `amount` decimal(10, 2) NOT NULL COMMENT '实际支付金额',
  `status` varchar(50) NOT NULL COMMENT '订单状态',
  `paytype` varchar(50) NOT NULL COMMENT '支付方式',
  `gamename` varchar(100) NOT NULL COMMENT '游戏名',
  `gameimg` varchar(6535) NOT NULL COMMENT '游戏照片',
  `gametxt` text NOT NULL COMMENT '游戏介绍',
  `datetime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏订单表';
```

## 开发环境要求
- JDK 11 或以上
- MySQL 8.0 或以上
- Maven 3.x
- Tomcat 9.x 或以上

## 安装说明

1. 克隆项目到本地：
```bash
git clone [项目地址]
```

2. 导入数据库：
```bash
mysql -u your_username -p your_database < sql/cloudcity.sql
```

3. 修改数据库配置：
配置文件位于 `src/main/resources/` 目录下

4. 编译打包：
```bash
mvn clean package
```

5. 部署war包到Tomcat

## 项目结构

```
cloud1/
├── src/
│   ├── main/
│   │   ├── java/         # Java源代码
│   │   ├── resources/    # 配置文件
│   │   └── webapp/       # Web资源
├── sql/                  # 数据库脚本
├── pom.xml              # Maven配置
└── README.md
```

## 开发团队
- **党林龙**
  - 角色：前端开发工程师
  - 职责：负责项目的前端开发和UI设计
  - 联系方式：qxsj@vip.qq.com

- **杜旭晨**
  - 角色：后端开发工程师
  - 职责：负责项目的后端开发和数据库设计
  - 联系方式：qxsj@vip.qq.com

## 部署说明
1. **环境要求**
   - JDK 17.0.9 LTS
   - Tomcat 10.0.23
   - MySQL 8.0.35
   - Maven 3.x

2. **配置步骤**
   - 导入数据库脚本
   - 修改数据库连接配置
   - 编译打包项目
   - 部署到Tomcat服务器

3. **访问地址**
   - 前台访问：http://localhost:8080/cloud1/
   - 后台访问：http://localhost:8080/cloud1/login.jsp
   - 默认管理员账号：adminFQ
   - 默认管理员密码：123456

## 开发环境介绍

### 开发工具
1. **IDE选择**
   - IntelliJ IDEA 2023.2 旗舰版
   - Visual Studio Code 1.84.0（前端代码编辑）
   - Navicat Premium 16（数据库管理）

2. **开发环境**
   - 操作系统：Windows 11 专业版
   - JDK版本：Java 17.0.9 LTS
   - 服务器：Apache Tomcat 10.1.16
   - 数据库：MySQL 8.0.35 Community

### 项目依赖
1. **后端依赖**
   ```xml
   <!-- Jakarta Servlet API -->
   <dependency>
       <groupId>jakarta.servlet</groupId>
       <artifactId>jakarta.servlet-api</artifactId>
       <version>5.0.0</version>
   </dependency>

   <!-- MySQL Connector -->
   <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
       <version>8.0.27</version>
   </dependency>

   <!-- JSON处理 -->
   <dependency>
       <groupId>com.google.code.gson</groupId>
       <artifactId>gson</artifactId>
       <version>2.8.9</version>
   </dependency>
   ```

2. **前端技术**
   - HTML5
   - CSS3
   - JavaScript ES6+
   - Bootstrap 5.3.0
   - jQuery 3.7.1

### 开发规范
1. **代码规范**
   - 遵循阿里巴巴Java开发手册
   - ESLint标准的JavaScript代码规范
   - 统一的代码注释规范

2. **版本控制**
   - Git 2.42.0
   - GitHub/Gitee托管
   - 采用Git Flow工作流

### 项目结构
```
cloud1/
├── .git/                      # Git版本控制目录
├── .gitignore                 # Git忽略文件配置
├── .idea/                     # IDE配置文件
├── .mvn/                      # Maven包装器配置
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── cloudcity/
│       │               ├── dao/           # 数据访问层
│       │               ├── filter/        # 过滤器
│       │               ├── model/         # 数据模型
│       │               ├── service/       # 业务逻辑层
│       │               ├── servlet/       # Servlet控制器
│       │               └── utils/         # 工具类
│       ├── resources/                     # 配置文件
│       └── webapp/
│           ├── WEB-INF/                   # Web应用配置
│           ├── css/                       # 样式文件
│           │   └── *.css
│           ├── js/                        # JavaScript文件
│           │   └── *.js
│           ├── images/                    # 图片资源
│           ├── index.jsp                  # 首页
│           ├── login.jsp                  # 登录页
│           ├── register.jsp               # 注册页
│           ├── about.jsp                  # 关于页面
│           ├── dingdan.jsp                # 订单页面
│           ├── xiangqing.jsp              # 详情页面
│           ├── welcome.jsp                # 欢迎页面
│           ├── logout.jsp                 # 登出处理
│           └── error.jsp                  # 错误页面
├── sql/                                   # SQL脚本文件
│   ├── cloudcity.sql                      # 主数据库脚本
│   ├── game_order.sql                     # 订单表脚本
│   ├── gamelist.sql                       # 游戏列表脚本
│   ├── operation_logs.sql                 # 操作日志脚本
│   └── users.sql                          # 用户表脚本
├── pom.xml                                # Maven配置文件
├── mvnw                                   # Maven包装器脚本(Unix)
├── mvnw.cmd                               # Maven包装器脚本(Windows)
├── README.md                              # 项目说明文档
└── target/                                # 编译输出目录
```

### 开发流程
1. **环境搭建**
   - JDK安装配置
   - Maven环境配置
   - Tomcat服务器配置
   - MySQL数据库安装

2. **配置说明**
   - 服务器端口：8080
   - 数据库端口：3306
   - 数据库名：cloudcity
   - 字符集：UTF-8

3. **部署要求**
   - 最低JDK版本：JDK 11+
   - 最低内存要求：4GB RAM
   - 推荐系统：Windows 10/11
   - 浏览器要求：支持ES6的现代浏览器

## 更新日志
最后更新时间：2024-12-16

## 开发团队
- 管理员联系邮箱：qxsj@vip.qq.com

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
2. 管理员登录（默认账号：adminFQ/123456）
3. 根据权限使用相应功能
4. 安全退出系统

## 注意事项
- 确保数据库配置正确
- 定期备份重要数据
- 遵循安全使用规范

## 更新日志

### 2024-12-12
1. 日志管理系统实现
   - 新增日志记录功能（LogUtils）
     - 支持记录操作类型、操作内容、操作人、IP地址等信息
     - 实现了多级IP地址获取逻辑，确保准确记录用户IP
     - 集成数据库连接池，提升日志写入性能
   
   - 日志查询功能（LogManageServlet）
     - 支持按时间范围筛选日志
     - 支持按操作类型、操作内容、用户名等关键词搜索
     - 实现分页查询，优化大数据量展示
     - 提供日志删除功能

2. 系统操作日志完善
   - 用户登录登出日志
     - 记录用户登录时间和IP
     - 记录用户退出系统时间
   
   - 用户管理日志
     - 记录用户添加操作
     - 记录用户信息修改
     - 记录用户删除操作
   
   - 管理员操作日志
     - 记录管理员添加操作
     - 记录管理员信息更新
     - 记录管理员删除操作
   
   - 游戏内容管理日志
     - 记录游戏添加操作
     - 记录游戏信息更新
     - 记录游戏删除操作

3. 日志记录规范化
   - 统一日志格式
     - 操作类型：使用中文描述（如"添加用户"、"更新游戏"等）
     - 操作内容：详细记录操作对象和关键信息
     - 操作结果：统一使用"成功"/"失败"标识
   
   - 异常处理完善
     - 记录操作失败原因
     - 保存详细错误信息
     - 便于问题追踪和分析

4. 系统安全性增强
   - 实现日志访问权限控制
   - 防止SQL注入攻击
   - 保护敏感信息不被泄露


### 2024-11-26
1. 用户管理功能优化
   - 修复了用户编辑时的更新失败问题
   - 改进了密码显示方式，从隐藏改为明文显示
   - 优化了时间字段的显示格式
   - 完善了用户信息的更新逻辑

2. 管理员管理功能改进
   - 修复了管理员列表时间显示问题
   - 统一了管理员和用户的信息展示方式
   - 优化了管理员信息的编辑界面
   - 改进了数据验证和错误处理

3. 界面交互优化
   - 统一了用户和管理员列表的显示风格
   - 优化了表格中的时间格式显示
   - 改进了编辑表单的布局和样式
   - 增强了用户操作的反馈效果

4. 代码结构优化
   - 统一了前端代码的错误处理方式
   - 改进了日期格式化函数的实现
   - 优化了数据验证和提交逻辑
   - 完善了代码注释和文档

### 2024-11-25
1. 管理员管理模块优化
   - 修复了时间显示问题，将last_login_time字段改为使用updated_at
   - 优化了在线状态的判断逻辑，基于updated_at时间判断
   - 完善了管理员信息的显示和日志记录
   - 改进了管理员列表的数据查询SQL语句

2. 界面交互优化
   - 优化了管理员列表的显示效果
   - 改进了时间格式的显示
   - 完善了在线状态的显示逻辑
   - 添加了更详细的调试日志输出

3. 代码结构优化
   - 优化了AdminService类的代码结构
   - 添加了更详细的错误处理和日志记录
   - 改进了数据库查询的性能
   - 完善了代码注释和文档

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


## 内容管理系统说明

### 后端实现 (GameManageServlet)
1. **核心功能实现**
   - 基于Java Servlet技术
   - RESTful风格API设计
   - 完整的CRUD操作支持
   - 数据库连接池优化

2. **API接口设计**
   - GET 接口：
     - `/game?action=list`：获取游戏列表
     - `/game?action=get`：获取单个游戏详情
   - POST 接口：
     - `/game?action=add`：添加新游戏
     - `/game?action=update`：更新游戏信息
     - `/game?action=delete`：删除游戏

3. **数据处理**
   - JSON数据解析与生成
   - 参数化SQL查询
   - 事务管理
   - 异常处理机制

4. **安全特性**
   - SQL注入防护
   - XSS攻击防护
   - 权限验证
   - 输入验证

### 前端实现 (gameManage.js)
1. **界面组件**
   - 响应式数据表格
   - 模态框表单
   - 图片预览
   - 操作按钮组

2. **交互功能**
   - 游戏列表动态加载
   - 添加/编辑表单验证
   - 图片URL验证
   - 实时数据更新

3. **用户体验**
   - 加载状态提示
   - 操作成功/失败反馈
   - 表单数据验证
   - 图片预加载

4. **样式设计**
   - 模块化CSS
   - 响应式布局
   - 主题定制
   - 动画效果

### 数据流程
1. **数据录入流程**
   - 表单数据收集
   - 前端数据验证
   - API请求发送
   - 后端数据处理
   - 数据库持久化
   - 结果反馈

2. **数据展示流程**
   - 页面初始化
   - 数据请求
   - 数据解析
   - 动态渲染
   - 用户交互
   - 状态更新

3. **数据更新流程**
   - 修改触发
   - 数据验证
   - 后端更新
   - 界面刷新
   - 状态同步

### 技术特点
1. **模块化设计**
   - 前后端分离
   - 组件化开发
   - 代码复用
   - 维护性优化

2. **性能优化**
   - 连接池管理
   - 图片预加载
   - 异步数据处理
   - 缓存机制

3. **安全机制**
   - 数据验证
   - 权限控制
   - 攻击防护
   - 错误处理

4. **可扩展性**
   - 插件化架构
   - 配置化管理
   - 接口标准化
   - 版本控制

## 内容管理系统演讲大纲

### 1. 系统概述（1分钟）
- "今天我为大家介绍CloudCity游戏管理系统的内容管理模块"
- 强调这是一个基于Java Web的完整解决方案
- 简述系统的主要功能：游戏内容的展示和管理

### 2. 技术架构（2分钟）
- **前端技术**
  - "前端我们采用了原生JavaScript"
  - "使用模块化的方式组织代码"
  - "实现了响应式的用户界面"

- **后端技术**
  - "后端基于Java Servlet技术"
  - "采用RESTful风格API设计"
  - "使用MySQL数据库存储"

### 3. 核心功能演示（3分钟）
- **展示界面**
  - 打开系统管理界面
  - 展示游戏列表
  - 演示响应式布局

- **功能操作**
  - 添加新游戏
  - 修改游戏信息
  - 删除游戏记录

### 4. 技术亮点（2分钟）
- **安全性**
  - "我们实现了完整的安全防护机制"
  - "包括SQL注入防护和XSS攻击防护"

- **性能优化**
  - "使用数据库连接池提升性能"
  - "实现了图片预加载功能"
  - "采用异步处理提升响应速度"

### 5. 项目特色（1分钟）
- **用户体验**
  - "简洁直观的操作界面"
  - "及时的操作反馈"
  - "完善的错误提示"

- **可扩展性**
  - "模块化的系统设计"
  - "标准化的接口规范"
  - "便于后续功能扩展"

### 演讲技巧
1. **开场**
   - 简短的自我介绍
   - 用一句话概括系统特点
   - 提出本次演示的重点

2. **演示过程**
   - 边演示边解说
   - 突出系统的实用性
   - 适时强调技术亮点

3. **结束语**
   - 总结系统优势
   - 展望未来发展
   - 感谢聆听

### 注意事项
- 控制时间在8-10分钟
- 语速要适中，重点要突出
- 演示要流畅，提前做好准备
- 准备应对可能的问题

## 内容管理系统演讲稿

"各位老师、同学们好！

今天我要向大家介绍的是我们CloudCity游戏管理系统中的内容管理模块。这个模块是我在开发过程中投入最多精力的部分，它不仅实现了游戏内容的完整管理功能，更融入了许多现代化的技术理念。

首先从技术架构来说，我们采用了Java Web技术栈。后端使用Java Servlet处理请求，选择MySQL作为数据库。这样的技术选择既保证了系统的稳定性，又便于后期维护和扩展。前端我采用了原生JavaScript开发，实现了模块化的代码组织和响应式的用户界面，确保了良好的用户体验。

接下来我给大家演示一下核心功能。首先是游戏列表的展示界面，您可以看到，这里采用了清晰的表格布局，展示了游戏的基本信息。我们可以进行添加、修改、删除等操作。比如，我现在添加一个新游戏，您可以看到表单验证和图片预览功能，这些细节都是为了提升用户体验。

在技术实现上，我特别注重了几个关键点：首先是安全性，实现了完整的SQL注入防护和XSS攻击防护；其次是性能优化，使用了数据库连接池和图片预加载技术；最后是可扩展性，采用了模块化设计和标准化接口。

这个系统目前已经完成了基础功能的开发和测试，后续我计划添加更多功能，比如游戏分类管理、用户评分系统等，进一步提升系统的实用性。

以上就是我的介绍，感谢大家的聆听！如果有任何问题，我很乐意为大家解答。"

### 补充说明要点：
1. 如果被问到技术选择：强调Java Web技术栈的成熟稳定性和广泛应用
2. 如果被问到开发难点：可以谈论前后端数据交互和图片处理的优化
3. 如果被问到未来规划：可以详细展开游戏分类、评分系统的设计构想

## 项目特色
1. **用户体验**
   - 响应式设计，适配多种设备
   - 直观的操作界面
   - 实时的操作反馈
   - 友好的错误提示

2. **安全性**
   - 用户权限控制
   - 数据加密存储
   - SQL注入防护
   - XSS攻击防护

3. **可维护性**
   - 模块化的代码结构
   - 完善的注释文档
   - 统一的编码规范
   - 便捷的部署方案

4. **扩展性**
   - 插件化架构设计
   - 接口标准化
   - 配置外部化
   - 易于功能扩展

## 开发团队
- **党林龙**
  - 角色：前端开发工程师
  - 职责：负责项目的前端开发和UI设计
  - 联系方式：qxsj@vip.qq.com

- **杜旭晨**
  - 角色：后端开发工程师
  - 职责：负责项目的后端开发和数据库设计
  - 联系方式：qxsj@vip.qq.com

## 部署说明
1. **环境要求**
   - JDK 17.0.9 LTS
   - Tomcat 10.0.23
   - MySQL 8.0.35
   - Maven 3.x

2. **配置步骤**
   - 导入数据库脚本
   - 修改数据库连接配置
   - 编译打包项目
   - 部署到Tomcat服务器

3. **访问地址**
   - 前台访问：http://localhost:8080/cloud1/
   - 后台访问：http://localhost:8080/cloud1/login.jsp
   - 默认管理员账号：adminFQ
   - 默认管理员密码：123456

## 开发环境介绍

### 开发工具
1. **IDE选择**
   - IntelliJ IDEA 2023.2 旗舰版
   - Visual Studio Code 1.84.0（前端代码编辑）
   - Navicat Premium 16（数据库管理）

2. **开发环境**
   - 操作系统：Windows 11 专业版
   - JDK版本：Java 17.0.9 LTS
   - 服务器：Apache Tomcat 10.1.16
   - 数据库：MySQL 8.0.35 Community

### 项目依赖
1. **后端依赖**
   ```xml
   <!-- Jakarta Servlet API -->
   <dependency>
       <groupId>jakarta.servlet</groupId>
       <artifactId>jakarta.servlet-api</artifactId>
       <version>5.0.0</version>
   </dependency>

   <!-- MySQL Connector -->
   <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
       <version>8.0.27</version>
   </dependency>

   <!-- JSON处理 -->
   <dependency>
       <groupId>com.google.code.gson</groupId>
       <artifactId>gson</artifactId>
       <version>2.8.9</version>
   </dependency>
   ```

2. **前端技术**
   - HTML5
   - CSS3
   - JavaScript ES6+
   - Bootstrap 5.3.0
   - jQuery 3.7.1

### 开发规范
1. **代码规范**
   - 遵循阿里巴巴Java开发手册
   - ESLint标准的JavaScript代码规范
   - 统一的代码注释规范

2. **版本控制**
   - Git 2.42.0
   - GitHub/Gitee托管
   - 采用Git Flow工作流

### 项目结构
```
cloud1/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/cloudcity/
│       │       ├── servlet/    # Servlet控制器
│       │       ├── model/      # 数据模型
│       │       ├── utils/      # 工具类
│       │       └── service/    # 业务逻辑
│       ├── resources/          # 配置文件
│       └── webapp/
│           ├── WEB-INF/
│           ├── css/
│           ├── js/
│           └── images/
├── pom.xml                     # Maven配置
└── README.md                   # 项目文档
