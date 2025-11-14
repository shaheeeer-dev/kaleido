package com.kaleido.models;

import java.time.LocalDateTime;

public class Post {
    private int postId;
    private int userId;
    private String userName;
    private int communityId;
    private String contentText;
    private String imageUrl;
    private LocalDateTime createdAt;


    public Post() {
    }

    public Post(int postId, int userId, String userName, int communityId, String contentText, String imageUrl, LocalDateTime createdAt) {
        this.postId = postId;
        this.userId = userId;
        this.userName = userName;
        this.communityId = communityId;
        this.contentText = contentText;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}