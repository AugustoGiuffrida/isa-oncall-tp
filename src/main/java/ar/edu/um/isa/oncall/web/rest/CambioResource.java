package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.CambioRepository;
import ar.edu.um.isa.oncall.service.CambioService;
import ar.edu.um.isa.oncall.service.dto.CambioDTO;
import ar.edu.um.isa.oncall.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.Cambio}.
 */
@RestController
@RequestMapping("/api/cambios")
public class CambioResource {

    private static final Logger LOG = LoggerFactory.getLogger(CambioResource.class);

    private static final String ENTITY_NAME = "cambio";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final CambioService cambioService;

    private final CambioRepository cambioRepository;

    public CambioResource(CambioService cambioService, CambioRepository cambioRepository) {
        this.cambioService = cambioService;
        this.cambioRepository = cambioRepository;
    }

    /**
     * {@code POST  /cambios} : Create a new cambio.
     *
     * @param cambioDTO the cambioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cambioDTO, or with status {@code 400 (Bad Request)} if the cambio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CambioDTO> createCambio(@Valid @RequestBody CambioDTO cambioDTO) throws URISyntaxException {
        LOG.debug("REST request to save Cambio : {}", cambioDTO);
        if (cambioDTO.getId() != null) {
            throw new BadRequestAlertException("A new cambio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cambioDTO = cambioService.save(cambioDTO);
        return ResponseEntity.created(new URI("/api/cambios/" + cambioDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cambioDTO.getId().toString()))
            .body(cambioDTO);
    }

    /**
     * {@code PUT  /cambios/:id} : Updates an existing cambio.
     *
     * @param id the id of the cambioDTO to save.
     * @param cambioDTO the cambioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cambioDTO,
     * or with status {@code 400 (Bad Request)} if the cambioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cambioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CambioDTO> updateCambio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CambioDTO cambioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Cambio : {}, {}", id, cambioDTO);
        if (cambioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cambioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cambioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cambioDTO = cambioService.update(cambioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cambioDTO.getId().toString()))
            .body(cambioDTO);
    }

    /**
     * {@code PATCH  /cambios/:id} : Partial updates given fields of an existing cambio, field will ignore if it is null
     *
     * @param id the id of the cambioDTO to save.
     * @param cambioDTO the cambioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cambioDTO,
     * or with status {@code 400 (Bad Request)} if the cambioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cambioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cambioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CambioDTO> partialUpdateCambio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CambioDTO cambioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Cambio partially : {}, {}", id, cambioDTO);
        if (cambioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cambioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cambioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CambioDTO> result = cambioService.partialUpdate(cambioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cambioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cambios} : get all the Cambios.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cambios in body.
     */
    @GetMapping("")
    public List<CambioDTO> getAllCambios(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all Cambios");
        return cambioService.findAll();
    }

    /**
     * {@code GET  /cambios/:id} : get the "id" cambio.
     *
     * @param id the id of the cambioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cambioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CambioDTO> getCambio(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Cambio : {}", id);
        Optional<CambioDTO> cambioDTO = cambioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cambioDTO);
    }

    /**
     * {@code DELETE  /cambios/:id} : delete the "id" cambio.
     *
     * @param id the id of the cambioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCambio(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Cambio : {}", id);
        cambioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
