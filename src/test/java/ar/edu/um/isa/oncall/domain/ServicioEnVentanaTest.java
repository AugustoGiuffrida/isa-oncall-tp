package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.ServicioEnVentanaTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ServicioTestSamples.*;
import static ar.edu.um.isa.oncall.domain.VentanaDeMantenimientoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServicioEnVentanaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServicioEnVentana.class);
        ServicioEnVentana servicioEnVentana1 = getServicioEnVentanaSample1();
        ServicioEnVentana servicioEnVentana2 = new ServicioEnVentana();
        assertThat(servicioEnVentana1).isNotEqualTo(servicioEnVentana2);

        servicioEnVentana2.setId(servicioEnVentana1.getId());
        assertThat(servicioEnVentana1).isEqualTo(servicioEnVentana2);

        servicioEnVentana2 = getServicioEnVentanaSample2();
        assertThat(servicioEnVentana1).isNotEqualTo(servicioEnVentana2);
    }

    @Test
    void ventanaTest() {
        ServicioEnVentana servicioEnVentana = getServicioEnVentanaRandomSampleGenerator();
        VentanaDeMantenimiento ventanaDeMantenimientoBack = getVentanaDeMantenimientoRandomSampleGenerator();

        servicioEnVentana.setVentana(ventanaDeMantenimientoBack);
        assertThat(servicioEnVentana.getVentana()).isEqualTo(ventanaDeMantenimientoBack);

        servicioEnVentana.ventana(null);
        assertThat(servicioEnVentana.getVentana()).isNull();
    }

    @Test
    void servicioTest() {
        ServicioEnVentana servicioEnVentana = getServicioEnVentanaRandomSampleGenerator();
        Servicio servicioBack = getServicioRandomSampleGenerator();

        servicioEnVentana.setServicio(servicioBack);
        assertThat(servicioEnVentana.getServicio()).isEqualTo(servicioBack);

        servicioEnVentana.servicio(null);
        assertThat(servicioEnVentana.getServicio()).isNull();
    }
}
