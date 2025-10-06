package com.carpool.carpool.controller;

import com.carpool.carpool.dao.codeMirror.CodeEdit;
import com.carpool.carpool.dto.codeMirror.CodeDocument;
import com.carpool.carpool.dto.codeMirror.CodeUpdate;
import com.carpool.carpool.util.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class EditorController {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/editor")
//    @SendTo("/topic/editor") // Removed to use SimpMessagingTemplate for targeted messaging
    public void handleEdit(CodeEdit edit) {
        // Process the incoming code update (e.g., save to database, etc.)
        CodeDocument document = sessionManager.getOrCreateSession(edit.getSessionId());

        // Apply patch to the document content
        String[] lines = document.getContent().split("\n");
        while (lines.length <= edit.getLineNumber()) {
            String[] newLines = new String[lines.length + 1];
            System.arraycopy(lines, 0, newLines, 0, lines.length);
            newLines[lines.length] = "";
            lines = newLines;
        }
        String line = lines[edit.getLineNumber()];
        String updatedLine = line.substring(0, Math.min(edit.getStartIndex(), line.length())) + edit.getNewText() + line.substring(Math.min(edit.getEndIndex(), line.length()));
        lines[edit.getLineNumber()] = updatedLine;
        document.setContent(String.join("\n", lines));
        //update SessionManager
        sessionManager.updateSession(edit.getSessionId(), document);
        // Save the updated document to the database
        // ✅ Broadcast to all subscribers of this specific sessionId
        simpMessagingTemplate.convertAndSend("/topic/editor/" + edit.getSessionId(), new CodeUpdate(edit.getSessionId(), edit.getUserId(), edit.getLineNumber(), edit.getStartIndex(), edit.getEndIndex(), edit.getNewText()));
    }
}
