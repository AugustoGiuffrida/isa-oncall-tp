package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.CorrelacionCambioIncidenteRepository;
import ar.edu.um.isa.oncall.service.CorrelacionCambioIncidenteService;
import ar.edu.um.isa.oncall.service.dto.CorrelacionCambioIncidenteDTO;
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
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.CorrelacionCambioIncidente}.
 */
@RestController
@RequestMapping("/api/correlacion-cambio-incidentes")
public class CorrelacionCambioIncidenteResource {

    private static final Logger LOG = LoggerFactory.getLogger(CorrelacionCambioIncidenteResource.class);

    private static final String ENTITY_NAME = "correlacionCambioIncidente";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final CorrelacionCambioIncidenteService correlacionCambioIncidenteService;

    private final CorrelacionCambioIncidenteRepository correlacionCambioIncidenteRepository;

    public CorrelacionCambioIncidenteResource(
        CorrelacionCambioIncidenteService correlacionCambioIncidenteService,
        CorrelacionCambioIncidenteRepository correlacionCambioIncidenteRepository
    ) {
        this.correlacionCambioIncidenteService = correlacionCambioIncidenteService;
        this.correlacionCambioIncidenteRepository = correlacionCambioIncidenteRepository;
    }

    /**
     * {@code POST  /correlacion-cambio-incidentes} : Create a new correlacionCambioIncidente.
     *
     * @param correlacionCambioIncidenteDTO the correlacionCambioIncidenteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new correlacionCambioIncidenteDTO, or with status {@code 400 (Bad Request)} if the correlacionCambioIncidente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CorrelacionCambioIncidenteDTO> createCorrelacionCambioIncidente(
        @Valid @RequestBody CorrelacionCambioIncidenteDTO correlacionCambioIncidenteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save CorrelacionCambioIncidente : {}", correlacionCambioIncidenteDTO);
        if (correlacionCambioIncidenteDTO.getId() != null) {
            throw new BadRequestAlertException("A new correlacionCambioIncidente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        correlacionCambioIncidenteDTO = correlacionCambioIncidenteService.save(correlacionCambioIncidenteDTO);
        return ResponseEntity.created(new URI("/api/correlacion-cambio-incidentes/" + correlacionCambioIncidenteDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, correlacionCambioIncidenteDTO.getId().toString())
            )
            .body(correlacionCambioIncidenteDTO);
    }

    /**
     * {@code PUT  /correlacion-cambio-incidentes/:id} : Updates an existing correlacionCambioIncidente.
     *
     * @param id the id of the correlacionCambioIncidenteDTO to save.
     * @param correlacionCambioIncidenteDTO the correlacionCambioIncidenteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated correlacionCambioIncidenteDTO,
     * or with status {@code 400 (Bad Request)} if the correlacionCambioIncidenteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the correlacionCambioIncidenteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CorrelacionCambioIncidenteDTO> updateCorrelacionCambioIncidente(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CorrelacionCambioIncidenteDTO correlacionCambioIncidenteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CorrelacionCambioIncidente : {}, {}", id, correlacionCambioIncidenteDTO);
        if (correlacionCambioIncidenteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, correlacionCambioIncidenteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!correlacionCambioIncidenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        correlacionCambioIncidenteDTO = correlacionCambioIncidenteService.update(correlacionCambioIncidenteDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, correlacionCambioIncidenteDTO.getId().toString())
            )
            .body(correlacionCambioIncidenteDTO);
    }

    /**
     * {@code PATCH  /correlacion-cambio-incidentes/:id} : Partial updates given fields of an existing correlacionCambioIncidente, field will ignore if it is null
     *
     * @param id the id of the correlacionCambioIncidenteDTO to save.
     * @param correlacionCambioIncidenteDTO the correlacionCambioIncidenteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated correlacionCambioIncidenteDTO,
     * or with status {@code 400 (Bad Request)} if the correlacionCambioIncidenteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the correlacionCambioIncidenteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the correlacionCambioIncidenteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CorrelacionCambioIncidenteDTO> partialUpdateCorrelacionCambioIncidente(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CorrelacionCambioIncidenteDTO correlacionCambioIncidenteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CorrelacionCambioIncidente partially : {}, {}", id, correlacionCambioIncidenteDTO);
        if (correlacionCambioIncidenteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, correlacionCambioIncidenteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!correlacionCambioIncidenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CorrelacionCambioIncidenteDTO> result = correlacionCambioIncidenteService.partialUpdate(correlacionCambioIncidenteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, correlacionCambioIncidenteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /correlacion-cambio-incidentes} : get all the Correlacion Cambio Incidentes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Correlacion Cambio Incidentes in body.
     */
    @GetMapping("")
    public List<CorrelacionCambioIncidenteDTO> getAllCorrelacionCambioIncidentes(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all CorrelacionCambioIncidentes");
        return correlacionCambioIncidenteService.findAll();
    }

    /**
     * {@code GET  /correlacion-cambio-incidentes/:id} : get the "id" correlacionCambioIncidente.
     *
     * @param id the id of the correlacionCambioIncidenteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the correlacionCambioIncidenteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CorrelacionCambioIncidenteDTO> getCorrelacionCambioIncidente(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CorrelacionCambioIncidente : {}", id);
        Optional<CorrelacionCambioIncidenteDTO> correlacionCambioIncidenteDTO = correlacionCambioIncidenteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(correlacionCambioIncidenteDTO);
    }

    /**
     * {@code DELETE  /correlacion-cambio-incidentes/:id} : delete the "id" correlacionCambioIncidente.
     *
     * @param id the id of the correlacionCambioIncidenteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCorrelacionCambioIncidente(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CorrelacionCambioIncidente : {}", id);
        correlacionCambioIncidenteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
