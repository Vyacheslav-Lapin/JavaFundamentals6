package com.epam.trainings.jf.web.controllers;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/StandaloneController")
public class StandaloneController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        resp.setStatus(408);
        resp.setContentType("text/html");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><head>"
                    + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">"
                    + "<title>Title</title>"
                    + "</head><body>"
                    + "<h1>Hello, world!!!</h1>"
                    + "</body></html>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
