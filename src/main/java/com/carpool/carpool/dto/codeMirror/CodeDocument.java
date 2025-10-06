package com.carpool.carpool.dto.codeMirror;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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

}