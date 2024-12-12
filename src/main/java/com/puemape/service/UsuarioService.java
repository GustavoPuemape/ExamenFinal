package com.puemape.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.puemape.entity.Usuario;

public interface UsuarioService {
	public List<Usuario> findAll(Pageable page);
	public List<Usuario> findAll();
	public Usuario findByEmail(String email);
	public Usuario findById(int id);
	public Usuario save(Usuario usuario);
	public Usuario deactivate(int id);
	public Usuario activate(int id);
}