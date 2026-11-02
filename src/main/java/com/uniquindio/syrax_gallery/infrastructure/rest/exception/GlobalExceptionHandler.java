package com.uniquindio.syrax_gallery.infrastructure.rest.exception;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 *  - 400: la petición viene mal (falla @Valid, JSON mal formado, parámetro inválido, página negativa).
 *  - 404: el recurso no existe.
 *  - 422: la petición está bien, pero el dominio la rechaza porque rompe una regla del negocio.
 *  - 401 y 403 los responde la seguridad (RespuestasSeguridad), antes de llegar al controller.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReglaDominioException.class)
    public ResponseEntity<Map<String, Object>> manejarReglaDominio(ReglaDominioException ex) {
        return construirRespuesta(HttpStatus.valueOf(422), ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(NoSuchElementException ex) {
        return construirRespuesta(HttpStatus.NOT_FOUND, "El recurso solicitado no existe.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return construirRespuesta(HttpStatus.BAD_REQUEST, mensaje);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> manejarCuerpoInvalido(HttpMessageNotReadableException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición no es válido: revise el formato del JSON y los valores permitidos.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> manejarParametroInvalido(MethodArgumentTypeMismatchException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST,
                "El parámetro '" + ex.getName() + "' tiene un valor no válido: " + ex.getValue());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> manejarParametroFaltante(MissingServletRequestParameterException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST,
                "Falta el parámetro obligatorio '" + ex.getParameterName() + "'");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarEntradaInvalida(IllegalArgumentException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> construirRespuesta(HttpStatus status, String mensaje) {
        Map<String, Object> cuerpo = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", status.value(),
                "mensaje", mensaje
        );

        return ResponseEntity.status(status).body(cuerpo);
    }
}
