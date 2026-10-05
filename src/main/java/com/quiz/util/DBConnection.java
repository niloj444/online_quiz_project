package com.quiz.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Single place that knows how to reach MySQL. Edit the three constants below. */
public final class DBConnection {
    private static final String DEFAULT_URL  = "jdbc:mysql://localhost:3306/quizdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "niloj@2026";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DBConnection() { }

    public static Connection get() throws SQLException {
        String url  = envOr("DB_URL", DEFAULT_URL);
        String user = envOr("DB_USER", DEFAULT_USER);
        String pass = envOr("DB_PASS", DEFAULT_PASS);
        return DriverManager.getConnection(url, user, pass);
    }

    private static String envOr(String key, String fallback) {
        String v = System.getenv(key);
        if (v == null || v.trim().isEmpty()) {
            v = System.getProperty(key);
        }
        return (v != null && !v.trim().isEmpty()) ? v.trim() : fallback;
    }
}
