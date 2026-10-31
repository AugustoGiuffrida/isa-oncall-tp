package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CorrelacionCambioIncidenteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorrelacionCambioIncidenteDTO.class);
        CorrelacionCambioIncidenteDTO correlacionCambioIncidenteDTO1 = new CorrelacionCambioIncidenteDTO();
        correlacionCambioIncidenteDTO1.setId(1L);
        CorrelacionCambioIncidenteDTO correlacionCambioIncidenteDTO2 = new CorrelacionCambioIncidenteDTO();
        assertThat(correlacionCambioIncidenteDTO1).isNotEqualTo(correlacionCambioIncidenteDTO2);
        correlacionCambioIncidenteDTO2.setId(correlacionCambioIncidenteDTO1.getId());
        assertThat(correlacionCambioIncidenteDTO1).isEqualTo(correlacionCambioIncidenteDTO2);
        correlacionCambioIncidenteDTO2.setId(2L);
        assertThat(correlacionCambioIncidenteDTO1).isNotEqualTo(correlacionCambioIncidenteDTO2);
        correlacionCambioIncidenteDTO1.setId(null);
        assertThat(correlacionCambioIncidenteDTO1).isNotEqualTo(correlacionCambioIncidenteDTO2);
    }
}
