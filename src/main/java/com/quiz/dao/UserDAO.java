package com.quiz.dao;

import com.quiz.model.User;
import com.quiz.util.DBConnection;
import java.sql.*;

public class UserDAO {

    /** @return the user, or null when the email is not registered. */
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT id, name, email, password_hash, role FROM users WHERE email = ?";
        try (Connection con = DBConnection.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setRole(rs.getString("role"));
                return u;
            }
        }
    }

    /** @return false when the email is already taken. */
    public boolean create(String name, String email, String passwordHash) throws SQLException {
        return create(name, email, passwordHash, "USER");
    }

    /** Creates a student or teacher account. ADMIN is intentionally not self-assignable. */
    public boolean create(String name, String email, String passwordHash, String role) throws SQLException {
        String sql = "INSERT INTO users (name, email, password_hash, role) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, passwordHash);
            ps.setString(4, "TEACHER".equals(role) ? "TEACHER" : "USER");
            ps.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException duplicate) {
            return false;
        }
    }
}
