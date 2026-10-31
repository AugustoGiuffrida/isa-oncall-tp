package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.ServicioEnVentana;
import ar.edu.um.isa.oncall.repository.ServicioEnVentanaRepository;
import ar.edu.um.isa.oncall.service.dto.ServicioEnVentanaDTO;
import ar.edu.um.isa.oncall.service.mapper.ServicioEnVentanaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.ServicioEnVentana}.
 */
@Service
@Transactional
public class ServicioEnVentanaService {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioEnVentanaService.class);

    private final ServicioEnVentanaRepository servicioEnVentanaRepository;

    private final ServicioEnVentanaMapper servicioEnVentanaMapper;

    public ServicioEnVentanaService(
        ServicioEnVentanaRepository servicioEnVentanaRepository,
        ServicioEnVentanaMapper servicioEnVentanaMapper
    ) {
        this.servicioEnVentanaRepository = servicioEnVentanaRepository;
        this.servicioEnVentanaMapper = servicioEnVentanaMapper;
    }

    /**
     * Save a servicioEnVentana.
     *
     * @param servicioEnVentanaDTO the entity to save.
     * @return the persisted entity.
     */
    public ServicioEnVentanaDTO save(ServicioEnVentanaDTO servicioEnVentanaDTO) {
        LOG.debug("Request to save ServicioEnVentana : {}", servicioEnVentanaDTO);
        ServicioEnVentana servicioEnVentana = servicioEnVentanaMapper.toEntity(servicioEnVentanaDTO);
        servicioEnVentana = servicioEnVentanaRepository.save(servicioEnVentana);
        return servicioEnVentanaMapper.toDto(servicioEnVentana);
    }

    /**
     * Update a servicioEnVentana.
     *
     * @param servicioEnVentanaDTO the entity to save.
     * @return the persisted entity.
     */
    public ServicioEnVentanaDTO update(ServicioEnVentanaDTO servicioEnVentanaDTO) {
        LOG.debug("Request to update ServicioEnVentana : {}", servicioEnVentanaDTO);
        ServicioEnVentana servicioEnVentana = servicioEnVentanaMapper.toEntity(servicioEnVentanaDTO);
        servicioEnVentana = servicioEnVentanaRepository.save(servicioEnVentana);
        return servicioEnVentanaMapper.toDto(servicioEnVentana);
    }

    /**
     * Partially update a servicioEnVentana.
     *
     * @param servicioEnVentanaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ServicioEnVentanaDTO> partialUpdate(ServicioEnVentanaDTO servicioEnVentanaDTO) {
        LOG.debug("Request to partially update ServicioEnVentana : {}", servicioEnVentanaDTO);

        return servicioEnVentanaRepository
            .findById(servicioEnVentanaDTO.getId())
            .map(existingServicioEnVentana -> {
                servicioEnVentanaMapper.partialUpdate(existingServicioEnVentana, servicioEnVentanaDTO);

                return existingServicioEnVentana;
            })
            .map(servicioEnVentanaRepository::save)
            .map(servicioEnVentanaMapper::toDto);
    }

    /**
     * Get all the servicioEnVentanas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ServicioEnVentanaDTO> findAll() {
        LOG.debug("Request to get all ServicioEnVentanas");
        return servicioEnVentanaRepository
            .findAll()
            .stream()
            .map(servicioEnVentanaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the servicioEnVentanas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ServicioEnVentanaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return servicioEnVentanaRepository.findAllWithEagerRelationships(pageable).map(servicioEnVentanaMapper::toDto);
    }

    /**
     * Get one servicioEnVentana by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServicioEnVentanaDTO> findOne(Long id) {
        LOG.debug("Request to get ServicioEnVentana : {}", id);
        return servicioEnVentanaRepository.findOneWithEagerRelationships(id).map(servicioEnVentanaMapper::toDto);
    }

    /**
     * Delete the servicioEnVentana by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ServicioEnVentana : {}", id);
        servicioEnVentanaRepository.deleteById(id);
    }
}
