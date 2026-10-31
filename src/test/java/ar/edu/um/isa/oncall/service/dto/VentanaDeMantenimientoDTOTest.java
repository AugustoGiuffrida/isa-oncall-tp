package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VentanaDeMantenimientoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VentanaDeMantenimientoDTO.class);
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO1 = new VentanaDeMantenimientoDTO();
        ventanaDeMantenimientoDTO1.setId(1L);
        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO2 = new VentanaDeMantenimientoDTO();
        assertThat(ventanaDeMantenimientoDTO1).isNotEqualTo(ventanaDeMantenimientoDTO2);
        ventanaDeMantenimientoDTO2.setId(ventanaDeMantenimientoDTO1.getId());
        assertThat(ventanaDeMantenimientoDTO1).isEqualTo(ventanaDeMantenimientoDTO2);
        ventanaDeMantenimientoDTO2.setId(2L);
        assertThat(ventanaDeMantenimientoDTO1).isNotEqualTo(ventanaDeMantenimientoDTO2);
        ventanaDeMantenimientoDTO1.setId(null);
        assertThat(ventanaDeMantenimientoDTO1).isNotEqualTo(ventanaDeMantenimientoDTO2);
    }
}
