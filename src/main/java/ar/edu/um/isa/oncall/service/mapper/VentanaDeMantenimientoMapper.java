package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.service.dto.VentanaDeMantenimientoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VentanaDeMantenimiento} and its DTO {@link VentanaDeMantenimientoDTO}.
 */
@Mapper(componentModel = "spring")
public interface VentanaDeMantenimientoMapper extends EntityMapper<VentanaDeMantenimientoDTO, VentanaDeMantenimiento> {}
