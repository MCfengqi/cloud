/**
 * 游戏管理Servlet
 * 用途：处理游戏相关的所有后端请求，包括：
 * 1. 获取游戏列表
 * 2. 添加新游戏
 * 3. 更新游戏信息
 * 4. 删除游戏
 * 5. 处理游戏图片上传
 */
package com.example.cloudcity.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet; // 导入HttpServlet类
import jakarta.servlet.http.HttpServletRequest; // 导入HttpServletRequest类
import jakarta.servlet.http.HttpServletResponse; // 导入HttpServletResponse类
import com.google.gson.Gson; // 导入Gson类用于JSON处理
import com.google.gson.JsonObject; // 导入JsonObject类用于处理JSON对象

import java.io.*; // 导入输入输出流相关类
import java.sql.*; // 导入SQL相关类
import java.util.*; // 导入集合框架相关类

public class GameManageServlet extends HttpServlet { // 定义GameManageServlet类继承自HttpServlet
    // 数据库连接信息
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cloudcity"; // 数据库URL
    private static final String USER = "cloudcity"; // 数据库用户名
    private static final String PASS = "cloudcity"; // 数据库密码

    // 创建Gson对象用于JSON处理
    private final Gson gson = new Gson(); // 创建Gson实例

    /**
     * 处理GET请求
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应类型为JSON
        response.setContentType("application/json;charset=UTF-8"); // 设置响应内容类型为JSON
        String action = request.getParameter("action"); // 获取请求中的action参数

        try {
            // 根据action参数执行相应操作
            switch (action) {
                case "list":
                    listGames(response); // 调用listGames方法获取游戏列表
                    break;
                case "get":
                    getGame(request, response); // 调用getGame方法获取单个游戏信息
                    break;
                default:
                    sendError(response, "Unknown action: " + action); // 发送错误响应
            }
        } catch (Exception e) {
            e.printStackTrace(); // 打印堆栈跟踪信息
            sendError(response, e.getMessage()); // 发送错误响应
        }
    }

    /**
     * 处理POST请求
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8"); // 设置响应内容类型为JSON

        try {
            // 读取JSON数据
            StringBuilder sb = new StringBuilder(); // 创建StringBuilder对象用于存储请求体内容
            try (BufferedReader reader = request.getReader()) { // 获取请求体的BufferedReader
                String line;
                while ((line = reader.readLine()) != null) { // 逐行读取请求体内容
                    sb.append(line); // 将每一行内容追加到StringBuilder中
                }
            }

            // 解析JSON数据
            JsonObject jsonData = gson.fromJson(sb.toString(), JsonObject.class); // 使用Gson解析JSON字符串为JsonObject
            String action = jsonData.get("action").getAsString(); // 获取action参数

            // 根据action执行相应操作
            switch (action) {
                case "add":
                    addGame(jsonData, response); // 调用addGame方法添加新游戏
                    break;
                case "update":
                    updateGame(jsonData, response); // 调用updateGame方法更新游戏信息
                    break;
                case "delete":
                    deleteGame(jsonData, response); // 调用deleteGame方法删除游戏
                    break;
                default:
                    sendError(response, "Unknown action: " + action); // 发送错误响应
            }
        } catch (Exception e) {
            e.printStackTrace(); // 打印堆栈跟踪信息
            sendError(response, e.getMessage()); // 发送错误响应
        }
    }

    /**
     * 获取游戏列表
     * @param response HTTP响应对象
     */
    private void listGames(HttpServletResponse response) throws SQLException, IOException {
        List<Map<String, Object>> games = new ArrayList<>(); // 创建List存储游戏信息

        // 查询所有游戏信息
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); // 获取数据库连接
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM gamelist ORDER BY created_at DESC"); // 准备SQL查询语句
             ResultSet rs = stmt.executeQuery()) { // 执行查询并获取结果集

            while (rs.next()) { // 遍历结果集中的每一行
                // 将每个游戏信息存入Map
                Map<String, Object> game = new HashMap<>(); // 创建Map存储单个游戏信息
                game.put("gameid", rs.getLong("gameid")); // 存储游戏ID
                game.put("gamename", rs.getString("gamename")); // 存储游戏名称
                game.put("gameimg", rs.getString("gameimg")); // 存储游戏图片URL
                game.put("gametxt", rs.getString("gametxt")); // 存储游戏描述
                game.put("gamelink", rs.getString("gamelink")); // 存储游戏链接
                game.put("created_at", rs.getTimestamp("created_at")); // 存储创建时间
                game.put("updated_at", rs.getTimestamp("updated_at")); // 存储更新时间
                games.add(game); // 将游戏信息添加到List中
            }
        }

        // 添加调试日志
        System.out.println("Games data: " + new Gson().toJson(games)); // 打印游戏数据到控制台

        // 返回游戏列表JSON数据
        response.getWriter().write(new Gson().toJson(games)); // 将游戏列表转换为JSON并写入响应
    }

    /**
     * 获取单个游戏信息
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     */
    private void getGame(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        long gameId = Long.parseLong(request.getParameter("id")); // 获取请求中的游戏ID

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); // 获取数据库连接
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM gamelist WHERE gameid = ?")) { // 准备SQL查询语句

            stmt.setLong(1, gameId); // 设置SQL查询参数
            ResultSet rs = stmt.executeQuery(); // 执行查询并获取结果集

            if (rs.next()) { // 检查结果集中是否有数据
                // 构建游戏信息对象
                Map<String, Object> game = new HashMap<>(); // 创建Map存储单个游戏信息
                game.put("gameid", rs.getLong("gameid")); // 存储游戏ID
                game.put("gamename", rs.getString("gamename")); // 存储游戏名称
                game.put("gameimg", rs.getString("gameimg")); // 存储游戏图片URL
                game.put("gametxt", rs.getString("gametxt")); // 存储游戏描述
                game.put("gamelink", rs.getString("gamelink")); // 存储游戏链接
                game.put("created_at", rs.getTimestamp("created_at")); // 存储创建时间
                game.put("updated_at", rs.getTimestamp("updated_at")); // 存储更新时间

                new Gson().toJson(game, response.getWriter()); // 将游戏信息转换为JSON并写入响应
            } else {
                sendError(response, "Game not found"); // 发送错误响应
            }
        }
    }

    /**
     * 添加新游戏
     * @param jsonData 游戏数据JSON对象
     * @param response HTTP响应对象
     */
    private void addGame(JsonObject jsonData, HttpServletResponse response)
            throws SQLException, IOException {
        String gamename = jsonData.get("gamename").getAsString(); // 获取游戏名称
        String gameimg = jsonData.get("gameimg").getAsString(); // 获取游戏图片URL
        String gametxt = jsonData.get("gametxt").getAsString(); // 获取游戏描述
        String gamelink = jsonData.get("gamelink").getAsString(); // 获取游戏链接

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); // 获取数据库连接
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO gamelist (gamename, gameimg, gametxt, gamelink, created_at, updated_at) " +
                             "VALUES (?, ?, ?, ?, NOW(), NOW())")) { // 准备SQL插入语句

            stmt.setString(1, gamename); // 设置SQL插入参数
            stmt.setString(2, gameimg); // 设置SQL插入参数
            stmt.setString(3, gametxt); // 设置SQL插入参数
            stmt.setString(4, gamelink); // 设置SQL插入参数

            int result = stmt.executeUpdate(); // 执行插入操作并获取受影响的行数
            sendSuccess(response, result > 0); // 发送成功响应
        }
    }

    /**
     * 更新游戏信息
     * @param jsonData 游戏数据JSON对象
     * @param response HTTP响应对象
     */
    private void updateGame(JsonObject jsonData, HttpServletResponse response)
            throws SQLException, IOException {
        long gameId = jsonData.get("id").getAsLong(); // 获取游戏ID
        String gamename = jsonData.get("gamename").getAsString(); // 获取游戏名称
        String gameimg = jsonData.get("gameimg").getAsString(); // 获取游戏图片URL
        String gametxt = jsonData.get("gametxt").getAsString(); // 获取游戏描述
        String gamelink = jsonData.get("gamelink").getAsString(); // 获取游戏链接

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); // 获取数据库连接
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE gamelist SET gamename = ?, gameimg = ?, gametxt = ?, " +
                             "gamelink = ?, updated_at = NOW() WHERE gameid = ?")) { // 准备SQL更新语句

            stmt.setString(1, gamename); // 设置SQL更新参数
            stmt.setString(2, gameimg); // 设置SQL更新参数
            stmt.setString(3, gametxt); // 设置SQL更新参数
            stmt.setString(4, gamelink); // 设置SQL更新参数
            stmt.setLong(5, gameId); // 设置SQL更新参数

            int result = stmt.executeUpdate(); // 执行更新操作并获取受影响的行数
            sendSuccess(response, result > 0); // 发送成功响应
        }
    }

    /**
     * 删除游戏
     * @param jsonData 游戏数据JSON对象
     * @param response HTTP响应对象
     */
    private void deleteGame(JsonObject jsonData, HttpServletResponse response)
            throws SQLException, IOException {
        long gameId = jsonData.get("id").getAsLong(); // 获取游戏ID

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); // 获取数据库连接
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM gamelist WHERE gameid = ?")) { // 准备SQL删除语句

            stmt.setLong(1, gameId); // 设置SQL删除参数
            int result = stmt.executeUpdate(); // 执行删除操作并获取受影响的行数

            JsonObject response_data = new JsonObject(); // 创建JsonObject存储响应数据
            if (result > 0) {
                response_data.addProperty("success", true); // 设置成功标志
                response_data.addProperty("message", "游戏删除成功"); // 设置成功消息
            } else {
                response_data.addProperty("success", false); // 设置失败标志
                response_data.addProperty("error", "未找到要删除的游戏"); // 设置错误消息
            }
            response.getWriter().write(gson.toJson(response_data)); // 将响应数据转换为JSON并写入响应
        } catch (SQLException e) {
            JsonObject error_response = new JsonObject(); // 创建JsonObject存储错误响应数据
            error_response.addProperty("success", false); // 设置失败标志
            error_response.addProperty("error", "删除游戏时发生错误: " + e.getMessage()); // 设置错误消息
            response.getWriter().write(gson.toJson(error_response)); // 将错误响应数据转换为JSON并写入响应
        }
    }

    /**
     * 发送错误响应
     * @param response HTTP响应对象
     * @param message 错误信息
     */
    private void sendError(HttpServletResponse response, String message) throws IOException {
        JsonObject json = new JsonObject(); // 创建JsonObject存储错误响应数据
        json.addProperty("success", false); // 设置失败标志
        json.addProperty("error", message); // 设置错误消息
        response.getWriter().write(json.toString()); // 将错误响应数据转换为JSON并写入响应
    }

    /**
     * 发送成功响应
     * @param response HTTP响应对象
     * @param success 是否成功
     */
    private void sendSuccess(HttpServletResponse response, boolean success) throws IOException {
        JsonObject json = new JsonObject(); // 创建JsonObject存储成功响应数据
        json.addProperty("success", success); // 设置成功标志
        response.getWriter().write(json.toString()); // 将成功响应数据转换为JSON并写入响应
    }
}