package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.ServicioEnVentanaAsserts.*;
import static ar.edu.um.isa.oncall.domain.ServicioEnVentanaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServicioEnVentanaMapperTest {

    private ServicioEnVentanaMapper servicioEnVentanaMapper;

    @BeforeEach
    void setUp() {
        servicioEnVentanaMapper = new ServicioEnVentanaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getServicioEnVentanaSample1();
        var actual = servicioEnVentanaMapper.toEntity(servicioEnVentanaMapper.toDto(expected));
        assertServicioEnVentanaAllPropertiesEquals(expected, actual);
    }
}
