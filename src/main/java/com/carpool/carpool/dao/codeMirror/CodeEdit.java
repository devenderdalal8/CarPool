package com.carpool.carpool.dao.codeMirror;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeEdit {
    private String sessionId;
    private String userId;
    private int lineNumber;
    private int startIndex;
    private int endIndex;
    private String newText;
    private String fullCode;
}
