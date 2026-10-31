package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.service.GuardiaVigenteService;
import ar.edu.um.isa.oncall.service.dto.GuardiaVigenteDTO;
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
 * Consulta de la regla de guardia vigente. Aparte de {@link RotacionResource}, que es el ABM
 * que escribe el generador.
 */
@RestController
@RequestMapping("/api/rotaciones")
public class GuardiaVigenteResource {

    private static final Logger LOG = LoggerFactory.getLogger(GuardiaVigenteResource.class);

    private final GuardiaVigenteService guardiaVigenteService;

    public GuardiaVigenteResource(GuardiaVigenteService guardiaVigenteService) {
        this.guardiaVigenteService = guardiaVigenteService;
    }

    /**
     * {@code GET /api/rotaciones/:id/guardia} : quien esta de guardia en la rotacion.
     *
     * @param id la rotacion.
     * @param en instante a consultar, en ISO-8601 con zona ({@code 2026-03-10T03:00:00Z}). Si
     *        no viene, se toma el ahora.
     * @return {@code 200} con la guardia vigente (aunque sea un hueco), o {@code 404} si la
     *         rotacion no existe.
     */
    @GetMapping("/{id}/guardia")
    public ResponseEntity<GuardiaVigenteDTO> consultarGuardia(
        @PathVariable("id") Long id,
        @RequestParam(name = "en", required = false) Instant en
    ) {
        LOG.debug("REST request para la guardia vigente de la Rotacion {} en {}", id, en);
        return ResponseUtil.wrapOrNotFound(guardiaVigenteService.resolver(id, en != null ? en : Instant.now()));
    }
}
