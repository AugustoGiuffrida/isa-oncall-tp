package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.CorrelacionCambioIncidente;
import ar.edu.um.isa.oncall.domain.DespliegueDeServicio;
import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.service.dto.CorrelacionCambioIncidenteDTO;
import ar.edu.um.isa.oncall.service.dto.DespliegueDeServicioDTO;
import ar.edu.um.isa.oncall.service.dto.IncidenteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CorrelacionCambioIncidente} and its DTO {@link CorrelacionCambioIncidenteDTO}.
 */
@Mapper(componentModel = "spring")
public interface CorrelacionCambioIncidenteMapper extends EntityMapper<CorrelacionCambioIncidenteDTO, CorrelacionCambioIncidente> {
    @Mapping(target = "incidente", source = "incidente", qualifiedByName = "incidenteTitulo")
    @Mapping(target = "despliegue", source = "despliegue", qualifiedByName = "despliegueDeServicioVersion")
    CorrelacionCambioIncidenteDTO toDto(CorrelacionCambioIncidente s);

    @Named("incidenteTitulo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titulo", source = "titulo")
    IncidenteDTO toDtoIncidenteTitulo(Incidente incidente);

    @Named("despliegueDeServicioVersion")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "version", source = "version")
    DespliegueDeServicioDTO toDtoDespliegueDeServicioVersion(DespliegueDeServicio despliegueDeServicio);
}
