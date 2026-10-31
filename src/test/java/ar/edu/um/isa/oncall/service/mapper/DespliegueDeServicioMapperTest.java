package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.DespliegueDeServicioAsserts.*;
import static ar.edu.um.isa.oncall.domain.DespliegueDeServicioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DespliegueDeServicioMapperTest {

    private DespliegueDeServicioMapper despliegueDeServicioMapper;

    @BeforeEach
    void setUp() {
        despliegueDeServicioMapper = new DespliegueDeServicioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDespliegueDeServicioSample1();
        var actual = despliegueDeServicioMapper.toEntity(despliegueDeServicioMapper.toDto(expected));
        assertDespliegueDeServicioAllPropertiesEquals(expected, actual);
    }
}
