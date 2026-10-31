package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.CorrelacionCambioIncidente;
import ar.edu.um.isa.oncall.repository.CorrelacionCambioIncidenteRepository;
import ar.edu.um.isa.oncall.service.dto.CorrelacionCambioIncidenteDTO;
import ar.edu.um.isa.oncall.service.mapper.CorrelacionCambioIncidenteMapper;
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
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.CorrelacionCambioIncidente}.
 */
@Service
@Transactional
public class CorrelacionCambioIncidenteService {

    private static final Logger LOG = LoggerFactory.getLogger(CorrelacionCambioIncidenteService.class);

    private final CorrelacionCambioIncidenteRepository correlacionCambioIncidenteRepository;

    private final CorrelacionCambioIncidenteMapper correlacionCambioIncidenteMapper;

    public CorrelacionCambioIncidenteService(
        CorrelacionCambioIncidenteRepository correlacionCambioIncidenteRepository,
        CorrelacionCambioIncidenteMapper correlacionCambioIncidenteMapper
    ) {
        this.correlacionCambioIncidenteRepository = correlacionCambioIncidenteRepository;
        this.correlacionCambioIncidenteMapper = correlacionCambioIncidenteMapper;
    }

    /**
     * Save a correlacionCambioIncidente.
     *
     * @param correlacionCambioIncidenteDTO the entity to save.
     * @return the persisted entity.
     */
    public CorrelacionCambioIncidenteDTO save(CorrelacionCambioIncidenteDTO correlacionCambioIncidenteDTO) {
        LOG.debug("Request to save CorrelacionCambioIncidente : {}", correlacionCambioIncidenteDTO);
        CorrelacionCambioIncidente correlacionCambioIncidente = correlacionCambioIncidenteMapper.toEntity(correlacionCambioIncidenteDTO);
        correlacionCambioIncidente = correlacionCambioIncidenteRepository.save(correlacionCambioIncidente);
        return correlacionCambioIncidenteMapper.toDto(correlacionCambioIncidente);
    }

    /**
     * Update a correlacionCambioIncidente.
     *
     * @param correlacionCambioIncidenteDTO the entity to save.
     * @return the persisted entity.
     */
    public CorrelacionCambioIncidenteDTO update(CorrelacionCambioIncidenteDTO correlacionCambioIncidenteDTO) {
        LOG.debug("Request to update CorrelacionCambioIncidente : {}", correlacionCambioIncidenteDTO);
        CorrelacionCambioIncidente correlacionCambioIncidente = correlacionCambioIncidenteMapper.toEntity(correlacionCambioIncidenteDTO);
        correlacionCambioIncidente = correlacionCambioIncidenteRepository.save(correlacionCambioIncidente);
        return correlacionCambioIncidenteMapper.toDto(correlacionCambioIncidente);
    }

    /**
     * Partially update a correlacionCambioIncidente.
     *
     * @param correlacionCambioIncidenteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CorrelacionCambioIncidenteDTO> partialUpdate(CorrelacionCambioIncidenteDTO correlacionCambioIncidenteDTO) {
        LOG.debug("Request to partially update CorrelacionCambioIncidente : {}", correlacionCambioIncidenteDTO);

        return correlacionCambioIncidenteRepository
            .findById(correlacionCambioIncidenteDTO.getId())
            .map(existingCorrelacionCambioIncidente -> {
                correlacionCambioIncidenteMapper.partialUpdate(existingCorrelacionCambioIncidente, correlacionCambioIncidenteDTO);

                return existingCorrelacionCambioIncidente;
            })
            .map(correlacionCambioIncidenteRepository::save)
            .map(correlacionCambioIncidenteMapper::toDto);
    }

    /**
     * Get all the correlacionCambioIncidentes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CorrelacionCambioIncidenteDTO> findAll() {
        LOG.debug("Request to get all CorrelacionCambioIncidentes");
        return correlacionCambioIncidenteRepository
            .findAll()
            .stream()
            .map(correlacionCambioIncidenteMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the correlacionCambioIncidentes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CorrelacionCambioIncidenteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return correlacionCambioIncidenteRepository.findAllWithEagerRelationships(pageable).map(correlacionCambioIncidenteMapper::toDto);
    }

    /**
     * Get one correlacionCambioIncidente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CorrelacionCambioIncidenteDTO> findOne(Long id) {
        LOG.debug("Request to get CorrelacionCambioIncidente : {}", id);
        return correlacionCambioIncidenteRepository.findOneWithEagerRelationships(id).map(correlacionCambioIncidenteMapper::toDto);
    }

    /**
     * Delete the correlacionCambioIncidente by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CorrelacionCambioIncidente : {}", id);
        correlacionCambioIncidenteRepository.deleteById(id);
    }
}
