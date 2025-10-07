package com.carpool.carpool.util;

import com.carpool.carpool.dto.codeMirror.CodeDocument;
import com.carpool.carpool.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component // Marks this class as a Spring component for dependency injection
public class SessionManager {
    private final ConcurrentHashMap<String, CodeDocument> sessions = new ConcurrentHashMap<>(); // Stores active sessions mapped by sessionId

    @Autowired // Injects the CodeRepository bean
    CodeRepository codeRepository;

    @Autowired // Injects the TaskScheduler bean for scheduling tasks
    TaskScheduler taskScheduler;

    private ScheduledFuture<?> persistenceTask; // Reference to the scheduled persistence task
    private final Object lock = new Object(); // Lock object for thread-safe scheduling

    public CodeDocument getOrCreateSession(String sessionId) {
        // Retrieves an existing session or creates a new one if absent
        CodeDocument doc = sessions.computeIfAbsent(sessionId,
                k -> new CodeDocument(sessionId, "", true));
        startPersistenceIfNeeded(); // Ensures persistence task starts if a new session is created
        return doc;
    }

    public void updateSession(String sessionId, CodeDocument doc) {
        // Updates or adds a session in the map
        sessions.put(sessionId, doc);
    }

    public CodeDocument getSession(String sessionId) {
        // Retrieves a session by its ID
        return sessions.get(sessionId);
    }

    public Collection<CodeDocument> getAllSessions() {
        // Returns all active sessions
        return sessions.values();
    }

    private void startPersistenceIfNeeded(){
        // Starts the persistence task if not already running and sessions exist
        synchronized (lock){
            if(persistenceTask ==null && !sessions.isEmpty()){
                persistenceTask = taskScheduler.scheduleAtFixedRate(this::persistActiveSessions,
                        TimeUnit.SECONDS.toMillis(5)); // Schedules every 5 seconds
            }
        }
    }

    private void persistActiveSessions(){
        // Persists sessions for pro users only
        var proSessions = sessions.values().stream()
                .filter(CodeDocument::isProUser) // Filters pro user sessions
                .toList();

        if(proSessions.isEmpty()){
            // If no pro sessions, stop the persistence task
            stopPersistenceTask();
            return;
        }

        // Updates the timestamp and saves all pro sessions
        proSessions.forEach(doc -> doc.setUpdatedAt(java.time.LocalDateTime.now()));
        codeRepository.saveAll(proSessions);
    }

    private void stopPersistenceTask(){
        // Stops the persistence task if no sessions remain
        synchronized (lock){
            if (sessions.isEmpty() && persistenceTask != null) {
                persistenceTask.cancel(false); // Cancels the scheduled task
                persistenceTask = null; // Clears the reference
            }
        }
    }
}