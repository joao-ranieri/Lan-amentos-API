package com.arcus.master.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arcus.master.entity.UserEntity;
import com.arcus.master.response.Response;
import com.arcus.master.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private UserService usersService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
		
	// METODO QUE LISTA TODOS OS USUARIO
	@GetMapping(value = "/listar/{page}/{count}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	private ResponseEntity<Response<Page<UserEntity>>> findAll(@PathVariable int page, @PathVariable int count){
		
		Response<Page<UserEntity>> response = new Response<Page<UserEntity>>();
		Page<UserEntity> users = usersService.findAll(page, count);
		
		if(users != null) {
			try {
				response.setData(users);
				return ResponseEntity.ok(response);
				
			} catch (Exception e) {
				response.getErrors().add(e.getMessage());
				return ResponseEntity.badRequest().body(response);
			}
			
		}else {
			response.getErrors().add("Nenhum usuário encontrado");
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	// METODO DE BUSCA PELO NOME DO USUARIO
	@GetMapping(value = "/listar/username/{nome}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	private ResponseEntity<Response<UserEntity>> findByUser(@PathVariable("nome") String nome){
		
		Response<UserEntity> response = new Response<UserEntity>();
		UserEntity user = usersService.findByUsuario(nome);
				
		if(user == null) {
			response.getErrors().add("Usuário " + nome + "não encontrado");
			return ResponseEntity.badRequest().body(response);

		}else {		
			response.setData(user);
			return ResponseEntity.ok(response);
		}
	}
	
	// METODO DE BUSCA PELO ID DO USUARIO
	@GetMapping(value = "/listar/userid/{id}")
	@PreAuthorize("hasAnyError('ADMIN')")
	private ResponseEntity<Response<UserEntity>> findById(@PathVariable("id") Integer id) {
		
		Response<UserEntity> response = new Response<UserEntity>();
		UserEntity user = usersService.findById(id);
		
		if(user == null) {
			response.getErrors().add("Usuário não encontrado");
			return ResponseEntity.badRequest().body(response);
			
		}else {
			response.setData(user);
			return ResponseEntity.ok(response);
		}

	}
	
	// METODO PARA CRIAR UM USUARIO
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<UserEntity>> create(HttpServletRequest request,
			@RequestBody UserEntity user, BindingResult result){
		
		Response<UserEntity> response = new Response<UserEntity>();
		UserEntity userTemp = usersService.findByUsuario(user.getUsuario());
		
		if(userTemp == null) {
			try {
				validateCreateUser(user,  result);
				if(result.hasErrors()) {
					result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
					return ResponseEntity.badRequest().body(response);
				}
				
				user.setSenha(passwordEncoder.encode(user.getSenha()));
				UserEntity userPersisted = (UserEntity) usersService.crateOrUpdate(user);
				response.setData(userPersisted);
				
			}catch (Exception e) {
				response.getErrors().add(e.getMessage());
				return ResponseEntity.badRequest().body(response);
			}
		}else {
			response.getErrors().add("Usuário já cadastrado!");
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	// METODO PARA CRIAR OU ALTERAR UM USUARIO
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<UserEntity>> update(HttpServletRequest request,
			@RequestBody UserEntity user, @PathVariable("id") Integer id ,BindingResult result){
		
		Response<UserEntity> response = new Response<UserEntity>();
		UserEntity userTemp = usersService.findById(id);
		
		if(userTemp != null) {
			try {
				validateCreateUser(user,  result);
				if(result.hasErrors()) {
					result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
					return ResponseEntity.badRequest().body(response);
				}
				
				user.setId(userTemp.getId());
				user.setSenha(passwordEncoder.encode(user.getSenha()));
				UserEntity userPersisted = (UserEntity) usersService.crateOrUpdate(user);
				response.setData(userPersisted);
				
			}catch (Exception e) {
				response.getErrors().add(e.getMessage());
				return ResponseEntity.badRequest().body(response);
			}
		}else {
			response.getErrors().add("Usuário não encontrado!");
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") Integer id){
		
		Response<String> response = new Response<String>();
		UserEntity user = usersService.findById(id);
		
		if(user == null){
			response.getErrors().add("Usuário não encontrado!");
			return ResponseEntity.badRequest().body(response);
		}
		
		usersService.deleteById(id);
		response.setData("Usuário excluido.");
		return ResponseEntity.ok(response);
	}
	
	//VALIDA OS DADOS QUE SERAO INSERIDOS
	private void validateCreateUser(UserEntity user, BindingResult result) {
		
		String error = "Dados inválidos.";
		
		if(user.getUsuario() == null || user.getSenha() == null || user.getPerfil() == null) {	
			result.addError(new ObjectError("User", error));
		
		}else if(user.getUsuario().isEmpty() || user.getSenha().isEmpty()) {
			result.addError(new ObjectError("User", error));
		}
	}
	
}
