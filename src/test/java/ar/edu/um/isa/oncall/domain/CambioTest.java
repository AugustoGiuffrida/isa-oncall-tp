package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.CambioTestSamples.*;
import static ar.edu.um.isa.oncall.domain.DespliegueDeServicioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CambioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cambio.class);
        Cambio cambio1 = getCambioSample1();
        Cambio cambio2 = new Cambio();
        assertThat(cambio1).isNotEqualTo(cambio2);

        cambio2.setId(cambio1.getId());
        assertThat(cambio1).isEqualTo(cambio2);

        cambio2 = getCambioSample2();
        assertThat(cambio1).isNotEqualTo(cambio2);
    }

    @Test
    void despliegueTest() {
        Cambio cambio = getCambioRandomSampleGenerator();
        DespliegueDeServicio despliegueDeServicioBack = getDespliegueDeServicioRandomSampleGenerator();

        cambio.addDespliegue(despliegueDeServicioBack);
        assertThat(cambio.getDespliegues()).containsOnly(despliegueDeServicioBack);
        assertThat(despliegueDeServicioBack.getCambio()).isEqualTo(cambio);

        cambio.removeDespliegue(despliegueDeServicioBack);
        assertThat(cambio.getDespliegues()).doesNotContain(despliegueDeServicioBack);
        assertThat(despliegueDeServicioBack.getCambio()).isNull();

        cambio.despliegues(new HashSet<>(Set.of(despliegueDeServicioBack)));
        assertThat(cambio.getDespliegues()).containsOnly(despliegueDeServicioBack);
        assertThat(despliegueDeServicioBack.getCambio()).isEqualTo(cambio);

        cambio.setDespliegues(new HashSet<>());
        assertThat(cambio.getDespliegues()).doesNotContain(despliegueDeServicioBack);
        assertThat(despliegueDeServicioBack.getCambio()).isNull();
    }
}
