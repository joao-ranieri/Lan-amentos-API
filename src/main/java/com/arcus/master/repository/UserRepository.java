package com.arcus.master.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.arcus.master.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer>{

	UserEntity findByUsuario(String usuario);
	
	Page<UserEntity> findAll(Pageable pages);
	
	UserEntity findById(int id);

	void deleteById(Integer id);

}
