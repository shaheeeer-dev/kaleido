package com.kaleido.db;

import com.kaleido.models.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.kaleido.db.DatabaseConnection.getConnection;

public class NotificationDAO {

    public void addNotification(int receiverId, int senderId, int postId, String type, String message) {
        String sql = "INSERT INTO notifications (receiver_id, sender_id, post_id, type, message) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, receiverId);
            stmt.setInt(2, senderId);
            stmt.setInt(3, postId);
            stmt.setString(4, type);
            stmt.setString(5, message);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getNotificationsForUser(int userId) {
        List<Notification> notifications = new ArrayList<>();

        String sql = "SELECT * FROM notifications WHERE receiver_id = ? ORDER BY created_at DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notification n = new Notification();
                n.setNotificationId(rs.getInt("notification_id"));
                n.setReceiverId(rs.getInt("receiver_id"));
                n.setSenderId(rs.getInt("sender_id"));
                n.setPostId(rs.getInt("post_id"));
                n.setType(rs.getString("type"));
                n.setMessage(rs.getString("message"));
                n.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                notifications.add(n);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }
}
