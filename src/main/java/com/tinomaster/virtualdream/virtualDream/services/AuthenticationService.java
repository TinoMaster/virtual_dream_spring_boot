package com.tinomaster.virtualdream.virtualDream.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinomaster.virtualdream.virtualDream.dtos.AddressDto;
import com.tinomaster.virtualdream.virtualDream.dtos.AuthLoginDto;
import com.tinomaster.virtualdream.virtualDream.dtos.AuthRegisterDto;
import com.tinomaster.virtualdream.virtualDream.dtos.AuthResponseDto;
import com.tinomaster.virtualdream.virtualDream.dtos.BusinessDto;
import com.tinomaster.virtualdream.virtualDream.dtos.EmailDto;
import com.tinomaster.virtualdream.virtualDream.entities.Address;
import com.tinomaster.virtualdream.virtualDream.entities.Business;
import com.tinomaster.virtualdream.virtualDream.entities.User;
import com.tinomaster.virtualdream.virtualDream.enums.ERole;
import com.tinomaster.virtualdream.virtualDream.exceptions.InvalidRoleException;
import com.tinomaster.virtualdream.virtualDream.repositories.AddressRepository;
import com.tinomaster.virtualdream.virtualDream.repositories.BusinessRepository;
import com.tinomaster.virtualdream.virtualDream.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final EmailService emailService;
	private final UserRepository userRepository;
	private final BusinessRepository businessRepository;
	private final AddressRepository addressRepository;

	@Transactional
	public AuthResponseDto registerOwner(AuthRegisterDto registerDto) {
	    System.out.println(registerDto);

	    if (registerDto.getRole() != ERole.OWNER) {
	        throw new InvalidRoleException("El rol proporcionado no es válido para registrar un propietario.");
	    }

	    BusinessDto businessDto = registerDto.getBusiness();
	    if (businessDto == null) {
	        throw new IllegalArgumentException("Se requiere el negocio para registrar un propietario.");
	    }

	    // Guardar la dirección
	    Address addressToSave = Address.builder()
	            .street(businessDto.getAddress().getStreet())
	            .number(businessDto.getAddress().getNumber())
	            .city(businessDto.getAddress().getCity())
	            .zip(businessDto.getAddress().getZip())
	            .build();
	    Address savedAddress = addressRepository.save(addressToSave);

	    if (savedAddress == null) {
	        throw new IllegalStateException("Error al guardar la dirección.");
	    }

	    // Guardar el usuario primero
	    User user = User.builder()
	            .name(registerDto.getName())
	            .email(registerDto.getEmail())
	            .password(passwordEncoder.encode(registerDto.getPassword()))
	            .role(registerDto.getRole())
	            .active(false)
	            .createdAt(LocalDateTime.now())
	            .updatedAt(LocalDateTime.now())
	            .build();
	    User registeredUser = userRepository.save(user);

	    if (registeredUser == null) {
	        throw new IllegalStateException("Error al guardar el usuario.");
	    }

	    // Guardar el negocio con el usuario como propietario
	    Business newBusiness = Business.builder()
	            .name(businessDto.getName())
	            .email(businessDto.getEmail())
	            .phone(businessDto.getPhone())
	            .description(businessDto.getDescription())
	            .address(savedAddress) // Asigna la dirección guardada
	            .owner(registeredUser) // Asigna el usuario como propietario
	            .createdAt(LocalDateTime.now())
	            .updatedAt(LocalDateTime.now())
	            .build();
	    Business savedBusiness = businessRepository.save(newBusiness);

	    if (savedBusiness == null) {
	        throw new IllegalStateException("Error al guardar el negocio.");
	    }
	    

	    // Actualizar el usuario con el negocio guardado
	    List<Business> businesses = new ArrayList<>();
	    businesses.add(savedBusiness);
	    registeredUser.setBusinesses(businesses);
	    userRepository.save(registeredUser);

	    // Generar los tokens
	    String token = jwtService.generateToken(registeredUser);
	    String refreshToken = jwtService.generateRefreshToken(registeredUser);

	    // Enviar correo
	    try {
	        EmailDto emailDto = new EmailDto();
	        emailDto.setDestination(registeredUser.getEmail());
	        emailDto.setSubject("¡Te damos la Bienvenida a nuestra aplicación!");
	        emailDto.setMessage("Gentil Usuario " + user.getName() + ", le informamos que ....");

	        emailService.sendEmailAfterRegisterUser(emailDto, registeredUser);
	    } catch (Exception e) {
	        System.err.println("Ha ocurrido un error mientras se enviaba el correo de registro: " + e.getMessage());
	    }

	    return AuthResponseDto.builder()
	            .token(token)
	            .refreshToken(refreshToken)
	            .role(registeredUser.getRole())
	            .build();
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
