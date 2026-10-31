package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.Cambio;
import ar.edu.um.isa.oncall.domain.User;
import ar.edu.um.isa.oncall.service.dto.CambioDTO;
import ar.edu.um.isa.oncall.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cambio} and its DTO {@link CambioDTO}.
 */
@Mapper(componentModel = "spring")
public interface CambioMapper extends EntityMapper<CambioDTO, Cambio> {
    @Mapping(target = "autor", source = "autor", qualifiedByName = "userLogin")
    CambioDTO toDto(Cambio s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
