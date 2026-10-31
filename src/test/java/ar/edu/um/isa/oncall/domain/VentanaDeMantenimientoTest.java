package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.AlertaTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ExcepcionDeVentanaTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ServicioEnVentanaTestSamples.*;
import static ar.edu.um.isa.oncall.domain.VentanaDeMantenimientoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VentanaDeMantenimientoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VentanaDeMantenimiento.class);
        VentanaDeMantenimiento ventanaDeMantenimiento1 = getVentanaDeMantenimientoSample1();
        VentanaDeMantenimiento ventanaDeMantenimiento2 = new VentanaDeMantenimiento();
        assertThat(ventanaDeMantenimiento1).isNotEqualTo(ventanaDeMantenimiento2);

        ventanaDeMantenimiento2.setId(ventanaDeMantenimiento1.getId());
        assertThat(ventanaDeMantenimiento1).isEqualTo(ventanaDeMantenimiento2);

        ventanaDeMantenimiento2 = getVentanaDeMantenimientoSample2();
        assertThat(ventanaDeMantenimiento1).isNotEqualTo(ventanaDeMantenimiento2);
    }

    @Test
    void servicioTest() {
        VentanaDeMantenimiento ventanaDeMantenimiento = getVentanaDeMantenimientoRandomSampleGenerator();
        ServicioEnVentana servicioEnVentanaBack = getServicioEnVentanaRandomSampleGenerator();

        ventanaDeMantenimiento.addServicio(servicioEnVentanaBack);
        assertThat(ventanaDeMantenimiento.getServicios()).containsOnly(servicioEnVentanaBack);
        assertThat(servicioEnVentanaBack.getVentana()).isEqualTo(ventanaDeMantenimiento);

        ventanaDeMantenimiento.removeServicio(servicioEnVentanaBack);
        assertThat(ventanaDeMantenimiento.getServicios()).doesNotContain(servicioEnVentanaBack);
        assertThat(servicioEnVentanaBack.getVentana()).isNull();

        ventanaDeMantenimiento.servicios(new HashSet<>(Set.of(servicioEnVentanaBack)));
        assertThat(ventanaDeMantenimiento.getServicios()).containsOnly(servicioEnVentanaBack);
        assertThat(servicioEnVentanaBack.getVentana()).isEqualTo(ventanaDeMantenimiento);

        ventanaDeMantenimiento.setServicios(new HashSet<>());
        assertThat(ventanaDeMantenimiento.getServicios()).doesNotContain(servicioEnVentanaBack);
        assertThat(servicioEnVentanaBack.getVentana()).isNull();
    }

    @Test
    void excepcionTest() {
        VentanaDeMantenimiento ventanaDeMantenimiento = getVentanaDeMantenimientoRandomSampleGenerator();
        ExcepcionDeVentana excepcionDeVentanaBack = getExcepcionDeVentanaRandomSampleGenerator();

        ventanaDeMantenimiento.addExcepcion(excepcionDeVentanaBack);
        assertThat(ventanaDeMantenimiento.getExcepcions()).containsOnly(excepcionDeVentanaBack);
        assertThat(excepcionDeVentanaBack.getVentana()).isEqualTo(ventanaDeMantenimiento);

        ventanaDeMantenimiento.removeExcepcion(excepcionDeVentanaBack);
        assertThat(ventanaDeMantenimiento.getExcepcions()).doesNotContain(excepcionDeVentanaBack);
        assertThat(excepcionDeVentanaBack.getVentana()).isNull();

        ventanaDeMantenimiento.excepcions(new HashSet<>(Set.of(excepcionDeVentanaBack)));
        assertThat(ventanaDeMantenimiento.getExcepcions()).containsOnly(excepcionDeVentanaBack);
        assertThat(excepcionDeVentanaBack.getVentana()).isEqualTo(ventanaDeMantenimiento);

        ventanaDeMantenimiento.setExcepcions(new HashSet<>());
        assertThat(ventanaDeMantenimiento.getExcepcions()).doesNotContain(excepcionDeVentanaBack);
        assertThat(excepcionDeVentanaBack.getVentana()).isNull();
    }

    @Test
    void alertaSuprimidaTest() {
        VentanaDeMantenimiento ventanaDeMantenimiento = getVentanaDeMantenimientoRandomSampleGenerator();
        Alerta alertaBack = getAlertaRandomSampleGenerator();

        ventanaDeMantenimiento.addAlertaSuprimida(alertaBack);
        assertThat(ventanaDeMantenimiento.getAlertaSuprimidas()).containsOnly(alertaBack);
        assertThat(alertaBack.getSuprimidaPor()).isEqualTo(ventanaDeMantenimiento);

        ventanaDeMantenimiento.removeAlertaSuprimida(alertaBack);
        assertThat(ventanaDeMantenimiento.getAlertaSuprimidas()).doesNotContain(alertaBack);
        assertThat(alertaBack.getSuprimidaPor()).isNull();

        ventanaDeMantenimiento.alertaSuprimidas(new HashSet<>(Set.of(alertaBack)));
        assertThat(ventanaDeMantenimiento.getAlertaSuprimidas()).containsOnly(alertaBack);
        assertThat(alertaBack.getSuprimidaPor()).isEqualTo(ventanaDeMantenimiento);

        ventanaDeMantenimiento.setAlertaSuprimidas(new HashSet<>());
        assertThat(ventanaDeMantenimiento.getAlertaSuprimidas()).doesNotContain(alertaBack);
        assertThat(alertaBack.getSuprimidaPor()).isNull();
    }
}
