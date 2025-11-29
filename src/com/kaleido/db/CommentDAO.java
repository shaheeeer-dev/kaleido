package com.kaleido.db;

import com.kaleido.models.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.kaleido.db.DatabaseConnection.getConnection;

public class CommentDAO{

    NotificationDAO notificationDAO = new NotificationDAO();

    // ADD COMMENT + NOTIFICATION
    public void addComment(int postId, int userId, String text, int postOwnerId) {
        String sql = "INSERT INTO comments (post_id, user_id, comment_text, created_at) VALUES (?, ?, ?, NOW())";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.setString(3, text);
            stmt.executeUpdate();

            if (userId != postOwnerId) {
                notificationDAO.addNotification(
                        postOwnerId,
                        userId,
                        postId,
                        "comment",
                        "Someone commented on your post"
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE COMMENT
    public void deleteComment(int commentId) {
        String sql = "DELETE FROM comments WHERE comment_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, commentId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // GET COMMENTS FOR A POST
    public List<Comment> getComments(int postId) {
        List<Comment> list = new ArrayList<>();

        String sql = "SELECT * FROM comments WHERE post_id = ? ORDER BY created_at ASC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Comment c = new Comment();
                c.setCommentId(rs.getInt("comment_id"));
                c.setPostId(rs.getInt("post_id"));
                c.setUserId(rs.getInt("user_id"));
                c.setCommentText(rs.getString("comment_text"));
                c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                list.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}