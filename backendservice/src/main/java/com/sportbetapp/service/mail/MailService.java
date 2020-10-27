package com.sportbetapp.service.mail;

import javax.mail.MessagingException;

import com.sportbetapp.domain.technical.Mail;

public interface MailService {

    void sendMailWithAttachment(Mail mail, byte[] pdfBytes) throws MessagingException;

    void sendMail(Mail mail) throws MessagingException;

}
