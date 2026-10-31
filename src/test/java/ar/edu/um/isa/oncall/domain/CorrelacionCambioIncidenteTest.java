package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.CorrelacionCambioIncidenteTestSamples.*;
import static ar.edu.um.isa.oncall.domain.DespliegueDeServicioTestSamples.*;
import static ar.edu.um.isa.oncall.domain.IncidenteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CorrelacionCambioIncidenteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorrelacionCambioIncidente.class);
        CorrelacionCambioIncidente correlacionCambioIncidente1 = getCorrelacionCambioIncidenteSample1();
        CorrelacionCambioIncidente correlacionCambioIncidente2 = new CorrelacionCambioIncidente();
        assertThat(correlacionCambioIncidente1).isNotEqualTo(correlacionCambioIncidente2);

        correlacionCambioIncidente2.setId(correlacionCambioIncidente1.getId());
        assertThat(correlacionCambioIncidente1).isEqualTo(correlacionCambioIncidente2);

        correlacionCambioIncidente2 = getCorrelacionCambioIncidenteSample2();
        assertThat(correlacionCambioIncidente1).isNotEqualTo(correlacionCambioIncidente2);
    }

    @Test
    void incidenteTest() {
        CorrelacionCambioIncidente correlacionCambioIncidente = getCorrelacionCambioIncidenteRandomSampleGenerator();
        Incidente incidenteBack = getIncidenteRandomSampleGenerator();

        correlacionCambioIncidente.setIncidente(incidenteBack);
        assertThat(correlacionCambioIncidente.getIncidente()).isEqualTo(incidenteBack);

        correlacionCambioIncidente.incidente(null);
        assertThat(correlacionCambioIncidente.getIncidente()).isNull();
    }

    @Test
    void despliegueTest() {
        CorrelacionCambioIncidente correlacionCambioIncidente = getCorrelacionCambioIncidenteRandomSampleGenerator();
        DespliegueDeServicio despliegueDeServicioBack = getDespliegueDeServicioRandomSampleGenerator();

        correlacionCambioIncidente.setDespliegue(despliegueDeServicioBack);
        assertThat(correlacionCambioIncidente.getDespliegue()).isEqualTo(despliegueDeServicioBack);

        correlacionCambioIncidente.despliegue(null);
        assertThat(correlacionCambioIncidente.getDespliegue()).isNull();
    }
}
