package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.service.MantenimientoVigenteService;
import ar.edu.um.isa.oncall.service.dto.MantenimientoVigenteDTO;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

/**
 * Consulta de la regla de ventanas de mantenimiento. Aparte de {@link ServicioResource}, que es
 * el ABM generado.
 */
@RestController
@RequestMapping("/api/servicios")
public class MantenimientoVigenteResource {

    private static final Logger LOG = LoggerFactory.getLogger(MantenimientoVigenteResource.class);

    private final MantenimientoVigenteService mantenimientoVigenteService;

    public MantenimientoVigenteResource(MantenimientoVigenteService mantenimientoVigenteService) {
        this.mantenimientoVigenteService = mantenimientoVigenteService;
    }

    /**
     * {@code GET /api/servicios/:id/mantenimiento} : si el servicio esta en una ventana de
     * mantenimiento.
     *
     * @param id el servicio.
     * @param en instante a consultar, en ISO-8601 con zona. Si no viene, se toma el ahora.
     * @return {@code 200} con la respuesta (este o no en mantenimiento), o {@code 404} si el
     *         servicio no existe.
     */
    @GetMapping("/{id}/mantenimiento")
    public ResponseEntity<MantenimientoVigenteDTO> consultarMantenimiento(
        @PathVariable("id") Long id,
        @RequestParam(name = "en", required = false) Instant en
    ) {
        LOG.debug("REST request para el mantenimiento del Servicio {} en {}", id, en);
        return ResponseUtil.wrapOrNotFound(mantenimientoVigenteService.consultar(id, en != null ? en : Instant.now()));
    }
}
