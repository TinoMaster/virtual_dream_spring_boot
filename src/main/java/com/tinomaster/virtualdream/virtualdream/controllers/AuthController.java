package com.tinomaster.virtualdream.virtualdream.controllers;

import java.io.IOException;

import com.tinomaster.virtualdream.virtualdream.dtos.SuperAdminDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tinomaster.virtualdream.virtualdream.dtos.AuthLoginDto;
import com.tinomaster.virtualdream.virtualdream.dtos.AuthRegisterDto;
import com.tinomaster.virtualdream.virtualdream.dtos.UserDto;
import com.tinomaster.virtualdream.virtualdream.dtos.response.AuthOwnerRegisterResponse;
import com.tinomaster.virtualdream.virtualdream.dtos.response.LoginResponse;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualdream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualdream.services.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthenticationService authService;

    @PostMapping("/public/register")
    public ResponseEntity<ResponseBody<AuthOwnerRegisterResponse>> registerOwner(
            @RequestBody AuthRegisterDto authRegister) {
        try {
            return ResponseType.ok("successfullyRegister", authService.registerOwner(authRegister));
        } catch (Exception e) {
            return ResponseType.badRequest(e.getMessage(), null);
        }
    }

    @PostMapping("/public/register/superadmin")
    public ResponseEntity<ResponseBody<LoginResponse>> registerAdmin(@RequestBody SuperAdminDto superAdminDto) {
        try {
            return ResponseType.ok("successfullyRegister", authService.registerAdmin(superAdminDto));
        } catch (Exception e) {
            return ResponseType.badRequest(e.getMessage(), null);
        }

    }

    @PostMapping("/public/authenticate")
    public ResponseEntity<ResponseBody<LoginResponse>> authenticate(@RequestBody AuthLoginDto authLogin) {
        LoginResponse authLoginToResponse;
        try {
            authLoginToResponse = authService.authenticate(authLogin);
        } catch (Exception e) {
            return ResponseType.badRequest(e.getMessage(), null);
        }
        return ResponseType.ok("successfullyLogin", authLoginToResponse);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        authService.refreshToken(request, response);
    }

}
