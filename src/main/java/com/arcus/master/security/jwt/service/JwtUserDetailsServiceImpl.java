package com.arcus.master.security.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.arcus.master.entity.UserEntity;
import com.arcus.master.security.jwt.JwtUserFactory;
import com.arcus.master.service.UserService;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserService rvuserService;
	
	@Override
	public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException{
		UserEntity userAux = rvuserService.findByUsuario(user);
		if(user == null) {
			throw new UsernameNotFoundException(String.format("Nenhum usuário encontrado com o nome '%s'.", user));
		}else {
			return JwtUserFactory.create(userAux);
		}
	}
	
}