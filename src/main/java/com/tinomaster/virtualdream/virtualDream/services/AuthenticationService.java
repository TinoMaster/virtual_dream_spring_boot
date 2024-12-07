package com.tinomaster.virtualdream.virtualDream.services;

import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualDream.dtos.AuthRegisterDto;
import com.tinomaster.virtualdream.virtualDream.dtos.AuthRequestDto;
import com.tinomaster.virtualdream.virtualDream.dtos.AuthResponseDto;
import com.tinomaster.virtualdream.virtualDream.dtos.EmailDto;
import com.tinomaster.virtualdream.virtualDream.entities.User;
import com.tinomaster.virtualdream.virtualDream.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final EmailService emailService;
	private final UserRepository userRepository;

	public void register(AuthRegisterDto registerDto) {
		var user = User.builder().name(registerDto.getName()).email(registerDto.getEmail())
				.password(passwordEncoder.encode(new Random().ints(8, 33, 122).mapToObj(i -> String.valueOf((char) i))
						.collect(Collectors.joining())))
				.role(registerDto.getRole()).active(true).businessId(registerDto.getBusinessId()).build();

		User registeredUser = userRepository.save(user);

		try {
			EmailDto emailDto = new EmailDto();
			emailDto.setDestination(user.getEmail());
			emailDto.setSubject("Te damos la Bienvenida a nuestra aplicacion!");
			emailDto.setMessage("Gentil Usuario " + user.getName() + ", le informamos que ....");

			emailService.sendEmailAfterRegisterUser(emailDto, registeredUser);
		} catch (Exception e) {
			System.err.println("Ha ocurrido un error mientras se enviaba el correo de registro" + e.getMessage());
		}
	}

	public AuthResponseDto authenticate(AuthRequestDto authRequestDto) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));

		User user = userRepository.findByEmail(authRequestDto.getEmail()).orElseThrow();
		String jwtToken = jwtService.generateToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);

		return AuthResponseDto.builder().token(jwtToken).refreshToken(refreshToken).role(user.getRole()).build();
	}

}
