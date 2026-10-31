package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.CorrelacionCambioIncidenteAsserts.*;
import static ar.edu.um.isa.oncall.domain.CorrelacionCambioIncidenteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CorrelacionCambioIncidenteMapperTest {

    private CorrelacionCambioIncidenteMapper correlacionCambioIncidenteMapper;

    @BeforeEach
    void setUp() {
        correlacionCambioIncidenteMapper = new CorrelacionCambioIncidenteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCorrelacionCambioIncidenteSample1();
        var actual = correlacionCambioIncidenteMapper.toEntity(correlacionCambioIncidenteMapper.toDto(expected));
        assertCorrelacionCambioIncidenteAllPropertiesEquals(expected, actual);
    }
}
