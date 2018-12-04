package com.arcus.master.security.jwt.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arcus.master.entity.UserEntity;
import com.arcus.master.security.jwt.JwtAuthenticationRequest;
import com.arcus.master.security.jwt.JwtTokenUtil;
import com.arcus.master.security.model.CurrentUserModel;
import com.arcus.master.service.UserService;

@RestController
@CrossOrigin(origins = "*")
public class AuthenticationRestController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping(value = "/auth")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws Exception{
		
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						authenticationRequest.getEmail(),
						authenticationRequest.getPassword())
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		final UserEntity user = userService.findByUsuario(authenticationRequest.getEmail());
		user.setSenha(null);
		return ResponseEntity.ok(new CurrentUserModel(token, user));
	}
	
	@PostMapping(value = "/refresh")
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request){
		String token = request.getHeader("Authorization");
		String username = jwtTokenUtil.getUserNameFromToken(token);
		final UserEntity user = userService.findByUsuario(username);
		
		if(jwtTokenUtil.canTokenBeRefreshed(token)) {
			String refreshedtoken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new CurrentUserModel(refreshedtoken, user));
		}else {
			return ResponseEntity.badRequest().body(null);
		}
	}
}
