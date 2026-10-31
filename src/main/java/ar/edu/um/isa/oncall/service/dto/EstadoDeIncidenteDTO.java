package ar.edu.um.isa.oncall.service.dto;

import ar.edu.um.isa.oncall.domain.enumeration.EstadoIncidente;
import java.io.Serializable;
import java.time.Instant;

/**
 * Como quedo un incidente despues de reconocerlo o silenciarlo.
 *
 * @param incidenteId el incidente.
 * @param estado el estado resultante.
 * @param reconocidoEn marca de reconocimiento.
 * @param silenciadoHasta hasta cuando esta en pausa el escalamiento, si lo esta.
 * @param comandante el login del comandante.
 * @param cambio {@code false} si la accion no cambio nada (por ejemplo, reconocer dos veces).
 * @param mensaje que se hizo, en castellano.
 */
public record EstadoDeIncidenteDTO(
    Long incidenteId,
    EstadoIncidente estado,
    Instant reconocidoEn,
    Instant silenciadoHasta,
    String comandante,
    boolean cambio,
    String mensaje
) implements Serializable {}
