package com.puemape.validator;

import com.puemape.entity.Evento;
import com.puemape.exception.ValidateException;

public class EventoValidator {
    public static void validate(Evento evento) {
        if (evento.getNombre() == null || evento.getNombre().trim().isEmpty()) {
            throw new ValidateException("El nombre del evento es requerido");
        }
        if (evento.getNombre().length() > 100) {
            throw new ValidateException("El nombre del evento no debe tener más de 100 caracteres");
        }

        if (evento.getDescripcion() == null || evento.getDescripcion().trim().isEmpty()) {
            throw new ValidateException("La descripción del evento es requerida");
        }
        if (evento.getDescripcion().length() > 500) {
            throw new ValidateException("La descripción del evento no debe tener más de 500 caracteres");
        }

        if (evento.getFechaInicio() == null) {
            throw new ValidateException("La fecha de inicio del evento es requerida");
        }

        if (evento.getFechaFin() == null) {
            throw new ValidateException("La fecha de fin del evento es requerida");
        }

        if (evento.getFechaFin().before(evento.getFechaInicio())) {
            throw new ValidateException("La fecha de fin no puede ser antes de la fecha de inicio");
        }

        if (evento.getCosto() <= 0) {
            throw new ValidateException("El costo del evento debe ser mayor a 0");
        }
    }
}
