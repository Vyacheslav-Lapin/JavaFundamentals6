package com.epam.trainings.jf.web.controllers;

import lombok.experimental.var;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/form-parse")
public class FormParseController extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        var login = req.getParameter("login");
        var password = req.getParameter("password");

        try (PrintWriter out = resp.getWriter()) {
            out.printf("Your login: %s<br />Your password: %s", login, password);
        }

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
