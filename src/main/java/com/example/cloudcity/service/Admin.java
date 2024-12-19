/**
 * 管理员实体类
 * 用途：封装管理员的基本信息，包括：
 * 1. 管理员的基本属性（ID、用户名、邮箱等）
 * 2. 管理员的在线状态
 * 3. 管理员的最后登录时间
 * 4. 提供属性的访问和修改方法
 */
package com.example.cloudcity.service;

import java.sql.Timestamp;

public class Admin {
    /** 管理员ID */
    private final Long id;
    /** 用户名 */
    private String username;
    /** 邮箱地址 */
    private String email;
    /** 手机号码 */
    private String mobile;
    /** 在线状态 */
    private boolean isOnline;
    /** 最后登录时间 */
    private Timestamp lastLoginTime;

    /**
     * 构造函数
     * @param id 管理员ID
     * @param username 用户名
     * @param email 邮箱地址
     * @param mobile 手机号码
     * @param isOnline 在线状态
     * @param lastLoginTime 最后登录时间
     */
    public Admin(Long id, String username, String email, String mobile, 
                boolean isOnline, Timestamp lastLoginTime) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.isOnline = isOnline;
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 获取管理员ID
     * @return 管理员ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 获取用户名
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     * @param username 新的用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取邮箱地址
     * @return 邮箱地址
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱地址
     * @param email 新的邮箱地址
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取手机号码
     * @return 手机号码
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号码
     * @param mobile 新的手机号码
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取在线状态
     * @return 在线状态
     */
    public boolean isOnline() {
        return isOnline;
    }

    /**
     * 设置在线状态
     * @param online 新的在线状态
     */
    public void setOnline(boolean online) {
        isOnline = online;
    }

    /**
     * 获取最后登录时间
     * @return 最后登录时间
     */
    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 设置最后登录时间
     * @param lastLoginTime 新的最后登录时间
     */
    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", online=" + isOnline +
                ", lastLoginTime=" + lastLoginTime +
                '}';
    }
}
