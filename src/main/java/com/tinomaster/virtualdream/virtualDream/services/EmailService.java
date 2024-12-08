package com.tinomaster.virtualdream.virtualDream.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.tinomaster.virtualdream.virtualDream.dtos.EmailDto;
import com.tinomaster.virtualdream.virtualDream.entities.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
public class EmailService {

	private final JavaMailSender javaMailSender;
	private final TemplateEngine templateEngine;

	public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
		this.javaMailSender = javaMailSender;
		this.templateEngine = templateEngine;
	}

	public void sendEmailAfterUpdateUser(EmailDto emailDto, User user) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(emailDto.getDestination());
		helper.setSubject(emailDto.getSubject());

		Context context = new Context();
		context.setVariable("userName", user.getName());
		context.setVariable("message", emailDto.getMessage());

		String contentHTML = templateEngine.process("sendEmailAfterUpdateUser", context);

		helper.setText(contentHTML, true);

		javaMailSender.send(message);
	}

	public void sendEmailAfterRegisterUser(EmailDto emailDto, User user) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(emailDto.getDestination());
		helper.setSubject(emailDto.getSubject());

		Context context = new Context();
		context.setVariable("username", user.getName());
		context.setVariable("applicationName", "Sueño Virtual");
		context.setVariable("link", "http://localhost:4200/auth/password/" + user.getId());

		String contentHtml = templateEngine.process("sendEmailAfterRegistration", context);

		helper.setText(contentHtml, true);

		javaMailSender.send(message);
	}
}
