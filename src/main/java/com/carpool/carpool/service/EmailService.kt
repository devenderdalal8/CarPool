package com.carpool.carpool.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.TemplateEngine

@Service("emailService")
@Transactional
open class EmailService (private var mailSender: JavaMailSender, val templateEngine: TemplateEngine) {

    @Async
    open fun sendEmail(email: SimpleMailMessage) {
        try {
            mailSender.send(email)
        } catch (e: Exception) {
           println(e.message)
        }
    }

    fun sendEmailWithoutHtml(email: String?, subject: String?, endpoint: String, token: String) {
        val mailMessage = SimpleMailMessage()
        mailMessage.setTo(email)
        mailMessage.subject = subject
        mailMessage.text = ("To confirm your account, please click here : "
                + "http://localhost:1234/" + endpoint + "?token=" + token)
        sendEmail(mailMessage)
    }
}