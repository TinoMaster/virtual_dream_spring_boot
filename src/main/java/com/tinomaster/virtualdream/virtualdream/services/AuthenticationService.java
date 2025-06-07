package com.tinomaster.virtualdream.virtualdream.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tinomaster.virtualdream.virtualdream.dtos.*;
import com.tinomaster.virtualdream.virtualdream.entities.Employee;
import com.tinomaster.virtualdream.virtualdream.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinomaster.virtualdream.virtualdream.dtos.response.AuthOwnerRegisterResponse;
import com.tinomaster.virtualdream.virtualdream.dtos.response.LoginResponse;
import com.tinomaster.virtualdream.virtualdream.entities.Address;
import com.tinomaster.virtualdream.virtualdream.entities.Business;
import com.tinomaster.virtualdream.virtualdream.entities.User;
import com.tinomaster.virtualdream.virtualdream.enums.ERole;
import com.tinomaster.virtualdream.virtualdream.exceptions.InvalidRoleException;
import com.tinomaster.virtualdream.virtualdream.repositories.AddressRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.BusinessRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final AddressRepository addressRepository;
    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    private final EmailService emailService;
    private final JwtService jwtService;


    public LoginResponse registerAdmin(SuperAdminDto superAdminDto) {
        User superAdmin = userRepository.findSuperAdmin();
        if (superAdminDto.getRole() != ERole.SUPERADMIN && superAdmin != null) {
            throw new InvalidRoleException("El rol proporcionado no es valido para registrar");
        }

        User user = User.builder()
                .name(superAdminDto.getName())
                .email(superAdminDto.getEmail())
                .role(superAdminDto.getRole())
                .password(passwordEncoder.encode(superAdminDto.getPassword()))
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return LoginResponse.builder().token(jwtToken).refreshToken(refreshToken).role(user.getRole()).build();
    }

    @Transactional
    public AuthOwnerRegisterResponse registerOwner(AuthRegisterDto registerDto) {

        if (registerDto.getRole() != ERole.OWNER) {
            throw new InvalidRoleException("El rol proporcionado no es válido para registrar un propietario.");
        }

        BusinessDto businessDto = registerDto.getBusiness();
        if (businessDto == null) {
            throw new IllegalArgumentException("Se requiere el negocio para registrar un propietario.");
        }

        Address savedAddress = addressRepository.save(modelMapper.map(businessDto.getAddress(), Address.class));

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

        Business newBusiness = Business.builder()
                .name(businessDto.getName())
                .phone(businessDto.getPhone())
                .description(businessDto.getDescription())
                .address(savedAddress) // Asigna la dirección guardada
                .owner(registeredUser) // Asigna el usuario como propietario
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Business savedBusiness = businessRepository.save(newBusiness);

        Employee employee = Employee.builder()
                .phone(registerDto.getEmployee().getPhone())
                .dni(registerDto.getEmployee().getDni())
                .fixedSalary(registerDto.getEmployee().getFixedSalary())
                .percentSalary(registerDto.getEmployee().getPercentSalary())
                .address(savedAddress)
                .user(registeredUser)
                .build();
        employeeRepository.save(employee);

        List<Business> businesses = new ArrayList<>();
        businesses.add(savedBusiness);
        registeredUser.setBusinessesOwned(businesses);
        userRepository.save(registeredUser);

        try {
            // Enviar email al usuario registrado
            EmailDto emailDto = new EmailDto();
            emailDto.setDestination(registeredUser.getEmail());
            emailDto.setSubject("¡Registro Exitoso - Esperando Aprobación!");

            emailService.sendEmailOwnerRegistrationPending(emailDto, registeredUser);

            // Enviar notificación al superadmin
            User superAdmin = userRepository.findSuperAdmin();
            if (superAdmin != null) {
                EmailDto adminEmailDto = new EmailDto();
                adminEmailDto.setDestination(superAdmin.getEmail());
                adminEmailDto.setSubject("Nuevo Usuario Registrado - Requiere Aprobación");

                emailService.sendAdminNewUserNotification(adminEmailDto, registeredUser, businessDto);

            }
        } catch (Exception e) {
            System.err.println("Ha ocurrido un error mientras se enviaba el correo de registro: " + e.getMessage());
        }

        return AuthOwnerRegisterResponse.builder()
                .success(true)
                .message("Registro satisfactorio")
                .build();
    }


    public LoginResponse authenticate(AuthLoginDto authRequestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword())
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("Error de autenticación: " + e.getMessage());
        }

        User user = userRepository.findByEmail(authRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!user.isActive()) {
            return LoginResponse.builder().active(false).build();
        }

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return LoginResponse.builder().token(jwtToken).refreshToken(refreshToken).role(user.getRole()).active(true).build();
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
                var authResponse = LoginResponse.builder().token(accessToken).refreshToken(refreshToken).build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}
