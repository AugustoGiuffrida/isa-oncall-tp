package ar.edu.um.isa.oncall.service.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * Si un servicio esta en mantenimiento en un instante, y por que ventana.
 *
 * @param servicioId el servicio.
 * @param servicioNombre su nombre.
 * @param instante el instante consultado.
 * @param enMantenimiento si alguna ventana activa lo cubre.
 * @param ventanaId la ventana que lo cubre, o {@code null}.
 * @param ventanaNombre su nombre.
 * @param desde inicio de la ocurrencia que lo cubre.
 * @param hasta fin de la ocurrencia que lo cubre (exclusive).
 * @param reprogramada {@code true} si esa ocurrencia fue movida por una excepcion.
 */
public record MantenimientoVigenteDTO(
    Long servicioId,
    String servicioNombre,
    Instant instante,
    boolean enMantenimiento,
    Long ventanaId,
    String ventanaNombre,
    Instant desde,
    Instant hasta,
    boolean reprogramada
) implements Serializable {}
