package com.arcus.master.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.arcus.master.entity.UserEntity;
import com.arcus.master.enums.ProfileEnum;

public class JwtUserFactory {

	private JwtUserFactory(){
	}
	
	public static JwtUser create(UserEntity user) {
		return new JwtUser(
				user.getUsuario(),
				user.getSenha(),
				mapToGrantedAuthorities(user.getPerfil())
		);
	}
	
	private static List<GrantedAuthority> mapToGrantedAuthorities(ProfileEnum profileEnum){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(profileEnum.toString()));
		return authorities;		
	}
}
