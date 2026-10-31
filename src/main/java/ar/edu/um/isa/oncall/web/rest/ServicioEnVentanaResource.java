package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.ServicioEnVentanaRepository;
import ar.edu.um.isa.oncall.service.ServicioEnVentanaService;
import ar.edu.um.isa.oncall.service.dto.ServicioEnVentanaDTO;
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
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.ServicioEnVentana}.
 */
@RestController
@RequestMapping("/api/servicio-en-ventanas")
public class ServicioEnVentanaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioEnVentanaResource.class);

    private static final String ENTITY_NAME = "servicioEnVentana";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final ServicioEnVentanaService servicioEnVentanaService;

    private final ServicioEnVentanaRepository servicioEnVentanaRepository;

    public ServicioEnVentanaResource(
        ServicioEnVentanaService servicioEnVentanaService,
        ServicioEnVentanaRepository servicioEnVentanaRepository
    ) {
        this.servicioEnVentanaService = servicioEnVentanaService;
        this.servicioEnVentanaRepository = servicioEnVentanaRepository;
    }

    /**
     * {@code POST  /servicio-en-ventanas} : Create a new servicioEnVentana.
     *
     * @param servicioEnVentanaDTO the servicioEnVentanaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new servicioEnVentanaDTO, or with status {@code 400 (Bad Request)} if the servicioEnVentana has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServicioEnVentanaDTO> createServicioEnVentana(@Valid @RequestBody ServicioEnVentanaDTO servicioEnVentanaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ServicioEnVentana : {}", servicioEnVentanaDTO);
        if (servicioEnVentanaDTO.getId() != null) {
            throw new BadRequestAlertException("A new servicioEnVentana cannot already have an ID", ENTITY_NAME, "idexists");
        }
        servicioEnVentanaDTO = servicioEnVentanaService.save(servicioEnVentanaDTO);
        return ResponseEntity.created(new URI("/api/servicio-en-ventanas/" + servicioEnVentanaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, servicioEnVentanaDTO.getId().toString()))
            .body(servicioEnVentanaDTO);
    }

    /**
     * {@code PUT  /servicio-en-ventanas/:id} : Updates an existing servicioEnVentana.
     *
     * @param id the id of the servicioEnVentanaDTO to save.
     * @param servicioEnVentanaDTO the servicioEnVentanaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicioEnVentanaDTO,
     * or with status {@code 400 (Bad Request)} if the servicioEnVentanaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the servicioEnVentanaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServicioEnVentanaDTO> updateServicioEnVentana(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServicioEnVentanaDTO servicioEnVentanaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServicioEnVentana : {}, {}", id, servicioEnVentanaDTO);
        if (servicioEnVentanaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicioEnVentanaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicioEnVentanaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        servicioEnVentanaDTO = servicioEnVentanaService.update(servicioEnVentanaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, servicioEnVentanaDTO.getId().toString()))
            .body(servicioEnVentanaDTO);
    }

    /**
     * {@code PATCH  /servicio-en-ventanas/:id} : Partial updates given fields of an existing servicioEnVentana, field will ignore if it is null
     *
     * @param id the id of the servicioEnVentanaDTO to save.
     * @param servicioEnVentanaDTO the servicioEnVentanaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicioEnVentanaDTO,
     * or with status {@code 400 (Bad Request)} if the servicioEnVentanaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the servicioEnVentanaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the servicioEnVentanaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServicioEnVentanaDTO> partialUpdateServicioEnVentana(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServicioEnVentanaDTO servicioEnVentanaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ServicioEnVentana partially : {}, {}", id, servicioEnVentanaDTO);
        if (servicioEnVentanaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicioEnVentanaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicioEnVentanaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServicioEnVentanaDTO> result = servicioEnVentanaService.partialUpdate(servicioEnVentanaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, servicioEnVentanaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /servicio-en-ventanas} : get all the Servicio En Ventanas.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Servicio En Ventanas in body.
     */
    @GetMapping("")
    public List<ServicioEnVentanaDTO> getAllServicioEnVentanas(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all ServicioEnVentanas");
        return servicioEnVentanaService.findAll();
    }

    /**
     * {@code GET  /servicio-en-ventanas/:id} : get the "id" servicioEnVentana.
     *
     * @param id the id of the servicioEnVentanaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the servicioEnVentanaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServicioEnVentanaDTO> getServicioEnVentana(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ServicioEnVentana : {}", id);
        Optional<ServicioEnVentanaDTO> servicioEnVentanaDTO = servicioEnVentanaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(servicioEnVentanaDTO);
    }

    /**
     * {@code DELETE  /servicio-en-ventanas/:id} : delete the "id" servicioEnVentana.
     *
     * @param id the id of the servicioEnVentanaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServicioEnVentana(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ServicioEnVentana : {}", id);
        servicioEnVentanaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
