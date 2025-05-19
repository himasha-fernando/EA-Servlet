package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/stockManagement")
public class StockManagementServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Check for theme preference
        Cookie[] cookies = request.getCookies();
        String theme = "light"; // default
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("theme".equals(cookie.getName())) {
                    theme = cookie.getValue();
                    break;
                }
            }
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<body style='background-color:" + ("dark".equals(theme) ? "#333; color:white;" : "#fff") + "'>");
        out.println("<h2>Stock List (Theme: " + theme + ")</h2>");
        out.println("<a href='addStock.html'>Add New Stock</a><br><br>");
        try (Connection conn = DatabaseUtil.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM stock");

            out.println("<table border='1'><tr><th>Product</th><th>Qty</th><th>Price</th><th>Actions</th></tr>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getString("product_name") + "</td>"
                          + "<td>" + rs.getInt("quantity") + "</td>"
                          + "<td>" + rs.getDouble("price") + "</td>"
                          + "<td><a href='updateStock?id=" + rs.getInt("id") + "'>Update</a> | "
                          + "<a href='deleteStock?id=" + rs.getInt("id") + "'>Delete</a></td></tr>");
            }
            out.println("</table>");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Error loading stock.");
        }
    }
}