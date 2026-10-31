package ar.edu.um.isa.oncall.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.Equipo;
import ar.edu.um.isa.oncall.domain.ExcepcionDeVentana;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.domain.ServicioEnVentana;
import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.domain.enumeration.Criticidad;
import ar.edu.um.isa.oncall.domain.enumeration.Entorno;
import ar.edu.um.isa.oncall.domain.enumeration.Recurrencia;
import ar.edu.um.isa.oncall.domain.enumeration.TipoExcepcionDeVentana;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests de la regla de ventanas de mantenimiento: la consulta contra la base real.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MantenimientoVigenteResourceIT {

    private static final String API_MANTENIMIENTO = "/api/servicios/{id}/mantenimiento";

    private static final AtomicInteger SECUENCIA = new AtomicInteger();

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMockMvc;

    @Test
    @Transactional
    @DisplayName("Una ventana semanal cubre al servicio cada semana, salvo la ocurrencia cancelada")
    void unaVentanaSemanalConUnaCancelada() throws Exception {
        Servicio servicio = servicio();
        // martes de 02:00 a 04:00 en Buenos Aires = 05:00 a 07:00 UTC
        VentanaDeMantenimiento ventana = ventanaSemanalSobre(servicio, "2026-03-10T05:00:00Z", "2026-03-10T07:00:00Z");
        em.persist(new ExcepcionDeVentana().fecha(LocalDate.parse("2026-03-17")).tipo(TipoExcepcionDeVentana.CANCELADA).ventana(ventana));
        em.flush();
        em.clear();

        restMockMvc
            .perform(get(API_MANTENIMIENTO, servicio.getId()).param("en", "2026-03-24T05:30:00Z"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.enMantenimiento").value(true))
            .andExpect(jsonPath("$.ventanaNombre").value(ventana.getNombre()))
            .andExpect(jsonPath("$.desde").value("2026-03-24T05:00:00Z"))
            .andExpect(jsonPath("$.hasta").value("2026-03-24T07:00:00Z"));

        restMockMvc
            .perform(get(API_MANTENIMIENTO, servicio.getId()).param("en", "2026-03-17T05:30:00Z"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.enMantenimiento").value(false));
    }

    @Test
    @Transactional
    @DisplayName("Una ventana inactiva no cuenta")
    void unaVentanaInactivaNoCuenta() throws Exception {
        Servicio servicio = servicio();
        VentanaDeMantenimiento ventana = ventanaSemanalSobre(servicio, "2026-03-10T05:00:00Z", "2026-03-10T07:00:00Z");
        ventana.setActiva(false);
        em.flush();
        em.clear();

        restMockMvc
            .perform(get(API_MANTENIMIENTO, servicio.getId()).param("en", "2026-03-10T05:30:00Z"))
            .andExpect(jsonPath("$.enMantenimiento").value(false));
    }

    @Test
    @Transactional
    @DisplayName("Un servicio que no existe da 404")
    void unServicioQueNoExisteDa404() throws Exception {
        restMockMvc.perform(get(API_MANTENIMIENTO, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    // ---------------------------------------------------------------------
    // Utilidades
    // ---------------------------------------------------------------------

    private Servicio servicio() {
        int n = SECUENCIA.incrementAndGet();
        Equipo equipo = new Equipo().nombre("equipo-mantenimiento-" + n).emailContacto("mantenimiento-" + n + "@um.edu.ar");
        em.persist(equipo);
        Servicio servicio = new Servicio()
            .nombre("servicio-mantenimiento-" + n)
            .criticidad(Criticidad.TIER2)
            .entorno(Entorno.PRODUCCION)
            .activo(true)
            .equipo(equipo);
        em.persist(servicio);
        return servicio;
    }

    private VentanaDeMantenimiento ventanaSemanalSobre(Servicio servicio, String desde, String hasta) {
        VentanaDeMantenimiento ventana = new VentanaDeMantenimiento()
            .nombre("ventana-mantenimiento-" + SECUENCIA.incrementAndGet())
            .desde(Instant.parse(desde))
            .hasta(Instant.parse(hasta))
            .recurrencia(Recurrencia.SEMANAL)
            .zonaHoraria("America/Argentina/Buenos_Aires")
            .activa(true);
        em.persist(ventana);
        em.persist(new ServicioEnVentana().ventana(ventana).servicio(servicio));
        em.flush();
        return ventana;
    }
}
