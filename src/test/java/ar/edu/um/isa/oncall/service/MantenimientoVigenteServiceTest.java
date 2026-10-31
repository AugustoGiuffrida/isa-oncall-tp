package ar.edu.um.isa.oncall.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import ar.edu.um.isa.oncall.domain.ExcepcionDeVentana;
import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.domain.enumeration.Recurrencia;
import ar.edu.um.isa.oncall.domain.enumeration.TipoExcepcionDeVentana;
import ar.edu.um.isa.oncall.repository.MantenimientoVigenteRepository;
import ar.edu.um.isa.oncall.repository.ServicioRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * La regla de ventanas de mantenimiento, sin base de datos. Salvo que se diga otra cosa, las
 * ventanas estan en Buenos Aires (UTC-3, sin horario de verano) y la primera ocurrencia es el
 * martes 10 de marzo de 2026 de 02:00 a 04:00 hora local, es decir de 05:00 a 07:00 UTC.
 */
@ExtendWith(MockitoExtension.class)
class MantenimientoVigenteServiceTest {

    private static final long SERVICIO_ID = 7L;
    private static final String BUENOS_AIRES = "America/Argentina/Buenos_Aires";

    @Mock
    private MantenimientoVigenteRepository mantenimientoVigenteRepository;

    @Mock
    private ServicioRepository servicioRepository;

    private long proximoId = 1;

    // ---------------------------------------------------------------------
    // Ventana unica y bordes
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("Una ventana unica cubre su rango, con el inicio incluido y el fin excluido")
    void unaVentanaUnicaCubreSuRango() {
        VentanaDeMantenimiento ventana = martesDeDosACuatro(Recurrencia.UNICA);

        assertThat(cubre(ventana, "2026-03-10T05:00:00Z")).isTrue();
        assertThat(cubre(ventana, "2026-03-10T06:59:59Z")).isTrue();
        assertThat(cubre(ventana, "2026-03-10T07:00:00Z")).isFalse();
        assertThat(cubre(ventana, "2026-03-10T04:59:59Z")).isFalse();
        assertThat(cubre(ventana, "2026-03-17T05:30:00Z")).isFalse();
    }

    @Test
    @DisplayName("Una ventana con hasta anterior o igual a desde no cubre nada")
    void unaVentanaInvalidaNoCubreNada() {
        VentanaDeMantenimiento ventana = martesDeDosACuatro(Recurrencia.UNICA);
        ventana.setHasta(ventana.getDesde());

        assertThat(cubre(ventana, "2026-03-10T05:00:00Z")).isFalse();
    }

    // ---------------------------------------------------------------------
    // Recurrencia
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("Semanal: se repite el mismo dia de la semana a la misma hora local, y no los otros dias")
    void semanalSeRepiteCadaSemana() {
        VentanaDeMantenimiento ventana = martesDeDosACuatro(Recurrencia.SEMANAL);

        assertThat(cubre(ventana, "2026-03-17T05:30:00Z")).isTrue(); // martes siguiente
        assertThat(cubre(ventana, "2026-04-07T06:30:00Z")).isTrue(); // cuatro semanas despues
        assertThat(cubre(ventana, "2026-03-18T05:30:00Z")).isFalse(); // miercoles
        assertThat(cubre(ventana, "2026-03-03T05:30:00Z")).isFalse(); // martes anterior a la primera
    }

    @Test
    @DisplayName("Diaria: se repite todos los dias")
    void diariaSeRepiteTodosLosDias() {
        VentanaDeMantenimiento ventana = martesDeDosACuatro(Recurrencia.DIARIA);

        assertThat(cubre(ventana, "2026-03-11T05:30:00Z")).isTrue();
        assertThat(cubre(ventana, "2026-03-14T06:00:00Z")).isTrue();
    }

    @Test
    @DisplayName("repetirHasta es la ultima fecha en que empieza una ocurrencia")
    void repetirHastaCortaLaRecurrencia() {
        VentanaDeMantenimiento ventana = martesDeDosACuatro(Recurrencia.SEMANAL);
        ventana.setRepetirHasta(LocalDate.parse("2026-03-17"));

        assertThat(cubre(ventana, "2026-03-17T05:30:00Z")).isTrue();
        assertThat(cubre(ventana, "2026-03-24T05:30:00Z")).isFalse();
    }

    @Test
    @DisplayName("Una ventana que cruza la medianoche cubre la madrugada del dia siguiente")
    void unaVentanaQueCruzaLaMedianoche() {
        // diaria de 23:00 a 01:00 hora local
        VentanaDeMantenimiento ventana = ventana(Recurrencia.DIARIA, "2026-03-10T02:00:00Z", "2026-03-10T04:00:00Z");

        assertThat(cubre(ventana, "2026-03-13T03:30:00Z")).isTrue(); // 00:30 local del 13
        assertThat(cubre(ventana, "2026-03-13T04:00:00Z")).isFalse(); // 01:00 local
    }

