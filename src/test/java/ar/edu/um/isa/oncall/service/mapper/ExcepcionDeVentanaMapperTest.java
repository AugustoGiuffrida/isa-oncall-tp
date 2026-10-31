package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.ExcepcionDeVentanaAsserts.*;
import static ar.edu.um.isa.oncall.domain.ExcepcionDeVentanaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExcepcionDeVentanaMapperTest {

    private ExcepcionDeVentanaMapper excepcionDeVentanaMapper;

    @BeforeEach
    void setUp() {
        excepcionDeVentanaMapper = new ExcepcionDeVentanaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExcepcionDeVentanaSample1();
        var actual = excepcionDeVentanaMapper.toEntity(excepcionDeVentanaMapper.toDto(expected));
        assertExcepcionDeVentanaAllPropertiesEquals(expected, actual);
    }
}
