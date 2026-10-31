package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CambioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CambioDTO.class);
        CambioDTO cambioDTO1 = new CambioDTO();
        cambioDTO1.setId(1L);
        CambioDTO cambioDTO2 = new CambioDTO();
        assertThat(cambioDTO1).isNotEqualTo(cambioDTO2);
        cambioDTO2.setId(cambioDTO1.getId());
        assertThat(cambioDTO1).isEqualTo(cambioDTO2);
        cambioDTO2.setId(2L);
        assertThat(cambioDTO1).isNotEqualTo(cambioDTO2);
        cambioDTO1.setId(null);
        assertThat(cambioDTO1).isNotEqualTo(cambioDTO2);
    }
}
