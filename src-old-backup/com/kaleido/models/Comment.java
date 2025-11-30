package com.kaleido.models;

import java.time.LocalDateTime;

public class Comment {

    private int commentId;
    private int postId;
    private int userId;
    private String username;
    private String commentText;
    private LocalDateTime createdAt;

    public Comment() {}

    public Comment(int postId, int userId, String username, String commentText) {
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.commentText = commentText;
        this.createdAt = LocalDateTime.now();
    }

    public Comment(int commentId, int postId, int userId, String commentText, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.commentText = commentText;
        this.createdAt = createdAt;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}