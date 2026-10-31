package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.EventoDeIncidente;
import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.enumeration.EstadoIncidente;
import ar.edu.um.isa.oncall.domain.enumeration.TipoEvento;
import ar.edu.um.isa.oncall.repository.EscalamientoRepository;
import ar.edu.um.isa.oncall.repository.EventoDeIncidenteRepository;
import ar.edu.um.isa.oncall.repository.UserRepository;
import ar.edu.um.isa.oncall.service.dto.EstadoDeIncidenteDTO;
import java.time.Duration;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Las dos acciones de una persona que cortan o pausan el escalamiento: reconocer y silenciar.
 *
 * <p>El ABM generado permite escribir {@code reconocidoEn} o {@code silenciadoHasta} a mano;
 * esto es el camino normal, el que deja rastro en la linea de tiempo. Cada accion es
 * idempotente: repetirla no cambia nada.</p>
 *
 * <p>La fila del incidente se bloquea igual que en {@link EscalamientoService}: si alguien
 * reconoce justo mientras corre el escalamiento, una de las dos operaciones espera a la otra.
 * O se notifico antes del reconocimiento, o no se notifica.</p>
 */
@Service
@Transactional
public class CicloDeVidaDeIncidenteService {

    private static final Logger LOG = LoggerFactory.getLogger(CicloDeVidaDeIncidenteService.class);

    /** Silencio maximo: un dia. Un silencio mas largo es un incidente abandonado. */
    public static final int MAX_MINUTOS_DE_SILENCIO = 1440;

    private static final Set<EstadoIncidente> ESTADOS_TERMINADOS = EnumSet.of(EstadoIncidente.RESUELTO, EstadoIncidente.CERRADO);

    private static final int MAX_DETALLE = 500;

    private final EscalamientoRepository escalamientoRepository;
    private final EventoDeIncidenteRepository eventoDeIncidenteRepository;
    private final UserRepository userRepository;

    public CicloDeVidaDeIncidenteService(
        EscalamientoRepository escalamientoRepository,
        EventoDeIncidenteRepository eventoDeIncidenteRepository,
        UserRepository userRepository
    ) {
        this.escalamientoRepository = escalamientoRepository;
        this.eventoDeIncidenteRepository = eventoDeIncidenteRepository;
        this.userRepository = userRepository;
    }

    /**
     * Reconocer: "lo tengo". Corta el escalamiento. Si el incidente no tenia comandante, quien
     * reconoce pasa a serlo.
     *
     * @param incidenteId el incidente.
     * @param login quien reconoce.
     * @param ahora el instante del reconocimiento.
     * @return como quedo, o vacio si el incidente no existe.
     * @throws TransicionInvalidaException si el incidente ya termino.
     */
    public Optional<EstadoDeIncidenteDTO> reconocer(Long incidenteId, String login, Instant ahora) {
        return escalamientoRepository.bloquear(incidenteId).map(incidente -> {
            exigirAbierto(incidente, "reconocer");
            boolean cambio = false;
            if (incidente.getReconocidoEn() == null) {
                incidente.setReconocidoEn(ahora);
                cambio = true;
            }
            if (incidente.getEstado() == EstadoIncidente.ABIERTO) {
                incidente.setEstado(EstadoIncidente.RECONOCIDO);
                cambio = true;
            }
            if (incidente.getComandante() == null && login != null) {
                userRepository.findOneByLogin(login).ifPresent(incidente::setComandante);
            }
            String mensaje = cambio ? "Reconocido por " + login + ". El escalamiento se corta." : "Ya estaba reconocido: no cambia nada.";
            if (cambio) {
                registrar(incidente, TipoEvento.CAMBIO_DE_ESTADO, mensaje, ahora);
            }
            return aEstado(incidente, cambio, mensaje);
        });
    }

    /**
     * Silenciar: pone en pausa el escalamiento. Al vencer, la cadena sigue donde estaba.
     *
     * @param incidenteId el incidente.
     * @param minutos cuanto dura el silencio; {@code 0} levanta un silencio vigente.
     * @param login quien silencia.
     * @param ahora el instante del pedido.
     * @return como quedo, o vacio si el incidente no existe.
     * @throws TransicionInvalidaException si el incidente ya termino.
     * @throws IllegalArgumentException si los minutos estan fuera de rango.
     */
    public Optional<EstadoDeIncidenteDTO> silenciar(Long incidenteId, int minutos, String login, Instant ahora) {
        if (minutos < 0 || minutos > MAX_MINUTOS_DE_SILENCIO) {
            throw new IllegalArgumentException("Los minutos de silencio van de 0 a " + MAX_MINUTOS_DE_SILENCIO + ", y son " + minutos);
        }
        return escalamientoRepository.bloquear(incidenteId).map(incidente -> {
            exigirAbierto(incidente, "silenciar");
            String mensaje;
            if (minutos == 0) {
                incidente.setSilenciadoHasta(null);
                mensaje = "Silencio levantado por " + login + ". El escalamiento sigue en la proxima corrida.";
            } else {
                Instant hasta = ahora.plus(Duration.ofMinutes(minutos));
                incidente.setSilenciadoHasta(hasta);
                mensaje = "Silenciado por " + login + " durante " + minutos + " minutos, hasta " + hasta + ".";
            }
            registrar(incidente, TipoEvento.NOTA, mensaje, ahora);
            return aEstado(incidente, true, mensaje);
        });
    }

    private void exigirAbierto(Incidente incidente, String accion) {
        if (ESTADOS_TERMINADOS.contains(incidente.getEstado())) {
            throw new TransicionInvalidaException(incidente.getId(), incidente.getEstado(), accion);
        }
    }

    private void registrar(Incidente incidente, TipoEvento tipo, String detalle, Instant ocurridoEn) {
        LOG.debug("Incidente {}: {}", incidente.getId(), detalle);
        eventoDeIncidenteRepository.save(
            new EventoDeIncidente()
                .tipo(tipo)
                .detalle(detalle.length() <= MAX_DETALLE ? detalle : detalle.substring(0, MAX_DETALLE - 3) + "...")
                .ocurridoEn(ocurridoEn)
                .automatico(false)
                .incidente(incidente)
        );
    }

    private static EstadoDeIncidenteDTO aEstado(Incidente incidente, boolean cambio, String mensaje) {
        return new EstadoDeIncidenteDTO(
            incidente.getId(),
            incidente.getEstado(),
            incidente.getReconocidoEn(),
            incidente.getSilenciadoHasta(),
            incidente.getComandante() != null ? incidente.getComandante().getLogin() : null,
            cambio,
            mensaje
        );
    }
}
