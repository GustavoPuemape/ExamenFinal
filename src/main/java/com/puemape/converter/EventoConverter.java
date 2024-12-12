package com.puemape.converter;

import org.springframework.stereotype.Component;

import com.puemape.dto.EventoDto;
import com.puemape.entity.Evento;

@Component
public class EventoConverter extends AbstractConverter<Evento, EventoDto> {

    @Override
    public EventoDto fromEntity(Evento entity) {
        if (entity == null) return null;
        return EventoDto.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .fechaInicio(entity.getFechaInicio() != null ? entity.getFechaInicio().toString() : null)
                .fechaFin(entity.getFechaFin() != null ? entity.getFechaFin().toString() : null)
                .costo(entity.getCosto())
                .build();
    }

    @Override
    public Evento fromDTO(EventoDto dto) {
        if (dto == null) return null;
        return Evento.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .fechaInicio(dto.getFechaInicio() != null ? java.sql.Date.valueOf(dto.getFechaInicio()) : null)
                .fechaFin(dto.getFechaFin() != null ? java.sql.Date.valueOf(dto.getFechaFin()) : null)
                .costo(dto.getCosto())
                .build();
    }
}
