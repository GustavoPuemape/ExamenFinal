package com.puemape.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.puemape.entity.Evento;
import com.puemape.repository.EventoRepository;
import com.puemape.service.EventoService;
import com.puemape.validator.EventoValidator;

@Service
public class EventoServiceImpl implements EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Override
    public List<Evento> findAll(Pageable page) {
        return eventoRepository.findAll(page).getContent();
    }

    @Override
    public List<Evento> findAll() {
        return eventoRepository.findAll();
    }

    @Override
    public List<Evento> findByNombre(String nombre, Pageable page) {
        return eventoRepository.findByNombreContaining(nombre, page);
    }

    @Override
    public List<Evento> findByDescripcion(String descripcion, Pageable page) {
        return eventoRepository.findByDescripcionContaining(descripcion, page);
    }

    @Override
    public Evento findById(int id) {
        return eventoRepository.findById(id).orElse(null);
    }

    @Override
    public Evento save(Evento evento) {
        EventoValidator.validate(evento);
        return eventoRepository.save(evento);
    }

    @Override
    public void delete(int id) {
        eventoRepository.deleteById(id);
    }
}
