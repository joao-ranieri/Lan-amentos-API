package com.arcus.master.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.arcus.master.entity.UserEntity;
import com.arcus.master.repository.UserRepository;
import com.arcus.master.service.UserService;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository  usersRepository;
	
	@Override
	public Page<UserEntity> findAll(int page, int count) {
		Pageable pages = PageRequest.of(page, count);
		return this.usersRepository.findAll(pages);
	}

	@Override
	public UserEntity findByUsuario(String usuario) {
		return this.usersRepository.findByUsuario(usuario);
	}
	
	@Override
	public UserEntity findById(int id) {
		return this.usersRepository.findById(id);
	}
	
	public UserEntity crateOrUpdate(UserEntity usuario) {
		return this.usersRepository.save(usuario);
	}
	
	
	public void deleteById(Integer id) {
		this.usersRepository.deleteById(id);
	}
	
}
