package com.puemape.rest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.thymeleaf.context.Context;

import com.puemape.converter.EventoConverter;
import com.puemape.dto.EventoDto;
import com.puemape.entity.Evento;
import com.puemape.service.EventoService;
import com.puemape.service.PdfService;
import com.puemape.util.WrapperResponse;

@RestController
@RequestMapping("/v1/eventos")
public class EventoController {

    @Autowired
    private EventoService service;

    @Autowired
    private EventoConverter converter;

    @Autowired
    private PdfService pdfService;

    @GetMapping
    public ResponseEntity<List<EventoDto>> findAll(
            @RequestParam(value = "offset", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        List<EventoDto> eventos = converter.fromEntity(service.findAll(page));

        return new WrapperResponse(true, "success", eventos).createResponse(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventoDto> create(@RequestBody EventoDto evento) {
        Evento eventoEntity = converter.fromDTO(evento);
        EventoDto registro = converter.fromEntity(service.save(eventoEntity));
        return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoDto> update(@PathVariable("id") int id, @RequestBody EventoDto evento) {
        Evento eventoEntity = converter.fromDTO(evento);
        eventoEntity.setId(id); // Aseguramos que se actualiza el registro correspondiente
        EventoDto registro = converter.fromEntity(service.save(eventoEntity));
        return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        service.delete(id);
        return new WrapperResponse(true, "success", null).createResponse(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoDto> findById(@PathVariable("id") int id) {
        EventoDto registro = converter.fromEntity(service.findById(id));
        return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
    }

    @GetMapping("/byNombre")
    public ResponseEntity<List<EventoDto>> findByNombre(
            @RequestParam String nombre,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        List<EventoDto> eventos = converter.fromEntity(service.findByNombre(nombre, page));
        return new WrapperResponse(true, "success", eventos).createResponse(HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> generateReport() {
        List<EventoDto> eventos = converter.fromEntity(service.findAll());

        LocalDateTime fecha = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaHora = fecha.format(formato);

        Context context = new Context();
        context.setVariable("registros", eventos);
        context.setVariable("fecha", fechaHora);

        byte[] pdfBytes = pdfService.createPdf("eventoReport", context);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "reporte_eventos.pdf");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}
