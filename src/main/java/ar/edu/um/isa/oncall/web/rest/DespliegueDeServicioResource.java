package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.DespliegueDeServicioRepository;
import ar.edu.um.isa.oncall.service.DespliegueDeServicioService;
import ar.edu.um.isa.oncall.service.dto.DespliegueDeServicioDTO;
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
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.DespliegueDeServicio}.
 */
@RestController
@RequestMapping("/api/despliegue-de-servicios")
public class DespliegueDeServicioResource {

    private static final Logger LOG = LoggerFactory.getLogger(DespliegueDeServicioResource.class);

    private static final String ENTITY_NAME = "despliegueDeServicio";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final DespliegueDeServicioService despliegueDeServicioService;

    private final DespliegueDeServicioRepository despliegueDeServicioRepository;

    public DespliegueDeServicioResource(
        DespliegueDeServicioService despliegueDeServicioService,
        DespliegueDeServicioRepository despliegueDeServicioRepository
    ) {
        this.despliegueDeServicioService = despliegueDeServicioService;
        this.despliegueDeServicioRepository = despliegueDeServicioRepository;
    }

    /**
     * {@code POST  /despliegue-de-servicios} : Create a new despliegueDeServicio.
     *
     * @param despliegueDeServicioDTO the despliegueDeServicioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new despliegueDeServicioDTO, or with status {@code 400 (Bad Request)} if the despliegueDeServicio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DespliegueDeServicioDTO> createDespliegueDeServicio(
        @Valid @RequestBody DespliegueDeServicioDTO despliegueDeServicioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save DespliegueDeServicio : {}", despliegueDeServicioDTO);
        if (despliegueDeServicioDTO.getId() != null) {
            throw new BadRequestAlertException("A new despliegueDeServicio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        despliegueDeServicioDTO = despliegueDeServicioService.save(despliegueDeServicioDTO);
        return ResponseEntity.created(new URI("/api/despliegue-de-servicios/" + despliegueDeServicioDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, despliegueDeServicioDTO.getId().toString()))
            .body(despliegueDeServicioDTO);
    }

    /**
     * {@code PUT  /despliegue-de-servicios/:id} : Updates an existing despliegueDeServicio.
     *
     * @param id the id of the despliegueDeServicioDTO to save.
     * @param despliegueDeServicioDTO the despliegueDeServicioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated despliegueDeServicioDTO,
     * or with status {@code 400 (Bad Request)} if the despliegueDeServicioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the despliegueDeServicioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DespliegueDeServicioDTO> updateDespliegueDeServicio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DespliegueDeServicioDTO despliegueDeServicioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DespliegueDeServicio : {}, {}", id, despliegueDeServicioDTO);
        if (despliegueDeServicioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, despliegueDeServicioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!despliegueDeServicioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        despliegueDeServicioDTO = despliegueDeServicioService.update(despliegueDeServicioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, despliegueDeServicioDTO.getId().toString()))
            .body(despliegueDeServicioDTO);
    }

    /**
     * {@code PATCH  /despliegue-de-servicios/:id} : Partial updates given fields of an existing despliegueDeServicio, field will ignore if it is null
     *
     * @param id the id of the despliegueDeServicioDTO to save.
     * @param despliegueDeServicioDTO the despliegueDeServicioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated despliegueDeServicioDTO,
     * or with status {@code 400 (Bad Request)} if the despliegueDeServicioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the despliegueDeServicioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the despliegueDeServicioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DespliegueDeServicioDTO> partialUpdateDespliegueDeServicio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DespliegueDeServicioDTO despliegueDeServicioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DespliegueDeServicio partially : {}, {}", id, despliegueDeServicioDTO);
        if (despliegueDeServicioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, despliegueDeServicioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!despliegueDeServicioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DespliegueDeServicioDTO> result = despliegueDeServicioService.partialUpdate(despliegueDeServicioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, despliegueDeServicioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /despliegue-de-servicios} : get all the Despliegue De Servicios.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Despliegue De Servicios in body.
     */
    @GetMapping("")
    public List<DespliegueDeServicioDTO> getAllDespliegueDeServicios(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all DespliegueDeServicios");
        return despliegueDeServicioService.findAll();
    }

    /**
     * {@code GET  /despliegue-de-servicios/:id} : get the "id" despliegueDeServicio.
     *
     * @param id the id of the despliegueDeServicioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the despliegueDeServicioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DespliegueDeServicioDTO> getDespliegueDeServicio(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DespliegueDeServicio : {}", id);
        Optional<DespliegueDeServicioDTO> despliegueDeServicioDTO = despliegueDeServicioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(despliegueDeServicioDTO);
    }

    /**
     * {@code DELETE  /despliegue-de-servicios/:id} : delete the "id" despliegueDeServicio.
     *
     * @param id the id of the despliegueDeServicioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDespliegueDeServicio(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DespliegueDeServicio : {}", id);
        despliegueDeServicioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
