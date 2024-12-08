package com.tinomaster.virtualdream.virtualDream.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tinomaster.virtualdream.virtualDream.dtos.AuthLoginDto;
import com.tinomaster.virtualdream.virtualDream.dtos.AuthRegisterDto;
import com.tinomaster.virtualdream.virtualDream.dtos.AuthResponseDto;
import com.tinomaster.virtualdream.virtualDream.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationService authService;

	@PostMapping("/public/register")
	public ResponseEntity<AuthResponseDto> registerOwner(@RequestBody AuthRegisterDto authRegister) {
		return ResponseEntity.ok(authService.registerOwner(authRegister));
	}

	@PostMapping("/public/authenticate")
	public ResponseEntity<AuthResponseDto> authenticate(@RequestBody AuthLoginDto authLogin) {
		System.out.println(authLogin);
		return ResponseEntity.ok(authService.authenticate(authLogin));
	}

}
