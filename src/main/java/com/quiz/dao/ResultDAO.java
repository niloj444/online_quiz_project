package com.quiz.dao;

import com.quiz.model.Result;
import com.quiz.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultDAO {

    /** Saves an attempt with subject/category and returns its new id. */
    public int save(int userId, String category, int score, int total) throws SQLException {
        String sql = "INSERT INTO results (user_id, category, score, total) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.get();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setString(2, (category == null || category.trim().isEmpty()) ? "All Topics" : category.trim());
            ps.setInt(3, score);
            ps.setInt(4, total);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                return 0;
            }
        }
    }

    /** Backwards-compatible overload. */
    public int save(int userId, int score, int total) throws SQLException {
        return save(userId, "All Topics", score, total);
    }

    public int saveAssessment(int userId, com.quiz.model.Quiz quiz, int score, int total) throws SQLException {
        String sql = "INSERT INTO results (user_id,category,score,total,quiz_id,passing_percentage,quiz_type) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.get(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,userId); ps.setString(2,quiz.getTitle()); ps.setInt(3,score); ps.setInt(4,total);
            ps.setInt(5,quiz.getId()); ps.setInt(6,quiz.getPassingPercentage()); ps.setString(7,quiz.getQuizType()); ps.executeUpdate();
            try(ResultSet keys=ps.getGeneratedKeys()) { return keys.next()?keys.getInt(1):0; }
        }
    }

    /** Only returns the result when it belongs to this user. */
    public Result findByIdAndUser(int id, int userId) throws SQLException {
        String sql = "SELECT id, user_id, category, score, total, quiz_id, passing_percentage, quiz_type, taken_at FROM results WHERE id = ? AND user_id = ?";
        try (Connection con = DBConnection.get(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Result> findByUser(int userId) throws SQLException {
        String sql = "SELECT id, user_id, category, score, total, quiz_id, passing_percentage, quiz_type, taken_at FROM results WHERE user_id = ? ORDER BY taken_at DESC";
        List<Result> out = new ArrayList<>();
        try (Connection con = DBConnection.get(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        }
        return out;
    }

    public List<com.quiz.model.UserResult> findAllWithUsers() throws SQLException {
        String sql = "SELECT r.id, r.user_id, u.name AS user_name, u.email AS user_email, "
                   + "r.category, r.score, r.total, r.taken_at "
                   + "FROM results r "
                   + "JOIN users u ON r.user_id = u.id "
                   + "ORDER BY r.taken_at DESC";
        List<com.quiz.model.UserResult> out = new ArrayList<>();
        try (Connection con = DBConnection.get();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                com.quiz.model.UserResult ur = new com.quiz.model.UserResult();
                ur.setId(rs.getInt("id"));
                ur.setUserId(rs.getInt("user_id"));
                ur.setUserName(rs.getString("user_name"));
                ur.setUserEmail(rs.getString("user_email"));
                ur.setCategory(rs.getString("category"));
                ur.setScore(rs.getInt("score"));
                ur.setTotal(rs.getInt("total"));
                ur.setTakenAt(rs.getTimestamp("taken_at"));
                out.add(ur);
            }
        }
        return out;
    }

    public List<com.quiz.model.TeacherResult> findByTeacherAndQuiz(int teacherId, int quizId) throws SQLException {
        String sql="SELECT u.name student_name,q.title quiz_name,r.score,r.total,r.passing_percentage,r.taken_at FROM results r JOIN users u ON u.id=r.user_id JOIN quizzes q ON q.id=r.quiz_id WHERE q.teacher_id=? AND r.quiz_id=? ORDER BY r.taken_at DESC";
        List<com.quiz.model.TeacherResult> out=new ArrayList<>();
        try(Connection c=DBConnection.get();PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,teacherId);p.setInt(2,quizId);try(ResultSet rs=p.executeQuery()){while(rs.next()){com.quiz.model.TeacherResult r=new com.quiz.model.TeacherResult();r.setStudentName(rs.getString("student_name"));r.setQuizName(rs.getString("quiz_name"));r.setScore(rs.getInt("score"));r.setTotal(rs.getInt("total"));r.setPassingPercentage(rs.getInt("passing_percentage"));r.setTakenAt(rs.getTimestamp("taken_at"));out.add(r);}}} return out;
    }

    public boolean hasAssessmentAttempt(int userId, int quizId) throws SQLException {
        try (Connection c=DBConnection.get(); PreparedStatement p=c.prepareStatement("SELECT 1 FROM results WHERE user_id=? AND quiz_id=? LIMIT 1")) {
            p.setInt(1,userId); p.setInt(2,quizId); try(ResultSet r=p.executeQuery()) { return r.next(); }
        }
    }
    public List<com.quiz.model.SubjectPerformance> findPerformanceByUser(int userId) throws SQLException {
        String sql="SELECT q.subject,COUNT(*) attempts,AVG(r.score*100.0/r.total) average_score,MAX(r.score*100/r.total) highest,SUM((r.score*100/r.total)>=r.passing_percentage) passed FROM results r JOIN quizzes q ON q.id=r.quiz_id WHERE r.user_id=? GROUP BY q.subject ORDER BY q.subject";
        List<com.quiz.model.SubjectPerformance> out=new ArrayList<>();
        try(Connection c=DBConnection.get();PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,userId);try(ResultSet rs=p.executeQuery()){while(rs.next()){com.quiz.model.SubjectPerformance s=new com.quiz.model.SubjectPerformance();s.setSubject(rs.getString("subject"));s.setAttempts(rs.getInt("attempts"));s.setAverage(rs.getDouble("average_score"));s.setHighest(rs.getInt("highest"));s.setPassed(rs.getInt("passed"));s.setFailed(s.getAttempts()-s.getPassed());out.add(s);}}} return out;
    }

    private Result map(ResultSet rs) throws SQLException {
        Result r = new Result();
        r.setId(rs.getInt("id"));
        r.setUserId(rs.getInt("user_id"));
        r.setCategory(rs.getString("category"));
        r.setQuizId((Integer) rs.getObject("quiz_id"));
        r.setPassingPercentage((Integer) rs.getObject("passing_percentage"));
        r.setQuizType(rs.getString("quiz_type"));
        r.setScore(rs.getInt("score"));
        r.setTotal(rs.getInt("total"));
        r.setTakenAt(rs.getTimestamp("taken_at"));
        return r;
    }
}