    // ---------------------------------------------------------------------
    // Horario de verano
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("Horario de verano: una ventana semanal sigue siendo de 1 a 3 en hora local despues del cambio de hora")
    void conHorarioDeVeranoSeRespetaLaHoraLocal() {
        // domingos de 01:00 a 03:00 en Nueva York; el 8 de marzo de 2026 se adelanta la hora
        VentanaDeMantenimiento ventana = ventana(Recurrencia.SEMANAL, "2026-03-01T06:00:00Z", "2026-03-01T08:00:00Z");
        ventana.setZonaHoraria("America/New_York");

        // el dia del salto: de 01:00 EST a 03:00 EDT, una sola hora real
        assertThat(cubre(ventana, "2026-03-08T06:30:00Z")).isTrue();
        assertThat(cubre(ventana, "2026-03-08T07:00:00Z")).isFalse();
        // la semana siguiente: de 01:00 a 03:00 EDT, que en UTC empieza una hora antes que al principio
        assertThat(cubre(ventana, "2026-03-15T05:30:00Z")).isTrue();
        assertThat(cubre(ventana, "2026-03-15T07:30:00Z")).isFalse();
    }

    @Test
    @DisplayName("Horario de verano: una ventana en la hora que se repite (atraso de otoño) no se corre a la otra")
    void unaVentanaEnLaHoraRepetidaNoSeCorre() {
        // el 1 de noviembre de 2026 en Nueva York la 01:15 pasa dos veces: 05:15Z (EDT) y 06:15Z (EST)
        VentanaDeMantenimiento ventana = ventana(Recurrencia.UNICA, "2026-11-01T06:15:00Z", "2026-11-01T06:45:00Z");
        ventana.setZonaHoraria("America/New_York");

        assertThat(cubre(ventana, "2026-11-01T06:30:00Z")).isTrue();
        assertThat(cubre(ventana, "2026-11-01T05:30:00Z")).isFalse();
    }

    // ---------------------------------------------------------------------
    // Excepciones
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("Una excepcion CANCELADA anula solo la ocurrencia de su fecha")
    void unaCanceladaAnulaSoloSuFecha() {
        VentanaDeMantenimiento ventana = martesDeDosACuatro(Recurrencia.SEMANAL);
        excepcion(ventana, "2026-03-17", TipoExcepcionDeVentana.CANCELADA, null, null);

        assertThat(cubre(ventana, "2026-03-17T05:30:00Z")).isFalse();
        assertThat(cubre(ventana, "2026-03-24T05:30:00Z")).isTrue();
    }

    @Test
    @DisplayName("Una excepcion REPROGRAMADA mueve la ocurrencia: el horario original deja de cubrir y el nuevo cubre")
    void unaReprogramadaMueveLaOcurrencia() {
        VentanaDeMantenimiento ventana = martesDeDosACuatro(Recurrencia.SEMANAL);
        excepcion(ventana, "2026-03-17", TipoExcepcionDeVentana.REPROGRAMADA, "2026-03-19T05:00:00Z", "2026-03-19T08:00:00Z");

        assertThat(cubre(ventana, "2026-03-17T05:30:00Z")).isFalse();
        Optional<MantenimientoVigenteService.Ocurrencia> movida = MantenimientoVigenteService.ocurrenciaQueCubre(
            ventana,
            Instant.parse("2026-03-19T07:30:00Z")
        );
        assertThat(movida).isPresent();
        assertThat(movida.orElseThrow().reprogramada()).isTrue();
    }

    @Test
    @DisplayName("Una excepcion sobre una fecha en la que la ventana no ocurre no tiene efecto")
    void unaExcepcionSobreUnaFechaSinOcurrenciaNoTieneEfecto() {
        VentanaDeMantenimiento ventana = martesDeDosACuatro(Recurrencia.SEMANAL);
        // el 18 es miercoles
        excepcion(ventana, "2026-03-18", TipoExcepcionDeVentana.REPROGRAMADA, "2026-03-19T05:00:00Z", "2026-03-19T08:00:00Z");

        assertThat(cubre(ventana, "2026-03-19T05:30:00Z")).isFalse();
        assertThat(cubre(ventana, "2026-03-17T05:30:00Z")).isTrue();
    }

    @Test
    @DisplayName("Si dos ocurrencias de la misma ventana se solapan, informa la que termina mas tarde")
    void dentroDeUnaVentanaGanaLaOcurrenciaQueTerminaMasTarde() {
        // diaria de 30 horas: la del 10 (02:00 del 10 a 08:00 del 11) y la del 11 se pisan
        VentanaDeMantenimiento ventana = ventana(Recurrencia.DIARIA, "2026-03-10T05:00:00Z", "2026-03-11T11:00:00Z");

        Optional<MantenimientoVigenteService.Ocurrencia> ocurrencia = MantenimientoVigenteService.ocurrenciaQueCubre(
            ventana,
            Instant.parse("2026-03-11T06:00:00Z")
        );

        assertThat(ocurrencia).isPresent();
        assertThat(ocurrencia.orElseThrow().hasta()).isEqualTo(Instant.parse("2026-03-12T11:00:00Z"));
    }

