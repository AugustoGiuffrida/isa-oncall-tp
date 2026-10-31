package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.service.EjecucionDeEscalamientoService;
import ar.edu.um.isa.oncall.service.dto.EjecucionDeEscalamientoDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.EjecucionDeEscalamiento}.
 */
@RestController
@RequestMapping("/api/ejecucion-de-escalamientos")
public class EjecucionDeEscalamientoResource {

    private static final Logger LOG = LoggerFactory.getLogger(EjecucionDeEscalamientoResource.class);

    private final EjecucionDeEscalamientoService ejecucionDeEscalamientoService;

    public EjecucionDeEscalamientoResource(EjecucionDeEscalamientoService ejecucionDeEscalamientoService) {
        this.ejecucionDeEscalamientoService = ejecucionDeEscalamientoService;
    }

    /**
     * {@code GET  /ejecucion-de-escalamientos} : get all the Ejecucion De Escalamientos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Ejecucion De Escalamientos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EjecucionDeEscalamientoDTO>> getAllEjecucionDeEscalamientos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of EjecucionDeEscalamientos");
        Page<EjecucionDeEscalamientoDTO> page;
        if (eagerload) {
            page = ejecucionDeEscalamientoService.findAllWithEagerRelationships(pageable);
        } else {
            page = ejecucionDeEscalamientoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ejecucion-de-escalamientos/:id} : get the "id" ejecucionDeEscalamiento.
     *
     * @param id the id of the ejecucionDeEscalamientoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ejecucionDeEscalamientoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EjecucionDeEscalamientoDTO> getEjecucionDeEscalamiento(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EjecucionDeEscalamiento : {}", id);
        Optional<EjecucionDeEscalamientoDTO> ejecucionDeEscalamientoDTO = ejecucionDeEscalamientoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ejecucionDeEscalamientoDTO);
    }
}
