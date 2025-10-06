package com.carpool.carpool.repository;

import com.carpool.carpool.dto.codeMirror.CodeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CodeRepository extends JpaRepository<CodeDocument, String> {
    Optional<CodeDocument> findBySessionId(String sessionId);
    List<CodeDocument> findByUpdatedAtBefore(LocalDateTime cutoff);
}