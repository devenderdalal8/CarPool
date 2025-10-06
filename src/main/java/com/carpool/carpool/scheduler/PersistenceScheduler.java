package com.carpool.carpool.scheduler;

import com.carpool.carpool.dto.codeMirror.CodeDocument;
import com.carpool.carpool.repository.CodeRepository;
import com.carpool.carpool.util.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PersistenceScheduler {
    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private CodeRepository codeRepository;

    @Scheduled(fixedRate = 5000)
    public void persistSessions() {
        var allSessions = sessionManager.getAllSessions().stream()
                .filter(CodeDocument::getProUser)
                .toList();

        if (allSessions.isEmpty()) {
            // No active sessions → skip work
            return;
        }

        // Update timestamp and batch save
        allSessions.forEach(doc -> doc.setUpdatedAt(LocalDateTime.now()));
        codeRepository.saveAll(allSessions);

    }
}
