package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.DespliegueDeServicio;
import ar.edu.um.isa.oncall.repository.DespliegueDeServicioRepository;
import ar.edu.um.isa.oncall.service.dto.DespliegueDeServicioDTO;
import ar.edu.um.isa.oncall.service.mapper.DespliegueDeServicioMapper;
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
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.DespliegueDeServicio}.
 */
@Service
@Transactional
public class DespliegueDeServicioService {

    private static final Logger LOG = LoggerFactory.getLogger(DespliegueDeServicioService.class);

    private final DespliegueDeServicioRepository despliegueDeServicioRepository;

    private final DespliegueDeServicioMapper despliegueDeServicioMapper;

    public DespliegueDeServicioService(
        DespliegueDeServicioRepository despliegueDeServicioRepository,
        DespliegueDeServicioMapper despliegueDeServicioMapper
    ) {
        this.despliegueDeServicioRepository = despliegueDeServicioRepository;
        this.despliegueDeServicioMapper = despliegueDeServicioMapper;
    }

    /**
     * Save a despliegueDeServicio.
     *
     * @param despliegueDeServicioDTO the entity to save.
     * @return the persisted entity.
     */
    public DespliegueDeServicioDTO save(DespliegueDeServicioDTO despliegueDeServicioDTO) {
        LOG.debug("Request to save DespliegueDeServicio : {}", despliegueDeServicioDTO);
        DespliegueDeServicio despliegueDeServicio = despliegueDeServicioMapper.toEntity(despliegueDeServicioDTO);
        despliegueDeServicio = despliegueDeServicioRepository.save(despliegueDeServicio);
        return despliegueDeServicioMapper.toDto(despliegueDeServicio);
    }

    /**
     * Update a despliegueDeServicio.
     *
     * @param despliegueDeServicioDTO the entity to save.
     * @return the persisted entity.
     */
    public DespliegueDeServicioDTO update(DespliegueDeServicioDTO despliegueDeServicioDTO) {
        LOG.debug("Request to update DespliegueDeServicio : {}", despliegueDeServicioDTO);
        DespliegueDeServicio despliegueDeServicio = despliegueDeServicioMapper.toEntity(despliegueDeServicioDTO);
        despliegueDeServicio = despliegueDeServicioRepository.save(despliegueDeServicio);
        return despliegueDeServicioMapper.toDto(despliegueDeServicio);
    }

    /**
     * Partially update a despliegueDeServicio.
     *
     * @param despliegueDeServicioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DespliegueDeServicioDTO> partialUpdate(DespliegueDeServicioDTO despliegueDeServicioDTO) {
        LOG.debug("Request to partially update DespliegueDeServicio : {}", despliegueDeServicioDTO);

        return despliegueDeServicioRepository
            .findById(despliegueDeServicioDTO.getId())
            .map(existingDespliegueDeServicio -> {
                despliegueDeServicioMapper.partialUpdate(existingDespliegueDeServicio, despliegueDeServicioDTO);

                return existingDespliegueDeServicio;
            })
            .map(despliegueDeServicioRepository::save)
            .map(despliegueDeServicioMapper::toDto);
    }

    /**
     * Get all the despliegueDeServicios.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DespliegueDeServicioDTO> findAll() {
        LOG.debug("Request to get all DespliegueDeServicios");
        return despliegueDeServicioRepository
            .findAll()
            .stream()
            .map(despliegueDeServicioMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the despliegueDeServicios with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DespliegueDeServicioDTO> findAllWithEagerRelationships(Pageable pageable) {
        return despliegueDeServicioRepository.findAllWithEagerRelationships(pageable).map(despliegueDeServicioMapper::toDto);
    }

    /**
     * Get one despliegueDeServicio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DespliegueDeServicioDTO> findOne(Long id) {
        LOG.debug("Request to get DespliegueDeServicio : {}", id);
        return despliegueDeServicioRepository.findOneWithEagerRelationships(id).map(despliegueDeServicioMapper::toDto);
    }

    /**
     * Delete the despliegueDeServicio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DespliegueDeServicio : {}", id);
        despliegueDeServicioRepository.deleteById(id);
    }
}