    @Test
    @DisplayName("Si una reprogramacion pisa otra ocurrencia de la misma ventana, informa la que termina mas tarde")
    void unaReprogramacionQuePisaOtraOcurrencia() {
        VentanaDeMantenimiento ventana = martesDeDosACuatro(Recurrencia.SEMANAL);
        // la del 17 se mueve al 24 de 05:30 a 06:45 UTC, dentro de la ocurrencia regular del 24 (05:00 a 07:00)
        excepcion(ventana, "2026-03-17", TipoExcepcionDeVentana.REPROGRAMADA, "2026-03-24T05:30:00Z", "2026-03-24T06:45:00Z");

        Optional<MantenimientoVigenteService.Ocurrencia> ocurrencia = MantenimientoVigenteService.ocurrenciaQueCubre(
            ventana,
            Instant.parse("2026-03-24T06:30:00Z")
        );

        assertThat(ocurrencia).isPresent();
        assertThat(ocurrencia.orElseThrow().hasta()).isEqualTo(Instant.parse("2026-03-24T07:00:00Z"));
        assertThat(ocurrencia.orElseThrow().reprogramada()).isFalse();
    }

    // ---------------------------------------------------------------------
    // Varias ventanas
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("Si varias ventanas cubren el instante, manda la que termina mas tarde")
    void conVariasVentanasMandaLaQueTerminaMasTarde() {
        VentanaDeMantenimiento corta = martesDeDosACuatro(Recurrencia.UNICA);
        VentanaDeMantenimiento larga = ventana(Recurrencia.UNICA, "2026-03-10T04:00:00Z", "2026-03-10T10:00:00Z");
        when(mantenimientoVigenteRepository.buscarVentanasActivasDeServicio(SERVICIO_ID)).thenReturn(List.of(corta, larga));
        MantenimientoVigenteService servicio = new MantenimientoVigenteService(mantenimientoVigenteRepository, servicioRepository);

        Optional<MantenimientoVigenteService.VentanaVigente> vigente = servicio.ventanaVigente(
            SERVICIO_ID,
            Instant.parse("2026-03-10T05:30:00Z")
        );

        assertThat(vigente).isPresent();
        assertThat(vigente.orElseThrow().ventana()).isEqualTo(larga);
    }

    @Test
    @DisplayName("Sin ventanas que cubran el instante, el servicio no esta en mantenimiento")
    void sinVentanasNoHayMantenimiento() {
        when(mantenimientoVigenteRepository.buscarVentanasActivasDeServicio(SERVICIO_ID)).thenReturn(
            List.of(martesDeDosACuatro(Recurrencia.UNICA))
        );
        MantenimientoVigenteService servicio = new MantenimientoVigenteService(mantenimientoVigenteRepository, servicioRepository);

        assertThat(servicio.ventanaVigente(SERVICIO_ID, Instant.parse("2026-03-10T08:00:00Z"))).isEmpty();
    }

    // ---------------------------------------------------------------------
    // Utilidades
    // ---------------------------------------------------------------------

    private static boolean cubre(VentanaDeMantenimiento ventana, String instante) {
        return MantenimientoVigenteService.ocurrenciaQueCubre(ventana, Instant.parse(instante)).isPresent();
    }

    private VentanaDeMantenimiento martesDeDosACuatro(Recurrencia recurrencia) {
        return ventana(recurrencia, "2026-03-10T05:00:00Z", "2026-03-10T07:00:00Z");
    }

    private VentanaDeMantenimiento ventana(Recurrencia recurrencia, String desde, String hasta) {
        VentanaDeMantenimiento ventana = new VentanaDeMantenimiento()
            .nombre("ventana " + proximoId)
            .desde(Instant.parse(desde))
            .hasta(Instant.parse(hasta))
            .recurrencia(recurrencia)
            .zonaHoraria(BUENOS_AIRES)
            .activa(true);
        ventana.setId(proximoId++);
        return ventana;
    }

    private void excepcion(
        VentanaDeMantenimiento ventana,
        String fecha,
        TipoExcepcionDeVentana tipo,
        String nuevoDesde,
        String nuevoHasta
    ) {
        ExcepcionDeVentana excepcion = new ExcepcionDeVentana()
            .fecha(LocalDate.parse(fecha))
            .tipo(tipo)
            .nuevoDesde(nuevoDesde == null ? null : Instant.parse(nuevoDesde))
            .nuevoHasta(nuevoHasta == null ? null : Instant.parse(nuevoHasta));
        excepcion.setId(proximoId++);
        ventana.addExcepcion(excepcion);
    }
}
