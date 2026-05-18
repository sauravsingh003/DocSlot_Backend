package com.app.Controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.DTO.AuthReq;
import com.app.DTO.AuthResp;
import com.app.jwt_utils.JwtUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

	// JWT Utility
	@Autowired
	private JwtUtils utils;

	// Authentication Manager
	@Autowired
	private AuthenticationManager manager;

	@PostMapping
	public ResponseEntity<?> validateUserCreateToken(@RequestBody @Valid AuthReq request) {

		// Create authentication token
		UsernamePasswordAuthenticationToken authToken =
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword());

		System.out.println("Auth token: " + authToken);

		try {

			// Authenticate user
			Authentication authenticatedDetails = manager.authenticate(authToken);

			System.out.println("Authenticated user: " + authenticatedDetails.getName());

			// Generate JWT token
			String jwtToken = utils.generateJwtToken(authenticatedDetails);

			// Return clean response
			return ResponseEntity.ok(
					new AuthResp(
							"Auth successful!",
							jwtToken
					)
			);

		} catch (BadCredentialsException e) {

			System.out.println("Authentication failed: " + e.getMessage());

			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body("Invalid email or password!");
		}
	}
}