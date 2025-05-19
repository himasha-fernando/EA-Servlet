package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM users WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);

                Cookie userCookie = new Cookie("username", username);
                userCookie.setMaxAge(60 * 60 * 24 * 7);
                response.addCookie(userCookie);

                // Theme preference
                Cookie themeCookie = new Cookie("theme", "dark");
                themeCookie.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(themeCookie);

                response.sendRedirect("stockManagement");
            } else {
                response.getWriter().println("Invalid credentials.");
            }
        }  catch (SQLException e) {
    e.printStackTrace();
    response.setContentType("text/plain");
    response.getWriter().println("Database error: " + e.getMessage());
}
    }
}