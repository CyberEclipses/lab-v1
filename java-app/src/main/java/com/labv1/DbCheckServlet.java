package com.labv1;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbCheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dbHost = getenvOrDefault("DB_HOST", "mysql");
        String dbPort = getenvOrDefault("DB_PORT", "3306");
        String dbName = getenvOrDefault("DB_NAME", "labapp");
        String dbUser = getenvOrDefault("DB_USER", "labuser");
        String dbPassword = getenvOrDefault("DB_PASSWORD", "labpass123");

        String jdbcUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

        response.setContentType("application/json");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (
                Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT service_name, status FROM health_check LIMIT 1")
            ) {
                if (resultSet.next()) {
                    response.setStatus(HttpServletResponse.SC_OK);

                    PrintWriter out = response.getWriter();
                    out.println("{");
                    out.println("  \"service\": \"labv1-java-war\",");
                    out.println("  \"database\": \"mysql\",");
                    out.println("  \"db_connection\": \"ok\",");
                    out.println("  \"service_name\": \"" + resultSet.getString("service_name") + "\",");
                    out.println("  \"status\": \"" + resultSet.getString("status") + "\"");
                    out.println("}");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().println("{\"db_connection\":\"failed\",\"reason\":\"no rows returned\"}");
                }
            }

        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            PrintWriter out = response.getWriter();
            out.println("{");
            out.println("  \"service\": \"labv1-java-war\",");
            out.println("  \"database\": \"mysql\",");
            out.println("  \"db_connection\": \"failed\",");
            out.println("  \"error\": \"" + escapeJson(exception.getMessage()) + "\"");
            out.println("}");
        }
    }

    private String getenvOrDefault(String key, String fallback) {
        String value = System.getenv(key);
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value;
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}