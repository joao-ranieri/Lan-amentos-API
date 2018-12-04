package com.arcus.master.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.arcus.master.enums.ProfileEnum;

@Entity(name = "USUARIOS")
public class UserEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int id;
	
	@NotBlank
	@Size(min = 1, max = 50)
	@NotNull(message = "Campo obrigatório")
	public String usuario;
	
	@NotBlank
	@Size(min = 4)
	@NotNull(message = "Campo obrigatório")
	public String senha;
		
	@NotNull(message = "Campo obrigatório")
	@Enumerated(EnumType.STRING)
	private ProfileEnum perfil;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public ProfileEnum getPerfil() {
		return perfil;
	}

	public void setPerfil(ProfileEnum perfil) {
		this.perfil = perfil;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
