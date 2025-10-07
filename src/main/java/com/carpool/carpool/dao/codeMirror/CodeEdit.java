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

    @Override
    public String toString() {
        return "CodeEdit{" +
                "sessionId='" + sessionId + '\'' +
                ", userId='" + userId + '\'' +
                ", lineNumber=" + lineNumber +
                ", startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                ", newText='" + newText + '\'' +
                ", fullCode='" + fullCode + '\'' +
                '}';
    }
}
