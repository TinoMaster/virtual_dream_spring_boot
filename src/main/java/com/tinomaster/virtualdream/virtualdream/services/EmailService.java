package com.tinomaster.virtualdream.virtualdream.services;

import com.tinomaster.virtualdream.virtualdream.dtos.BusinessDto;
import com.tinomaster.virtualdream.virtualdream.entities.Business;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.tinomaster.virtualdream.virtualdream.dtos.EmailDto;
import com.tinomaster.virtualdream.virtualdream.entities.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(EmailDto emailDto) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(emailDto.getDestination());
        helper.setSubject(emailDto.getSubject());
        helper.setText(emailDto.getMessage(), true);

        javaMailSender.send(message);
    }

    public void sendEmailOwnerRegistrationPending(EmailDto emailDto, User user) throws MessagingException {
        Context context = new Context();
        context.setVariable("username", user.getName());
        context.setVariable("applicationName", "CONTROL");

        String contentHtml = templateEngine.process("sendEmailAfterRegistrationOwner", context);

        emailDto.setMessage(contentHtml);

        sendEmail(emailDto);
    }

    public void sendAdminNewUserNotification(EmailDto emailDto, User user, BusinessDto business) throws MessagingException {
        Context context = new Context();
        context.setVariable("username", user.getName());
        context.setVariable("userEmail", user.getEmail());
        context.setVariable("businessName", business.getName());
        context.setVariable("applicationName", "CONTROL");
        context.setVariable("registrationDate", user.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        context.setVariable("adminPanelLink", "http://localhost:4200/admin/users");

        String contentHtml = templateEngine.process("adminNewOwnerRegistration", context);

        emailDto.setMessage(contentHtml);

        sendEmail(emailDto);
    }

    public void sendAcceptedAuthRequest(EmailDto emailDto, User user, Business business) throws MessagingException {
        Context context = new Context();
        context.setVariable("username", user.getName());
        context.setVariable("userEmail", user.getEmail());
        context.setVariable("businessName", business.getName());
        context.setVariable("applicationName", "CONTROL");
        context.setVariable("registrationDate", user.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        context.setVariable("adminPanelLink", "http://localhost:4200/admin/users");

        String contentHtml = templateEngine.process("acceptedAuthRequest", context);

        emailDto.setMessage(contentHtml);

        sendEmail(emailDto);
    }

    public void sendRejectedAuthRequest(EmailDto emailDto, User user, Business business) throws MessagingException {
        Context context = new Context();
        context.setVariable("username", user.getName());
        context.setVariable("userEmail", user.getEmail());
        context.setVariable("businessName", business.getName());
        context.setVariable("applicationName", "CONTROL");
        context.setVariable("registrationDate", user.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        context.setVariable("adminPanelLink", "http://localhost:4200/admin/users");

        String contentHtml = templateEngine.process("rejectedAuthRequest", context);

        emailDto.setMessage(contentHtml);

        sendEmail(emailDto);
    }

    public void sendEmailAfterUpdateUser(EmailDto emailDto, User user) throws MessagingException {
        Context context = new Context();
        context.setVariable("userName", user.getName());
        context.setVariable("message", emailDto.getMessage());

        String contentHTML = templateEngine.process("sendEmailAfterUpdateUser", context);
        emailDto.setMessage(contentHTML);

        sendEmail(emailDto);
    }

    public void sendEmailAfterRegisterUser(EmailDto emailDto, User user) throws MessagingException {
        Context context = new Context();
        context.setVariable("username", user.getName());
        context.setVariable("applicationName", "CONTROL");
        context.setVariable("link", "http://localhost:4200/auth/password/" + user.getId());

        String contentHtml = templateEngine.process("sendEmailAfterRegistration", context);

        emailDto.setMessage(contentHtml);

        sendEmail(emailDto);
    }
}
