package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.Cambio;
import ar.edu.um.isa.oncall.repository.CambioRepository;
import ar.edu.um.isa.oncall.service.dto.CambioDTO;
import ar.edu.um.isa.oncall.service.mapper.CambioMapper;
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
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.Cambio}.
 */
@Service
@Transactional
public class CambioService {

    private static final Logger LOG = LoggerFactory.getLogger(CambioService.class);

    private final CambioRepository cambioRepository;

    private final CambioMapper cambioMapper;

    public CambioService(CambioRepository cambioRepository, CambioMapper cambioMapper) {
        this.cambioRepository = cambioRepository;
        this.cambioMapper = cambioMapper;
    }

    /**
     * Save a cambio.
     *
     * @param cambioDTO the entity to save.
     * @return the persisted entity.
     */
    public CambioDTO save(CambioDTO cambioDTO) {
        LOG.debug("Request to save Cambio : {}", cambioDTO);
        Cambio cambio = cambioMapper.toEntity(cambioDTO);
        cambio = cambioRepository.save(cambio);
        return cambioMapper.toDto(cambio);
    }

    /**
     * Update a cambio.
     *
     * @param cambioDTO the entity to save.
     * @return the persisted entity.
     */
    public CambioDTO update(CambioDTO cambioDTO) {
        LOG.debug("Request to update Cambio : {}", cambioDTO);
        Cambio cambio = cambioMapper.toEntity(cambioDTO);
        cambio = cambioRepository.save(cambio);
        return cambioMapper.toDto(cambio);
    }

    /**
     * Partially update a cambio.
     *
     * @param cambioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CambioDTO> partialUpdate(CambioDTO cambioDTO) {
        LOG.debug("Request to partially update Cambio : {}", cambioDTO);

        return cambioRepository
            .findById(cambioDTO.getId())
            .map(existingCambio -> {
                cambioMapper.partialUpdate(existingCambio, cambioDTO);

                return existingCambio;
            })
            .map(cambioRepository::save)
            .map(cambioMapper::toDto);
    }

    /**
     * Get all the cambios.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CambioDTO> findAll() {
        LOG.debug("Request to get all Cambios");
        return cambioRepository.findAll().stream().map(cambioMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the cambios with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CambioDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cambioRepository.findAllWithEagerRelationships(pageable).map(cambioMapper::toDto);
    }

    /**
     * Get one cambio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CambioDTO> findOne(Long id) {
        LOG.debug("Request to get Cambio : {}", id);
        return cambioRepository.findOneWithEagerRelationships(id).map(cambioMapper::toDto);
    }

    /**
     * Delete the cambio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Cambio : {}", id);
        cambioRepository.deleteById(id);
    }
}
