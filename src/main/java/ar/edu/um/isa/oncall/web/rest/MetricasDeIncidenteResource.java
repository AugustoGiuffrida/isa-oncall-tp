package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.service.MetricasDeIncidenteService;
import ar.edu.um.isa.oncall.service.dto.EvaluacionDeIncidenteDTO;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

/**
 * Endpoints de la regla de tiempos contra los SLOs. Aparte de {@link IncidenteResource}, que es el ABM.
 */
@RestController
@RequestMapping("/api")
public class MetricasDeIncidenteResource {

    private static final Logger LOG = LoggerFactory.getLogger(MetricasDeIncidenteResource.class);

    private final MetricasDeIncidenteService metricasDeIncidenteService;

    public MetricasDeIncidenteResource(MetricasDeIncidenteService metricasDeIncidenteService) {
        this.metricasDeIncidenteService = metricasDeIncidenteService;
    }

    /**
     * {@code GET /api/incidentes/:id/metricas} : tiempos del incidente contra sus objetivos.
     * No modifica nada.
     *
     * @param id el incidente.
     * @return {@code 200} con la evaluacion, o {@code 404} si el incidente no existe.
     */
    @GetMapping("/incidentes/{id}/metricas")
    public ResponseEntity<EvaluacionDeIncidenteDTO> evaluarIncidente(@PathVariable("id") Long id) {
        LOG.debug("REST request para evaluar el Incidente {}", id);
        return ResponseUtil.wrapOrNotFound(metricasDeIncidenteService.evaluar(id, Instant.now()));
    }

    /**
     * {@code POST /api/incidentes/:id/metricas/cumplimiento} : evalua el incidente y, si ya
     * termino, guarda el veredicto en {@code cumplioObjetivo}.
     *
     * @param id el incidente.
     * @return {@code 200} con la evaluacion, o {@code 404} si el incidente no existe.
     */
    @PostMapping("/incidentes/{id}/metricas/cumplimiento")
    public ResponseEntity<EvaluacionDeIncidenteDTO> registrarCumplimiento(@PathVariable("id") Long id) {
        LOG.debug("REST request para registrar el cumplimiento del Incidente {}", id);
        return ResponseUtil.wrapOrNotFound(metricasDeIncidenteService.registrarCumplimiento(id, Instant.now()));
    }
}
