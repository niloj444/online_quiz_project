package com.quiz.dao;

import com.quiz.model.Question;
import com.quiz.model.Quiz;
import com.quiz.util.DBConnection;
import java.sql.*;
import java.util.*;

/**
 * JDBC access for teacher assessments and their selected question-bank items.
 */
public class QuizDAO {
    private static final String COLS = "SELECT q.id,q.teacher_id,q.subject,q.title,q.description,q.quiz_type,q.duration_minutes,q.passing_percentage,q.published,q.due_date,q.created_at,COUNT(qq.question_id) question_count ";
    private static final String FROM = "FROM quizzes q LEFT JOIN quiz_questions qq ON q.id=qq.quiz_id ";

    public List<Quiz> findByTeacher(int teacherId) throws SQLException {
        return find(COLS + FROM + "WHERE q.teacher_id=? GROUP BY q.id ORDER BY q.created_at DESC", teacherId);
    }

    public List<Quiz> findPublished() throws SQLException {
        return find(COLS + FROM
                + "WHERE q.published=TRUE GROUP BY q.id HAVING COUNT(qq.question_id)>0 ORDER BY q.created_at DESC",
                null);
    }
    public List<Quiz> findPublishedForStudent(int studentId, String subject) throws SQLException {
        String sql="SELECT q.id,q.teacher_id,q.subject,q.title,q.description,q.quiz_type,q.duration_minutes,q.passing_percentage,q.published,q.due_date,q.created_at,COUNT(qq.question_id) question_count,MAX(r.id) attempt_id,MAX(r.score*100/r.total) last_score FROM quizzes q LEFT JOIN quiz_questions qq ON q.id=qq.quiz_id LEFT JOIN results r ON r.quiz_id=q.id AND r.user_id=? WHERE q.published=TRUE"+(subject==null||subject.isEmpty()?"":" AND q.subject=?")+" GROUP BY q.id HAVING COUNT(qq.question_id)>0 ORDER BY q.due_date IS NULL,q.due_date,q.created_at DESC";
        List<Quiz> out=new ArrayList<>();try(Connection c=DBConnection.get();PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,studentId);if(subject!=null&&!subject.isEmpty())p.setString(2,subject);try(ResultSet r=p.executeQuery()){while(r.next()){Quiz q=map(r);q.setCompleted(r.getInt("attempt_id")>0);Object score=r.getObject("last_score");q.setLastScore(score==null?null:((Number)score).intValue());out.add(q);}}}return out;
    }
    public List<String> findPublishedSubjects() throws SQLException { List<String> out=new ArrayList<>();try(Connection c=DBConnection.get();PreparedStatement p=c.prepareStatement("SELECT DISTINCT subject FROM quizzes WHERE published=TRUE ORDER BY subject");ResultSet r=p.executeQuery()){while(r.next())out.add(r.getString(1));}return out; }

    public Quiz findByIdAndTeacher(int id, int teacherId) throws SQLException {
        String sql = COLS + FROM + "WHERE q.id=? AND q.teacher_id=? GROUP BY q.id";
        try (Connection c = DBConnection.get(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            p.setInt(2, teacherId);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public Quiz findPublishedById(int id) throws SQLException {
        String sql = COLS + FROM + "WHERE q.id=? AND q.published=TRUE GROUP BY q.id HAVING COUNT(qq.question_id)>0";
        try (Connection c = DBConnection.get(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Question> findQuestions(int quizId) throws SQLException {
        String sql = "SELECT q.id,q.question_text,q.option_a,q.option_b,q.option_c,q.option_d,q.correct_option,q.category "
                + "FROM quiz_questions qq JOIN questions q ON q.id=qq.question_id WHERE qq.quiz_id=? ORDER BY qq.position,q.id";
        List<Question> out = new ArrayList<>();
        try (Connection c = DBConnection.get(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, quizId);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    Question q = new Question();
                    q.setId(r.getInt("id"));
                    q.setQuestionText(r.getString("question_text"));
                    q.setOptionA(r.getString("option_a"));
                    q.setOptionB(r.getString("option_b"));
                    q.setOptionC(r.getString("option_c"));
                    q.setOptionD(r.getString("option_d"));
                    q.setCorrectOption(r.getString("correct_option"));
                    q.setCategory(r.getString("category"));
                    out.add(q);
                }
            }
        }
        return out;
    }

    public int save(Quiz q, List<Integer> questionIds) throws SQLException {
        try (Connection c = DBConnection.get()) {
            c.setAutoCommit(false);
            try {
                int id = q.getId();
                if (id == 0) {
                    String s = "INSERT INTO quizzes (teacher_id,subject,title,description,quiz_type,duration_minutes,passing_percentage,published,due_date) VALUES (?,?,?,?,?,?,?,?,?)";
                    try (PreparedStatement p = c.prepareStatement(s, Statement.RETURN_GENERATED_KEYS)) {
                        fill(p, q, false);
                        p.executeUpdate();
                        try (ResultSet k = p.getGeneratedKeys()) {
                            k.next();
                            id = k.getInt(1);
                        }
                    }
                } else {
                    String s = "UPDATE quizzes SET subject=?,title=?,description=?,quiz_type=?,duration_minutes=?,passing_percentage=?,published=?,due_date=? WHERE id=? AND teacher_id=?";
                    try (PreparedStatement p = c.prepareStatement(s)) {
                        fill(p, q, true);
                        p.setInt(9, id);
                        p.setInt(10, q.getTeacherId());
                        if (p.executeUpdate() == 0)
                            throw new SQLException("Quiz not found");
                    }
                    try (PreparedStatement p = c.prepareStatement("DELETE FROM quiz_questions WHERE quiz_id=?")) {
                        p.setInt(1, id);
                        p.executeUpdate();
                    }
                }
                try (PreparedStatement p = c
                        .prepareStatement("INSERT INTO quiz_questions (quiz_id,question_id,position) VALUES (?,?,?)")) {
                    int pos = 1;
                    for (Integer qid : questionIds) {
                        p.setInt(1, id);
                        p.setInt(2, qid);
                        p.setInt(3, pos++);
                        p.addBatch();
                    }
                    p.executeBatch();
                }
                c.commit();
                return id;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public void setPublished(int id, int teacherId, boolean published) throws SQLException {
        try (Connection c = DBConnection.get();
                PreparedStatement p = c
                        .prepareStatement("UPDATE quizzes SET published=? WHERE id=? AND teacher_id=?")) {
            p.setBoolean(1, published);
            p.setInt(2, id);
            p.setInt(3, teacherId);
            p.executeUpdate();
        }
    }

    public void delete(int id, int teacherId) throws SQLException {
        try (Connection c = DBConnection.get();
                PreparedStatement p = c.prepareStatement("DELETE FROM quizzes WHERE id=? AND teacher_id=?")) {
            p.setInt(1, id);
            p.setInt(2, teacherId);
            p.executeUpdate();
        }
    }

    private List<Quiz> find(String sql, Integer id) throws SQLException {
        List<Quiz> o = new ArrayList<>();
        try (Connection c = DBConnection.get(); PreparedStatement p = c.prepareStatement(sql)) {
            if (id != null)
                p.setInt(1, id);
            try (ResultSet r = p.executeQuery()) {
                while (r.next())
                    o.add(map(r));
            }
        }
        return o;
    }

    private void fill(PreparedStatement p, Quiz q, boolean update) throws SQLException {
        int i = 1;
        if (!update)
            p.setInt(i++, q.getTeacherId());
        p.setString(i++, q.getSubject());
        p.setString(i++, q.getTitle());
        p.setString(i++, q.getDescription());
        p.setString(i++, q.getQuizType());
        p.setInt(i++, q.getDurationMinutes());
        p.setInt(i++, q.getPassingPercentage());
        p.setBoolean(i++, q.isPublished());
        p.setTimestamp(i, q.getDueDate());
    }

    private Quiz map(ResultSet r) throws SQLException {
        Quiz q = new Quiz();
        q.setId(r.getInt("id"));
        q.setTeacherId(r.getInt("teacher_id"));
        q.setSubject(r.getString("subject"));
        q.setTitle(r.getString("title"));
        q.setDescription(r.getString("description"));
        q.setQuizType(r.getString("quiz_type"));
        q.setDurationMinutes(r.getInt("duration_minutes"));
        q.setPassingPercentage(r.getInt("passing_percentage"));
        q.setPublished(r.getBoolean("published"));
        q.setDueDate(r.getTimestamp("due_date"));
        q.setCreatedAt(r.getTimestamp("created_at"));
        q.setQuestionCount(r.getInt("question_count"));
        return q;
    }
}
