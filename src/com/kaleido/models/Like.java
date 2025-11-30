package com.kaleido.models;

import java.time.LocalDateTime;

public class Like {
    private int userId;
    private int postId;
    private int likeId;
    private LocalDateTime createdAt;

    public Like() {
    }

    public Like(int userId, int postId, int likeId, LocalDateTime createdAt) {
        this.userId = userId;
        this.postId = postId;
        this.likeId = likeId;
        this.createdAt = createdAt;
    }

    public Like(int userId, int postId, int likeId) {
        this.userId = userId;
        this.postId = postId;
        this.likeId = likeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
