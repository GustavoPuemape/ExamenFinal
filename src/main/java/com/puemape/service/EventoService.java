package com.puemape.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.puemape.entity.Evento;

public interface EventoService {

    List<Evento> findAll(Pageable page);

    List<Evento> findAll();

    List<Evento> findByNombre(String nombre, Pageable page);

    List<Evento> findByDescripcion(String descripcion, Pageable page);

    Evento findById(int id);

    Evento save(Evento evento);

    void delete(int id);
}
