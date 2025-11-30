package com.kaleido.models;

import java.time.LocalDateTime;

public class Community {
    private int communityId;
    private String name;
    private LocalDateTime createdAt;

    public Community() {}

    public Community(String name) {
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    public int getCommunityId() { return communityId; }
    public void setCommunityId(int communityId) { this.communityId = communityId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}