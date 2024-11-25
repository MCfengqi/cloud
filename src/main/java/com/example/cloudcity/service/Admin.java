/**
 * 管理员实体类
 * 用途：定义管理员对象的数据结构，包含：
 * 1. 管理员基本信息（ID、用户名）
 * 2. 管理员状态（在线状态）
 * 3. 登录时间记录
 * 4. 提供相关的getter和setter方法
 */
package com.example.cloudcity.service;

import java.sql.Timestamp;

public class Admin {
    private Long id;
    private String username;
    private String email;
    private String mobile;
    private boolean online;
    private Timestamp lastLoginTime;

    public Admin(Long id, String username, String email, String mobile, boolean online, Timestamp lastLoginTime) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.online = online;
        this.lastLoginTime = lastLoginTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", online=" + online +
                ", lastLoginTime=" + lastLoginTime +
                '}';
    }
}
