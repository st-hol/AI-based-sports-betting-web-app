package com.sportbetapp.service.mail.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.sportbetapp.domain.technical.Mail;
import com.sportbetapp.service.mail.MailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailServiceImpl  implements MailService {

    @Autowired
    private JavaMailSender emailSender;

    /**
     * in future can be used to send PDF report to user
     * @param mail
     * @param pdfBytes
     * @throws MessagingException
     */
    @Override
    public void sendMailWithAttachment(Mail mail, byte[] pdfBytes) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject(mail.getSubject());
        helper.setText(mail.getContent());
        helper.setTo(mail.getTo());
        helper.setFrom(mail.getTo());

        InputStreamSource source = new ByteArrayResource(pdfBytes, "report.pdf");
        helper.addAttachment("report.pdf", source);
        emailSender.send(message);
        //It might be that gmail uses this delay to prevent spammers from using their SMTP server from the "outside":
        // if the SMTP is called from the actual webmail client it would not use this delay.
    }

    @Override
    public void sendMail(Mail mail) throws MessagingException {
        log.info("Creating message started.");
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getContent());
        helper.setTo(mail.getTo());
        helper.setFrom(mail.getFrom());
        emailSender.send(message);
        log.info("Message sent successfully.");
    }

}