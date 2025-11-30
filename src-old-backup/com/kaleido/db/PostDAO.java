package com.kaleido.db;

import com.kaleido.models.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.kaleido.db.DatabaseConnection.getConnection;

public class PostDAO {

    public boolean createPost(Post post) {
        String sql = "INSERT INTO posts (user_id, username, content_text, image_url) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, post.getUserId());
            stmt.setString(2, post.getUsername());
            stmt.setString(3, post.getContentText());
            stmt.setString(4, post.getImageUrl());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.post_id, p.user_id, p.content_text, p.image_url, p.created_at, u.username " +
                "FROM posts p " +
                "JOIN users u ON p.user_id = u.user_id " +
                "ORDER BY p.created_at DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Post post = new Post();
                post.setPostId(rs.getInt("post_id"));
                post.setUserId(rs.getInt("user_id"));
                post.setUsername(rs.getString("username")); // new: fetched dynamically
                post.setContentText(rs.getString("content_text"));
                post.setImageUrl(rs.getString("image_url"));
                post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public List<Post> getPostsByUser(int userId) {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.post_id, p.user_id, p.content_text, p.image_url, p.created_at, u.username " +
                "FROM posts p " +
                "JOIN users u ON p.user_id = u.user_id " +
                "WHERE p.user_id = ? " +
                "ORDER BY p.created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Post post = new Post();
                post.setPostId(rs.getInt("post_id"));
                post.setUserId(rs.getInt("user_id"));
                post.setUsername(rs.getString("username"));
                post.setContentText(rs.getString("content_text"));
                post.setImageUrl(rs.getString("image_url"));
                post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public List<Post> getRandomPosts(int limit, int excludeUserId) {
        List<Post> posts = new ArrayList<>();

        // Simple query without complex joins
        String sql = "SELECT * FROM posts WHERE user_id != ? ORDER BY RAND() LIMIT ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, excludeUserId);
            stmt.setInt(2, limit);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Post post = new Post();
                // Set basic post properties that actually exist in your Post class
                post.setPostId(rs.getInt("post_id"));
                post.setUserId(rs.getInt("user_id"));
                post.setUsername(rs.getString("username"));
                post.setContentText(rs.getString("content_text"));
                post.setImageUrl(rs.getString("image_url"));
                post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                posts.add(post);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
}