package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.ExcepcionDeVentana;
import ar.edu.um.isa.oncall.repository.ExcepcionDeVentanaRepository;
import ar.edu.um.isa.oncall.service.dto.ExcepcionDeVentanaDTO;
import ar.edu.um.isa.oncall.service.mapper.ExcepcionDeVentanaMapper;
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
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.ExcepcionDeVentana}.
 */
@Service
@Transactional
public class ExcepcionDeVentanaService {

    private static final Logger LOG = LoggerFactory.getLogger(ExcepcionDeVentanaService.class);

    private final ExcepcionDeVentanaRepository excepcionDeVentanaRepository;

    private final ExcepcionDeVentanaMapper excepcionDeVentanaMapper;

    public ExcepcionDeVentanaService(
        ExcepcionDeVentanaRepository excepcionDeVentanaRepository,
        ExcepcionDeVentanaMapper excepcionDeVentanaMapper
    ) {
        this.excepcionDeVentanaRepository = excepcionDeVentanaRepository;
        this.excepcionDeVentanaMapper = excepcionDeVentanaMapper;
    }

    /**
     * Save a excepcionDeVentana.
     *
     * @param excepcionDeVentanaDTO the entity to save.
     * @return the persisted entity.
     */
    public ExcepcionDeVentanaDTO save(ExcepcionDeVentanaDTO excepcionDeVentanaDTO) {
        LOG.debug("Request to save ExcepcionDeVentana : {}", excepcionDeVentanaDTO);
        ExcepcionDeVentana excepcionDeVentana = excepcionDeVentanaMapper.toEntity(excepcionDeVentanaDTO);
        excepcionDeVentana = excepcionDeVentanaRepository.save(excepcionDeVentana);
        return excepcionDeVentanaMapper.toDto(excepcionDeVentana);
    }

    /**
     * Update a excepcionDeVentana.
     *
     * @param excepcionDeVentanaDTO the entity to save.
     * @return the persisted entity.
     */
    public ExcepcionDeVentanaDTO update(ExcepcionDeVentanaDTO excepcionDeVentanaDTO) {
        LOG.debug("Request to update ExcepcionDeVentana : {}", excepcionDeVentanaDTO);
        ExcepcionDeVentana excepcionDeVentana = excepcionDeVentanaMapper.toEntity(excepcionDeVentanaDTO);
        excepcionDeVentana = excepcionDeVentanaRepository.save(excepcionDeVentana);
        return excepcionDeVentanaMapper.toDto(excepcionDeVentana);
    }

    /**
     * Partially update a excepcionDeVentana.
     *
     * @param excepcionDeVentanaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExcepcionDeVentanaDTO> partialUpdate(ExcepcionDeVentanaDTO excepcionDeVentanaDTO) {
        LOG.debug("Request to partially update ExcepcionDeVentana : {}", excepcionDeVentanaDTO);

        return excepcionDeVentanaRepository
            .findById(excepcionDeVentanaDTO.getId())
            .map(existingExcepcionDeVentana -> {
                excepcionDeVentanaMapper.partialUpdate(existingExcepcionDeVentana, excepcionDeVentanaDTO);

                return existingExcepcionDeVentana;
            })
            .map(excepcionDeVentanaRepository::save)
            .map(excepcionDeVentanaMapper::toDto);
    }

    /**
     * Get all the excepcionDeVentanas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ExcepcionDeVentanaDTO> findAll() {
        LOG.debug("Request to get all ExcepcionDeVentanas");
        return excepcionDeVentanaRepository
            .findAll()
            .stream()
            .map(excepcionDeVentanaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the excepcionDeVentanas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ExcepcionDeVentanaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return excepcionDeVentanaRepository.findAllWithEagerRelationships(pageable).map(excepcionDeVentanaMapper::toDto);
    }

    /**
     * Get one excepcionDeVentana by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExcepcionDeVentanaDTO> findOne(Long id) {
        LOG.debug("Request to get ExcepcionDeVentana : {}", id);
        return excepcionDeVentanaRepository.findOneWithEagerRelationships(id).map(excepcionDeVentanaMapper::toDto);
    }

    /**
     * Delete the excepcionDeVentana by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExcepcionDeVentana : {}", id);
        excepcionDeVentanaRepository.deleteById(id);
    }
}
