package com.example;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/displayProducts")
public class DisplayProductsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Connect to DB
        try (Connection conn = getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM stock");

            // Start HTML and table
            out.println("<html><head><title>Product List</title></head><body>");
            out.println("<h1>Product List from Database</h1>");
            out.println("<table border='1' cellpadding='10'>");
            out.println("<tr><th>ID</th><th>Product Name</th><th>Quantity</th></tr>");

            // Loop through results and print rows
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("product_name");
                int qty = rs.getInt("quantity");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + name + "</td>");
                out.println("<td>" + qty + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h2>Database Error: " + e.getMessage() + "</h2>");
        }
    }

    // DB connection method (same as before)
    private Connection getConnection() throws SQLException {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver"); //  Load MySQL Driver class
    } catch (ClassNotFoundException e) {
        throw new SQLException("MySQL JDBC Driver not found!", e);
    }

    String url = "jdbc:mysql://localhost:3306/stock_management?useSSL=false&serverTimezone=UTC";
    String username = "root";
    String password = ""; // change as per your DB password
    return DriverManager.getConnection(url, username, password);
    }}
