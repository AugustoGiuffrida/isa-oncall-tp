package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.VentanaDeMantenimientoAsserts.*;
import static ar.edu.um.isa.oncall.domain.VentanaDeMantenimientoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VentanaDeMantenimientoMapperTest {

    private VentanaDeMantenimientoMapper ventanaDeMantenimientoMapper;

    @BeforeEach
    void setUp() {
        ventanaDeMantenimientoMapper = new VentanaDeMantenimientoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVentanaDeMantenimientoSample1();
        var actual = ventanaDeMantenimientoMapper.toEntity(ventanaDeMantenimientoMapper.toDto(expected));
        assertVentanaDeMantenimientoAllPropertiesEquals(expected, actual);
    }
}
