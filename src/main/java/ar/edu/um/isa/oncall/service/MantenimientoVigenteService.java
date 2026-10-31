package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.ExcepcionDeVentana;
import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.domain.enumeration.TipoExcepcionDeVentana;
import ar.edu.um.isa.oncall.repository.MantenimientoVigenteRepository;
import ar.edu.um.isa.oncall.repository.ServicioRepository;
import ar.edu.um.isa.oncall.service.dto.MantenimientoVigenteDTO;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Extension 5 · Ventanas de mantenimiento: ¿esta el servicio en mantenimiento en el instante t?
 *
 * <p>Es la regla C aplicada a las ventanas: una ventana recurrente ("todos los martes de 2 a
 * 4") es el mismo problema temporal que una rotacion. Igual que los turnos, cada ocurrencia es
 * un intervalo semiabierto {@code [desde, hasta)}.</p>
 *
 * <p>La ventana se define por su primera ocurrencia ({@code desde}/{@code hasta}) y una
 * recurrencia. Las siguientes ocurrencias repiten la primera <em>en hora local</em> de la zona
 * de la ventana: "de 2 a 4" sigue siendo de 2 a 4 despues del cambio de hora, aunque eso sean
 * otros instantes UTC. Si la hora local no existe ese dia (salto de primavera), se corre hacia
 * adelante lo que dura el salto, como {@link ZonedDateTime#ofLocal}. Si existe dos veces
 * (atraso de otoño), se toma la que tiene el mismo offset que la primera ocurrencia: asi una
 * ventana cargada en la segunda 01:15 sigue siendo la segunda 01:15.</p>
 *
 * <p>Una excepcion afecta a la ocurrencia que empezaba en su {@code fecha} (local):
 * {@code CANCELADA} la anula y {@code REPROGRAMADA} la reemplaza por {@code nuevoDesde} /
 * {@code nuevoHasta}. Una excepcion sobre una fecha en la que la ventana no ocurre no tiene
 * efecto.</p>
 */
@Service
@Transactional(readOnly = true)
public class MantenimientoVigenteService {

    private final MantenimientoVigenteRepository mantenimientoVigenteRepository;
    private final ServicioRepository servicioRepository;

    public MantenimientoVigenteService(
        MantenimientoVigenteRepository mantenimientoVigenteRepository,
        ServicioRepository servicioRepository
    ) {
        this.mantenimientoVigenteRepository = mantenimientoVigenteRepository;
        this.servicioRepository = servicioRepository;
    }

    /**
     * Una ocurrencia concreta de una ventana.
     *
     * @param desde inicio, inclusive.
     * @param hasta fin, exclusive.
     * @param reprogramada {@code true} si es una ocurrencia movida por una excepcion.
     */
    public record Ocurrencia(Instant desde, Instant hasta, boolean reprogramada) {}

    /**
     * La ventana que tiene al servicio en mantenimiento, y la ocurrencia que lo cubre.
     */
    public record VentanaVigente(VentanaDeMantenimiento ventana, Ocurrencia ocurrencia) {}

    /**
     * La ventana activa del servicio que cubre el instante. Si hay varias, la que termina mas
     * tarde: es la que define hasta cuando el servicio esta en mantenimiento.
     *
     * @param servicioId el servicio.
     * @param instante el instante a resolver.
     * @return la ventana vigente, o vacio si el servicio no esta en mantenimiento.
     */
    public Optional<VentanaVigente> ventanaVigente(Long servicioId, Instant instante) {
        return mantenimientoVigenteRepository
            .buscarVentanasActivasDeServicio(servicioId)
            .stream()
            .flatMap(ventana ->
                ocurrenciaQueCubre(ventana, instante)
                    .map(ocurrencia -> new VentanaVigente(ventana, ocurrencia))
                    .stream()
            )
            .max(
                Comparator.comparing((VentanaVigente vigente) -> vigente.ocurrencia().hasta()).thenComparing(
                    vigente -> vigente.ventana().getId(),
                    Comparator.nullsFirst(Comparator.naturalOrder())
                )
            );
    }

    /**
     * La respuesta del endpoint.
     *
     * @param servicioId el servicio.
     * @param instante el instante a resolver.
     * @return si el servicio esta en mantenimiento, o vacio si el servicio no existe.
     */
    public Optional<MantenimientoVigenteDTO> consultar(Long servicioId, Instant instante) {
        return servicioRepository.findById(servicioId).map(servicio -> {
            Optional<VentanaVigente> vigente = ventanaVigente(servicioId, instante);
            return new MantenimientoVigenteDTO(
                servicio.getId(),
                servicio.getNombre(),
                instante,
                vigente.isPresent(),
                vigente.map(v -> v.ventana().getId()).orElse(null),
                vigente.map(v -> v.ventana().getNombre()).orElse(null),
                vigente.map(v -> v.ocurrencia().desde()).orElse(null),
                vigente.map(v -> v.ocurrencia().hasta()).orElse(null),
                vigente.map(v -> v.ocurrencia().reprogramada()).orElse(false)
            );
        });
    }

    /**
     * La regla propiamente dicha, sin base de datos: la ocurrencia de la ventana que cubre el
     * instante, si hay una.
     */
    static Optional<Ocurrencia> ocurrenciaQueCubre(VentanaDeMantenimiento ventana, Instant instante) {
        if (ventana.getDesde() == null || ventana.getHasta() == null || !ventana.getHasta().isAfter(ventana.getDesde())) {
            return Optional.empty();
        }
        ZoneId zona = zonaDe(ventana);
        LocalDateTime inicioLocal = ventana.getDesde().atZone(zona).toLocalDateTime();
        LocalDateTime finLocal = ventana.getHasta().atZone(zona).toLocalDateTime();
        LocalDate primeraFecha = inicioLocal.toLocalDate();
        // el offset original desempata las horas que existen dos veces en el cambio de horario
        ZoneOffset offsetInicio = ventana.getDesde().atZone(zona).getOffset();
        ZoneOffset offsetFin = ventana.getHasta().atZone(zona).getOffset();

        // una ocurrencia movida puede caer en cualquier lado: se mira primero
        for (ExcepcionDeVentana excepcion : ventana.getExcepcions()) {
            if (
                excepcion.getTipo() == TipoExcepcionDeVentana.REPROGRAMADA &&
                ocurreEn(ventana, primeraFecha, excepcion.getFecha()) &&
                excepcion.getNuevoDesde() != null &&
                excepcion.getNuevoHasta() != null &&
                cubre(excepcion.getNuevoDesde(), excepcion.getNuevoHasta(), instante)
            ) {
                return Optional.of(new Ocurrencia(excepcion.getNuevoDesde(), excepcion.getNuevoHasta(), true));
            }
        }

        Set<LocalDate> fechasConExcepcion = ventana.getExcepcions().stream().map(ExcepcionDeVentana::getFecha).collect(Collectors.toSet());

        // solo pueden cubrir el instante las ocurrencias que empezaron ese dia o, si la ventana
        // dura mas de un dia, en los dias inmediatamente anteriores
        long diasQueAbarca = ChronoUnit.DAYS.between(primeraFecha, finLocal.toLocalDate());
        LocalDate fechaDelInstante = instante.atZone(zona).toLocalDate();
        for (LocalDate fecha = fechaDelInstante.minusDays(diasQueAbarca + 1); !fecha.isAfter(fechaDelInstante); fecha = fecha.plusDays(1)) {
            if (!ocurreEn(ventana, primeraFecha, fecha) || fechasConExcepcion.contains(fecha)) {
                continue;
            }
            Instant desde = ZonedDateTime.ofLocal(fecha.atTime(inicioLocal.toLocalTime()), zona, offsetInicio).toInstant();
            Instant hasta = ZonedDateTime.ofLocal(
                fecha.plusDays(diasQueAbarca).atTime(finLocal.toLocalTime()),
                zona,
                offsetFin
            ).toInstant();
            if (cubre(desde, hasta, instante)) {
                return Optional.of(new Ocurrencia(desde, hasta, false));
            }
        }
        return Optional.empty();
    }

    /**
     * Si la ventana tiene una ocurrencia que empieza en esa fecha local.
     */
    static boolean ocurreEn(VentanaDeMantenimiento ventana, LocalDate primeraFecha, LocalDate fecha) {
        if (fecha == null || fecha.isBefore(primeraFecha)) {
            return false;
        }
        if (ventana.getRepetirHasta() != null && fecha.isAfter(ventana.getRepetirHasta())) {
            return false;
        }
        long dias = ChronoUnit.DAYS.between(primeraFecha, fecha);
        return switch (ventana.getRecurrencia()) {
            case UNICA -> dias == 0;
            case DIARIA -> true;
            case SEMANAL -> dias % 7 == 0;
        };
    }

    private static boolean cubre(Instant desde, Instant hasta, Instant instante) {
        return !desde.isAfter(instante) && hasta.isAfter(instante);
    }

    private static ZoneId zonaDe(VentanaDeMantenimiento ventana) {
        try {
            return ZoneId.of(ventana.getZonaHoraria());
        } catch (DateTimeException | NullPointerException e) {
            return ZoneOffset.UTC;
        }
    }
}
