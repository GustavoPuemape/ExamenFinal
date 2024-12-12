package com.puemape.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.puemape.entity.Evento;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {
    List<Evento> findByNombreContaining(String nombre, org.springframework.data.domain.Pageable page);
    List<Evento> findByDescripcionContaining(String descripcion, org.springframework.data.domain.Pageable page);
}
