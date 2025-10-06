package com.carpool.carpool.dto.codeMirror;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CodeUpdate {
    private String sessionId;
    private String userId;
    private int lineNumber;
    private int startIndex;
    private int endIndex;
    private String newText;
}