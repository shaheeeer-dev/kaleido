package com.kaleido.models;

import java.time.LocalDateTime;

public class CommunityMessage {
    private int messageId;
    private int communityId;
    private int userId;
    private String username;
    private String content;
    private LocalDateTime createdAt;

    public CommunityMessage() {}

    public CommunityMessage(int communityId, int userId, String username, String content) {
        this.communityId = communityId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; }

    public int getCommunityId() { return communityId; }
    public void setCommunityId(int communityId) { this.communityId = communityId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}