package ar.edu.um.isa.oncall.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.um.isa.oncall.domain.Cambio;
import ar.edu.um.isa.oncall.domain.CorrelacionCambioIncidente;
import ar.edu.um.isa.oncall.domain.DespliegueDeServicio;
import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.domain.User;
import ar.edu.um.isa.oncall.domain.enumeration.EstadoCorrelacion;
import ar.edu.um.isa.oncall.domain.enumeration.EstadoIncidente;
import ar.edu.um.isa.oncall.domain.enumeration.OrigenCorrelacion;
import ar.edu.um.isa.oncall.domain.enumeration.Severidad;
import ar.edu.um.isa.oncall.repository.CorrelacionCambioIncidenteRepository;
import ar.edu.um.isa.oncall.repository.CorrelacionDeCambiosRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * La correlacion entre cambios e incidentes, sin base de datos.
 */
@ExtendWith(MockitoExtension.class)
class CorrelacionDeCambiosServiceTest {

    private static final Instant DETECTADO = Instant.parse("2026-03-10T03:00:00Z");
    private static final Instant AHORA = DETECTADO.plusSeconds(30);

    @Mock
    private CorrelacionDeCambiosRepository correlacionDeCambiosRepository;

    @Mock
    private CorrelacionCambioIncidenteRepository correlacionCambioIncidenteRepository;

    private CorrelacionDeCambiosService servicio;

    private Incidente incidente;
    private Servicio pagos;

    private final List<CorrelacionCambioIncidente> existentes = new ArrayList<>();

    private long proximoId = 1;

    @BeforeEach
    void init() {
        servicio = new CorrelacionDeCambiosService(correlacionDeCambiosRepository, correlacionCambioIncidenteRepository, 60);
        pagos = new Servicio().nombre("pagos-api");
        pagos.setId(proximoId++);
        incidente = new Incidente().titulo("Errores 500").severidad(Severidad.SEV1).estado(EstadoIncidente.ABIERTO).detectadoEn(DETECTADO);
        incidente.setId(proximoId++);
        incidente.addServicio(pagos);
        lenient().when(correlacionDeCambiosRepository.buscarCorrelaciones(incidente.getId())).thenReturn(existentes);
    }

    @Test
    @DisplayName("Un despliegue dentro de la ventana queda correlacionado como AUTOMATICA y SUGERIDA, y es el sospechoso")
    void unDespliegueEnLaVentanaQuedaCorrelacionado() {
        DespliegueDeServicio despliegue = despliegue("ana", 10);
        hayDesplieguesEnLaVentana(despliegue);
        guardarDevuelveLoGuardado();

        Optional<CorrelacionCambioIncidente> sospechoso = servicio.sospechosoPrincipal(incidente, AHORA);

        assertThat(sospechoso).isPresent();
        CorrelacionCambioIncidente correlacion = sospechoso.orElseThrow();
        assertThat(correlacion.getDespliegue()).isEqualTo(despliegue);
        assertThat(correlacion.getOrigen()).isEqualTo(OrigenCorrelacion.AUTOMATICA);
        assertThat(correlacion.getEstado()).isEqualTo(EstadoCorrelacion.SUGERIDA);
        assertThat(correlacion.getDetectadaEn()).isEqualTo(AHORA);
        assertThat(correlacion.getNota()).contains("10 minutos antes de la deteccion");
    }

    @Test
    @DisplayName("La ventana va desde una hora antes de la deteccion hasta la deteccion, sobre los servicios del incidente")
    void laVentanaEsLaHoraAnteriorALaDeteccion() {
        hayDesplieguesEnLaVentana();

        servicio.sospechosoPrincipal(incidente, AHORA);

        verify(correlacionDeCambiosRepository).buscarDesplieguesEntre(
            Set.of(pagos.getId()),
            DETECTADO.minus(Duration.ofMinutes(60)),
            DETECTADO
        );
    }

    @Test
    @DisplayName("Con varios despliegues, el sospechoso es el mas cercano a la deteccion")
    void conVariosGanaElMasCercanoALaDeteccion() {
        DespliegueDeServicio viejo = despliegue("ana", 50);
        DespliegueDeServicio reciente = despliegue("beto", 5);
        hayDesplieguesEnLaVentana(viejo, reciente);
        guardarDevuelveLoGuardado();

        assertThat(servicio.sospechosoPrincipal(incidente, AHORA).orElseThrow().getDespliegue()).isEqualTo(reciente);
    }

