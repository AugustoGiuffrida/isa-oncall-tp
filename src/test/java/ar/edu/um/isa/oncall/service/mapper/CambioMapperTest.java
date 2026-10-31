package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.CambioAsserts.*;
import static ar.edu.um.isa.oncall.domain.CambioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CambioMapperTest {

    private CambioMapper cambioMapper;

    @BeforeEach
    void setUp() {
        cambioMapper = new CambioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCambioSample1();
        var actual = cambioMapper.toEntity(cambioMapper.toDto(expected));
        assertCambioAllPropertiesEquals(expected, actual);
    }
}
