package com.tinomaster.virtualdream.virtualDream.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinomaster.virtualdream.virtualDream.dtos.AuthLoginDto;
import com.tinomaster.virtualdream.virtualDream.dtos.AuthRegisterDto;
import com.tinomaster.virtualdream.virtualDream.dtos.AuthResponseDto;
import com.tinomaster.virtualdream.virtualDream.dtos.EmailDto;
import com.tinomaster.virtualdream.virtualDream.entities.User;
import com.tinomaster.virtualdream.virtualDream.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final EmailService emailService;
	private final UserRepository userRepository;

	public AuthResponseDto register(AuthRegisterDto registerDto) {
		var user = User.builder().name(registerDto.getName()).email(registerDto.getEmail()).active(true)
				.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
				.password(passwordEncoder.encode(registerDto.getPassword())).role(registerDto.getRole()).active(true)
				.businessId(registerDto.getBusinessId()).build();

		User registeredUser = userRepository.save(user);

		String token = jwtService.generateToken(registeredUser);
		String refreshToken = jwtService.generateRefreshToken(registeredUser);

		try {
			EmailDto emailDto = new EmailDto();
			emailDto.setDestination(registeredUser.getEmail());
			emailDto.setSubject("Te damos la Bienvenida a nuestra aplicacion!");
			emailDto.setMessage("Gentil Usuario " + user.getName() + ", le informamos que ....");

			emailService.sendEmailAfterRegisterUser(emailDto, registeredUser);
		} catch (Exception e) {
			System.err.println("Ha ocurrido un error mientras se enviaba el correo de registro" + e.getMessage());
		}

		return AuthResponseDto.builder().token(token).refreshToken(refreshToken).role(registeredUser.getRole()).build();
	}

	public AuthResponseDto authenticate(AuthLoginDto authRequestDto) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));

		User user = userRepository.findByEmail(authRequestDto.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		System.out.println("Usuario autenticado: " + user);
		String jwtToken = jwtService.generateToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);

		return AuthResponseDto.builder().token(jwtToken).refreshToken(refreshToken).role(user.getRole()).build();
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response)
			throws StreamWriteException, DatabindException, IOException {

		final String authHeader = request.getHeader("Authorization");
		final String refreshToken;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}

		refreshToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(refreshToken);
		if (userEmail != null) {
			var user = this.userRepository.findByEmail(userEmail).orElseThrow();
			if (jwtService.isTokenValid(refreshToken, user)) {
				var accessToken = jwtService.generateToken(user);
				var authResponse = AuthResponseDto.builder().token(accessToken).refreshToken(refreshToken).build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}

}
