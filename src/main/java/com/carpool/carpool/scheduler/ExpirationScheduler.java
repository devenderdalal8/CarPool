package com.carpool.carpool.scheduler;

import com.carpool.carpool.dto.codeMirror.CodeDocument;
import com.carpool.carpool.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ExpirationScheduler {

    @Autowired
    private CodeRepository codeRepository;

    // Run every hour
    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpiredSessions() {
        if (codeRepository.count() == 0) {
            return; // nothing to delete, skip
        }

        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        var expiredDocs = codeRepository.findByUpdatedAtBefore(cutoff);

        if (!expiredDocs.isEmpty()) {
            codeRepository.deleteAll(expiredDocs);
        }
    }
}