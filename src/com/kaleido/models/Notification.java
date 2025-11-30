package com.kaleido.models;

import java.time.LocalDateTime;

public class Notification {
    private int notificationId;
    private int receiverId;
    private int senderId;
    private int postId;
    private String type;
    private String message;
    private LocalDateTime createdAt;

    public Notification() {
    }

    public Notification(int notificationId, int receiverId, int senderId, int postId, String type, String message, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.postId = postId;
        this.type = type;
        this.message = message;
        this.createdAt = createdAt;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
