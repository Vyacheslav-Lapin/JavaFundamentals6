package com.epam.trainings.jf.web.controllers;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = {"/hello", "/mama"},
        initParams = @WebInitParam(name = "kjhsgdfag", value = "873645345"))
//@WebServlet("/hello")
@Log4j2
public class HWController extends HttpServlet {

    private int kjhsgdfag;

    @Override
    public void init(ServletConfig config) throws ServletException {
        kjhsgdfag = Integer.parseInt(
                config.getInitParameter("kjhsgdfag"));
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest();
    }

    private void processRequest() {
        log.info("Hello, World! + {}", kjhsgdfag);
    }

    @Override
    public void destroy() {
        log.info("Пока!");
    }
}
