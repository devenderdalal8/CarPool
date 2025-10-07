package com.carpool.carpool.dto.codeMirror;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class CodeDocument {
    @Id
    private String sessionId;

    @Lob
    private String content;

    private LocalDateTime updatedAt; // Track last modification
    private Boolean proUser; // Track last modification

    public CodeDocument(String sessionId, String content, Boolean proUser) {
        this.sessionId = sessionId;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
        this.proUser = proUser;
    }

    public Boolean isProUser() {
        return proUser;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getProUser() {
        return proUser;
    }

    public void setProUser(Boolean proUser) {
        this.proUser = proUser;
    }
}