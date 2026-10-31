package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DespliegueDeServicioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DespliegueDeServicioDTO.class);
        DespliegueDeServicioDTO despliegueDeServicioDTO1 = new DespliegueDeServicioDTO();
        despliegueDeServicioDTO1.setId(1L);
        DespliegueDeServicioDTO despliegueDeServicioDTO2 = new DespliegueDeServicioDTO();
        assertThat(despliegueDeServicioDTO1).isNotEqualTo(despliegueDeServicioDTO2);
        despliegueDeServicioDTO2.setId(despliegueDeServicioDTO1.getId());
        assertThat(despliegueDeServicioDTO1).isEqualTo(despliegueDeServicioDTO2);
        despliegueDeServicioDTO2.setId(2L);
        assertThat(despliegueDeServicioDTO1).isNotEqualTo(despliegueDeServicioDTO2);
        despliegueDeServicioDTO1.setId(null);
        assertThat(despliegueDeServicioDTO1).isNotEqualTo(despliegueDeServicioDTO2);
    }
}
