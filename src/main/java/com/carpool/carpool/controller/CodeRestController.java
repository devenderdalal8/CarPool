package com.carpool.carpool.controller;

import com.carpool.carpool.dto.codeMirror.CodeDocument;
import com.carpool.carpool.util.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/code")
public class CodeRestController {

    @Autowired
    private SessionManager sessionManager;

    @GetMapping("/{sessionId}")
    public CodeDocument getCode(@PathVariable String sessionId) {
        return sessionManager.getOrCreateSession(sessionId);
    }
}