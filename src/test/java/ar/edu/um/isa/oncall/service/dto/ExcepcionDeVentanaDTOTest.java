package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExcepcionDeVentanaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExcepcionDeVentanaDTO.class);
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO1 = new ExcepcionDeVentanaDTO();
        excepcionDeVentanaDTO1.setId(1L);
        ExcepcionDeVentanaDTO excepcionDeVentanaDTO2 = new ExcepcionDeVentanaDTO();
        assertThat(excepcionDeVentanaDTO1).isNotEqualTo(excepcionDeVentanaDTO2);
        excepcionDeVentanaDTO2.setId(excepcionDeVentanaDTO1.getId());
        assertThat(excepcionDeVentanaDTO1).isEqualTo(excepcionDeVentanaDTO2);
        excepcionDeVentanaDTO2.setId(2L);
        assertThat(excepcionDeVentanaDTO1).isNotEqualTo(excepcionDeVentanaDTO2);
        excepcionDeVentanaDTO1.setId(null);
        assertThat(excepcionDeVentanaDTO1).isNotEqualTo(excepcionDeVentanaDTO2);
    }
}
