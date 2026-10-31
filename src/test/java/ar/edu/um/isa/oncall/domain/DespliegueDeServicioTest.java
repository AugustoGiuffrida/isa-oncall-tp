package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.CambioTestSamples.*;
import static ar.edu.um.isa.oncall.domain.CorrelacionCambioIncidenteTestSamples.*;
import static ar.edu.um.isa.oncall.domain.DespliegueDeServicioTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ServicioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DespliegueDeServicioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DespliegueDeServicio.class);
        DespliegueDeServicio despliegueDeServicio1 = getDespliegueDeServicioSample1();
        DespliegueDeServicio despliegueDeServicio2 = new DespliegueDeServicio();
        assertThat(despliegueDeServicio1).isNotEqualTo(despliegueDeServicio2);

        despliegueDeServicio2.setId(despliegueDeServicio1.getId());
        assertThat(despliegueDeServicio1).isEqualTo(despliegueDeServicio2);

        despliegueDeServicio2 = getDespliegueDeServicioSample2();
        assertThat(despliegueDeServicio1).isNotEqualTo(despliegueDeServicio2);
    }

    @Test
    void cambioTest() {
        DespliegueDeServicio despliegueDeServicio = getDespliegueDeServicioRandomSampleGenerator();
        Cambio cambioBack = getCambioRandomSampleGenerator();

        despliegueDeServicio.setCambio(cambioBack);
        assertThat(despliegueDeServicio.getCambio()).isEqualTo(cambioBack);

        despliegueDeServicio.cambio(null);
        assertThat(despliegueDeServicio.getCambio()).isNull();
    }

    @Test
    void servicioTest() {
        DespliegueDeServicio despliegueDeServicio = getDespliegueDeServicioRandomSampleGenerator();
        Servicio servicioBack = getServicioRandomSampleGenerator();

        despliegueDeServicio.setServicio(servicioBack);
        assertThat(despliegueDeServicio.getServicio()).isEqualTo(servicioBack);

        despliegueDeServicio.servicio(null);
        assertThat(despliegueDeServicio.getServicio()).isNull();
    }

    @Test
    void correlacionTest() {
        DespliegueDeServicio despliegueDeServicio = getDespliegueDeServicioRandomSampleGenerator();
        CorrelacionCambioIncidente correlacionCambioIncidenteBack = getCorrelacionCambioIncidenteRandomSampleGenerator();

        despliegueDeServicio.addCorrelacion(correlacionCambioIncidenteBack);
        assertThat(despliegueDeServicio.getCorrelacions()).containsOnly(correlacionCambioIncidenteBack);
        assertThat(correlacionCambioIncidenteBack.getDespliegue()).isEqualTo(despliegueDeServicio);

        despliegueDeServicio.removeCorrelacion(correlacionCambioIncidenteBack);
        assertThat(despliegueDeServicio.getCorrelacions()).doesNotContain(correlacionCambioIncidenteBack);
        assertThat(correlacionCambioIncidenteBack.getDespliegue()).isNull();

        despliegueDeServicio.correlacions(new HashSet<>(Set.of(correlacionCambioIncidenteBack)));
        assertThat(despliegueDeServicio.getCorrelacions()).containsOnly(correlacionCambioIncidenteBack);
        assertThat(correlacionCambioIncidenteBack.getDespliegue()).isEqualTo(despliegueDeServicio);

        despliegueDeServicio.setCorrelacions(new HashSet<>());
        assertThat(despliegueDeServicio.getCorrelacions()).doesNotContain(correlacionCambioIncidenteBack);
        assertThat(correlacionCambioIncidenteBack.getDespliegue()).isNull();
    }
}
