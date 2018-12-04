package com.arcus.master.security.model;

import com.arcus.master.entity.UserEntity;

public class CurrentUserModel {
	
	private String token;
	private UserEntity user;
	
	public CurrentUserModel(String token, UserEntity user) {
		super();
		this.token = token;
		this.user = user;
	}

	public UserEntity getRvuser() {
		return user;
	}

	public void setRvuser(UserEntity user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