    @Test
    @DisplayName("Un despliegue ya correlacionado no se vuelve a correlacionar")
    void unDespliegueYaCorrelacionadoNoSeDuplica() {
        DespliegueDeServicio despliegue = despliegue("ana", 10);
        existentes.add(correlacion(despliegue, OrigenCorrelacion.AUTOMATICA, EstadoCorrelacion.SUGERIDA));
        hayDesplieguesEnLaVentana(despliegue);

        Optional<CorrelacionCambioIncidente> sospechoso = servicio.sospechosoPrincipal(incidente, AHORA);

        assertThat(sospechoso).isPresent();
        verify(correlacionCambioIncidenteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Una correlacion descartada no cuenta, y tampoco se vuelve a sugerir")
    void unaCorrelacionDescartadaNoCuenta() {
        DespliegueDeServicio despliegue = despliegue("ana", 10);
        existentes.add(correlacion(despliegue, OrigenCorrelacion.AUTOMATICA, EstadoCorrelacion.DESCARTADA));
        hayDesplieguesEnLaVentana(despliegue);

        assertThat(servicio.sospechosoPrincipal(incidente, AHORA)).isEmpty();
        verify(correlacionCambioIncidenteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Una correlacion manual cuenta aunque el despliegue este fuera de la ventana")
    void unaCorrelacionManualCuentaFueraDeLaVentana() {
        DespliegueDeServicio deAyer = despliegue("ana", 24 * 60);
        existentes.add(correlacion(deAyer, OrigenCorrelacion.MANUAL, EstadoCorrelacion.CONFIRMADA));
        hayDesplieguesEnLaVentana();

        assertThat(servicio.sospechosoPrincipal(incidente, AHORA).orElseThrow().getDespliegue()).isEqualTo(deAyer);
    }

    @Test
    @DisplayName("Sin despliegues ni correlaciones no hay sospechoso")
    void sinDesplieguesNoHaySospechoso() {
        hayDesplieguesEnLaVentana();

        assertThat(servicio.sospechosoPrincipal(incidente, AHORA)).isEmpty();
    }

    @Test
    @DisplayName("La ventana tiene que ser de al menos un minuto")
    void laVentanaTieneQueSerPositiva() {
        assertThatThrownBy(() ->
            new CorrelacionDeCambiosService(correlacionDeCambiosRepository, correlacionCambioIncidenteRepository, 0)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    // ---------------------------------------------------------------------
    // Utilidades
    // ---------------------------------------------------------------------

    private void hayDesplieguesEnLaVentana(DespliegueDeServicio... despliegues) {
        when(correlacionDeCambiosRepository.buscarDesplieguesEntre(any(), any(), any())).thenReturn(List.of(despliegues));
    }

    private void guardarDevuelveLoGuardado() {
        when(correlacionCambioIncidenteRepository.save(any())).thenAnswer(i -> {
            CorrelacionCambioIncidente guardada = i.getArgument(0);
            guardada.setId(proximoId++);
            return guardada;
        });
    }

    /** Un despliegue sobre pagos, {@code minutosAntes} de la deteccion, hecho por {@code login}. */
    private DespliegueDeServicio despliegue(String login, long minutosAntes) {
        User autor = new User();
        autor.setId(proximoId++);
        autor.setLogin(login);
        Cambio cambio = new Cambio().titulo("cambio de " + login).autor(autor);
        cambio.setId(proximoId++);
        DespliegueDeServicio despliegue = new DespliegueDeServicio()
            .version("1." + proximoId)
            .desplegadoEn(DETECTADO.minus(Duration.ofMinutes(minutosAntes)))
            .cambio(cambio)
            .servicio(pagos);
        despliegue.setId(proximoId++);
        return despliegue;
    }

    private CorrelacionCambioIncidente correlacion(DespliegueDeServicio despliegue, OrigenCorrelacion origen, EstadoCorrelacion estado) {
        CorrelacionCambioIncidente correlacion = new CorrelacionCambioIncidente()
            .origen(origen)
            .estado(estado)
            .detectadaEn(DETECTADO)
            .incidente(incidente)
            .despliegue(despliegue);
        correlacion.setId(proximoId++);
        return correlacion;
    }
}
