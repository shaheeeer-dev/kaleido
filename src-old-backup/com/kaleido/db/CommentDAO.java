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
        // First get the username
        String username = getUsernameById(userId);

        String sql = "INSERT INTO comments (post_id, user_id, username, comment_text, created_at) VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.setString(3, username); // Add username
            stmt.setString(4, text);
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

    // Helper method to get username by user ID
    private String getUsernameById(int userId) {
        String sql = "SELECT username FROM users WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("username");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Unknown User";
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

        // Updated query to join with users table and get username
        String sql = "SELECT c.*, u.username " +
                "FROM comments c " +
                "JOIN users u ON c.user_id = u.user_id " +
                "WHERE c.post_id = ? " +
                "ORDER BY c.created_at ASC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Comment c = new Comment();
                c.setCommentId(rs.getInt("comment_id"));
                c.setPostId(rs.getInt("post_id"));
                c.setUserId(rs.getInt("user_id"));
                c.setUsername(rs.getString("username")); // Set the username
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