package com.kaleido.db;

import java.sql.*;

import static com.kaleido.db.DatabaseConnection.getConnection;

public class LikeDAO {

    NotificationDAO notificationDAO = new NotificationDAO();

    // ADD LIKE + NOTIFICATION
    public void addLike(int postId, int userId, int postOwnerId) {
        String sql = "INSERT INTO likes (post_id, user_id, created_at) VALUES (?, ?, NOW())";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();

            // Only notify if user is liking someone else's post
            if (userId != postOwnerId) {
                notificationDAO.addNotification(
                        postOwnerId,
                        userId,
                        postId,
                        "like",
                        "Someone liked your post"
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // REMOVE LIKE
    public void removeLike(int postId, int userId) {
        String sql = "DELETE FROM likes WHERE post_id = ? AND user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CHECK IF USER LIKED THE POST
    public boolean isLikedByUser(int postId, int userId) {
        String sql = "SELECT 1 FROM likes WHERE post_id = ? AND user_id = ? LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            stmt.setInt(2, userId);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // COUNT LIKES
    public int countLikes(int postId) {
        String sql = "SELECT COUNT(*) AS total FROM likes WHERE post_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return rs.getInt("total");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}