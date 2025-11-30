package com.kaleido.db;

import com.kaleido.models.Community;
import com.kaleido.models.CommunityMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.kaleido.db.DatabaseConnection.getConnection;

public class CommunitiesDAO {

    public Community getCommunityByName(String name) {
        String sql = "SELECT * FROM communities WHERE name = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Community community = new Community();
                community.setCommunityId(rs.getInt("community_id"));
                community.setName(rs.getString("name"));
                community.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return community;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void createCommunity(Community community) {
        String sql = "INSERT INTO communities (name) VALUES (?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, community.getName());
            stmt.executeUpdate();

            // Get the generated ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                community.setCommunityId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMessage(CommunityMessage message) {
        String sql = "INSERT INTO community_messages (community_id, user_id, username, content) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, message.getCommunityId());
            stmt.setInt(2, message.getUserId());
            stmt.setString(3, message.getUsername());
            stmt.setString(4, message.getContent());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CommunityMessage> getMessagesByCommunity(int communityId) {
        List<CommunityMessage> messages = new ArrayList<>();
        String sql = "SELECT * FROM community_messages WHERE community_id = ? ORDER BY created_at ASC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, communityId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CommunityMessage message = new CommunityMessage();
                message.setMessageId(rs.getInt("message_id"));
                message.setCommunityId(rs.getInt("community_id"));
                message.setUserId(rs.getInt("user_id"));
                message.setUsername(rs.getString("username"));
                message.setContent(rs.getString("content"));
                message.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                messages.add(message);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public List<Community> getAllCommunities() {
        List<Community> communities = new ArrayList<>();
        String sql = "SELECT * FROM communities ORDER BY created_at DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Community community = new Community();
                community.setCommunityId(rs.getInt("community_id"));
                community.setName(rs.getString("name"));
                community.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                communities.add(community);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return communities;
    }
}