package com.tinomaster.virtualdream.virtualDream.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tinomaster.virtualdream.virtualDream.dtos.AuthLoginDto;
import com.tinomaster.virtualdream.virtualDream.dtos.AuthRegisterDto;
import com.tinomaster.virtualdream.virtualDream.dtos.response.AuthOwnerRegisterResponse;
import com.tinomaster.virtualdream.virtualDream.dtos.response.LoginResponse;
import com.tinomaster.virtualdream.virtualDream.dtos.response.ResponseBody;
import com.tinomaster.virtualdream.virtualDream.dtos.response.ResponseType;
import com.tinomaster.virtualdream.virtualDream.services.AuthenticationService;

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
		return ResponseType.ok("successfullyRegister", authService.registerOwner(authRegister));
	}

	@PostMapping("/public/authenticate")
	public ResponseEntity<ResponseBody<LoginResponse>> authenticate(@RequestBody AuthLoginDto authLogin) {
		return ResponseType.ok("successfullyLogin", authService.authenticate(authLogin));
	}

}
