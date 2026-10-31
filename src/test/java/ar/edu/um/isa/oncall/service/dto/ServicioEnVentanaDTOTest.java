package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServicioEnVentanaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServicioEnVentanaDTO.class);
        ServicioEnVentanaDTO servicioEnVentanaDTO1 = new ServicioEnVentanaDTO();
        servicioEnVentanaDTO1.setId(1L);
        ServicioEnVentanaDTO servicioEnVentanaDTO2 = new ServicioEnVentanaDTO();
        assertThat(servicioEnVentanaDTO1).isNotEqualTo(servicioEnVentanaDTO2);
        servicioEnVentanaDTO2.setId(servicioEnVentanaDTO1.getId());
        assertThat(servicioEnVentanaDTO1).isEqualTo(servicioEnVentanaDTO2);
        servicioEnVentanaDTO2.setId(2L);
        assertThat(servicioEnVentanaDTO1).isNotEqualTo(servicioEnVentanaDTO2);
        servicioEnVentanaDTO1.setId(null);
        assertThat(servicioEnVentanaDTO1).isNotEqualTo(servicioEnVentanaDTO2);
    }
}
