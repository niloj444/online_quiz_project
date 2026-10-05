package com.quiz.dao;

import com.quiz.model.Question;
import com.quiz.util.DBConnection;
import java.sql.*;
import java.util.*;

/** CRUD and queries for questions by topic/category. */
public class QuestionDAO {

    private static final String COLS =
        "id, question_text, option_a, option_b, option_c, option_d, correct_option, category";

    // ---------- Read ----------
    public List<Question> findAll() throws SQLException {
        return query("SELECT " + COLS + " FROM questions ORDER BY category ASC, id DESC", null);
    }

    public List<Question> findByCategory(String category) throws SQLException {
        if (category == null || category.trim().isEmpty() || "all".equalsIgnoreCase(category.trim())) {
            return findAll();
        }
        return query("SELECT " + COLS + " FROM questions WHERE category = ? ORDER BY id DESC",
                ps -> ps.setString(1, category.trim()));
    }

    public List<Question> findRandom(int limit) throws SQLException {
        return query("SELECT " + COLS + " FROM questions ORDER BY RAND() LIMIT ?",
                ps -> ps.setInt(1, limit));
    }

    public List<Question> findRandomByCategory(String category, int limit) throws SQLException {
        if (category == null || category.trim().isEmpty() || "all".equalsIgnoreCase(category.trim())) {
            return findRandom(limit);
        }
        return query("SELECT " + COLS + " FROM questions WHERE category = ? ORDER BY RAND() LIMIT ?", ps -> {
            ps.setString(1, category.trim());
            ps.setInt(2, limit);
        });
    }

    public List<Question> findByIds(List<Integer> ids) throws SQLException {
        if (ids.isEmpty()) return new ArrayList<>();
        String marks = String.join(",", Collections.nCopies(ids.size(), "?"));
        return query("SELECT " + COLS + " FROM questions WHERE id IN (" + marks + ")", ps -> {
            for (int i = 0; i < ids.size(); i++) ps.setInt(i + 1, ids.get(i));
        });
    }

    public Question findById(int id) throws SQLException {
        List<Question> list = query("SELECT " + COLS + " FROM questions WHERE id = ?", ps -> ps.setInt(1, id));
        return list.isEmpty() ? null : list.get(0);
    }

    public List<String> findAllCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM questions WHERE category IS NOT NULL AND category != '' ORDER BY category ASC";
        try (Connection con = DBConnection.get();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        }
        return categories;
    }

    public Map<String, Integer> getCategoryCounts() throws SQLException {
        Map<String, Integer> counts = new LinkedHashMap<>();
        String sql = "SELECT category, COUNT(*) as cnt FROM questions GROUP BY category ORDER BY category ASC";
        try (Connection con = DBConnection.get();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                counts.put(rs.getString("category"), rs.getInt("cnt"));
            }
        }
        return counts;
    }

    public int getTotalQuestionsCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM questions";
        try (Connection con = DBConnection.get();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ---------- Create ----------
    public void insert(Question q) throws SQLException {
        String sql = "INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option, category) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.get(); PreparedStatement ps = con.prepareStatement(sql)) {
            fill(ps, q);
            ps.executeUpdate();
        }
    }

    // ---------- Update ----------
    public void update(Question q) throws SQLException {
        String sql = "UPDATE questions SET question_text=?, option_a=?, option_b=?, option_c=?, option_d=?, "
                   + "correct_option=?, category=? WHERE id=?";
        try (Connection con = DBConnection.get(); PreparedStatement ps = con.prepareStatement(sql)) {
            fill(ps, q);
            ps.setInt(8, q.getId());
            ps.executeUpdate();
        }
    }

    // ---------- Delete ----------
    public void delete(int id) throws SQLException {
        try (Connection con = DBConnection.get();
             PreparedStatement ps = con.prepareStatement("DELETE FROM questions WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ---------- helpers ----------
    private interface Binder { void bind(PreparedStatement ps) throws SQLException; }

    private List<Question> query(String sql, Binder binder) throws SQLException {
        List<Question> out = new ArrayList<>();
        try (Connection con = DBConnection.get(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (binder != null) binder.bind(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        }
        return out;
    }

    private void fill(PreparedStatement ps, Question q) throws SQLException {
        ps.setString(1, q.getQuestionText());
        ps.setString(2, q.getOptionA());
        ps.setString(3, q.getOptionB());
        ps.setString(4, q.getOptionC());
        ps.setString(5, q.getOptionD());
        ps.setString(6, q.getCorrectOption());
        String cat = q.getCategory();
        ps.setString(7, (cat == null || cat.trim().isEmpty()) ? "General" : cat.trim());
    }

    private Question map(ResultSet rs) throws SQLException {
        Question q = new Question();
        q.setId(rs.getInt("id"));
        q.setQuestionText(rs.getString("question_text"));
        q.setOptionA(rs.getString("option_a"));
        q.setOptionB(rs.getString("option_b"));
        q.setOptionC(rs.getString("option_c"));
        q.setOptionD(rs.getString("option_d"));
        q.setCorrectOption(rs.getString("correct_option"));
        q.setCategory(rs.getString("category"));
        return q;
    }
}
