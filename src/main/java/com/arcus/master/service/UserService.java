package com.arcus.master.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.arcus.master.entity.UserEntity;

@Component
public interface UserService {
	
	Page<UserEntity> findAll(int page, int count);
	
	UserEntity findByUsuario(String usuario);
	
	UserEntity findById(int id);
	
	UserEntity crateOrUpdate(UserEntity user);

	void deleteById(Integer id);

}