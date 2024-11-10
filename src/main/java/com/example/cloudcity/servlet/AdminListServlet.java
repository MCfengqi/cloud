package com.example.cloudcity.servlet;


import com.example.cloudcity.service.Admin;
import com.example.cloudcity.service.AdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminListServlet", value = "/AdminListServlet")
public class AdminListServlet extends HttpServlet {
    private static final AdminService adminService = AdminService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Admin> adminList = adminService.getOnlineAdmins();
        request.setAttribute("adminList", adminList);
        request.getRequestDispatcher("welcome.jsp").forward(request, response);
    }
}
