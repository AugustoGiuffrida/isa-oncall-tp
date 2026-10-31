package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.ExcepcionDeVentanaTestSamples.*;
import static ar.edu.um.isa.oncall.domain.VentanaDeMantenimientoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExcepcionDeVentanaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExcepcionDeVentana.class);
        ExcepcionDeVentana excepcionDeVentana1 = getExcepcionDeVentanaSample1();
        ExcepcionDeVentana excepcionDeVentana2 = new ExcepcionDeVentana();
        assertThat(excepcionDeVentana1).isNotEqualTo(excepcionDeVentana2);

        excepcionDeVentana2.setId(excepcionDeVentana1.getId());
        assertThat(excepcionDeVentana1).isEqualTo(excepcionDeVentana2);

        excepcionDeVentana2 = getExcepcionDeVentanaSample2();
        assertThat(excepcionDeVentana1).isNotEqualTo(excepcionDeVentana2);
    }

    @Test
    void ventanaTest() {
        ExcepcionDeVentana excepcionDeVentana = getExcepcionDeVentanaRandomSampleGenerator();
        VentanaDeMantenimiento ventanaDeMantenimientoBack = getVentanaDeMantenimientoRandomSampleGenerator();

        excepcionDeVentana.setVentana(ventanaDeMantenimientoBack);
        assertThat(excepcionDeVentana.getVentana()).isEqualTo(ventanaDeMantenimientoBack);

        excepcionDeVentana.ventana(null);
        assertThat(excepcionDeVentana.getVentana()).isNull();
    }
}
