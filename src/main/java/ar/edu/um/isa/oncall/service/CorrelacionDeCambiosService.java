package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.CorrelacionCambioIncidente;
import ar.edu.um.isa.oncall.domain.DespliegueDeServicio;
import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.domain.enumeration.EstadoCorrelacion;
import ar.edu.um.isa.oncall.domain.enumeration.OrigenCorrelacion;
import ar.edu.um.isa.oncall.repository.CorrelacionCambioIncidenteRepository;
import ar.edu.um.isa.oncall.repository.CorrelacionDeCambiosRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Extension 6 · Gestion de cambios: que cambio sospechamos que rompio el incidente.
 *
 * <p>Un despliegue sobre alguno de los servicios del incidente, dentro de la ventana
 * {@code oncall.cambios.ventana-correlacion-minutos} anterior a la deteccion, queda
 * correlacionado automaticamente como {@code SUGERIDA}. Una persona puede cargar
 * correlaciones a mano (por el ABM) o descartar las automaticas: una {@code DESCARTADA} no
 * cuenta.</p>
 *
 * <p>Si hay varias correlaciones vigentes, el sospechoso principal es el despliegue mas
 * cercano a la deteccion: el ultimo que se toco antes de que se rompiera. A su autor va el
 * nivel 0 del escalamiento.</p>
 */
@Service
@Transactional
public class CorrelacionDeCambiosService {

    private static final Logger LOG = LoggerFactory.getLogger(CorrelacionDeCambiosService.class);

    /** Ventana usada cuando la propiedad no esta configurada: una hora. */
    public static final int VENTANA_POR_DEFECTO_MINUTOS = 60;

    private static final int MAX_NOTA = 255;

    private final CorrelacionDeCambiosRepository correlacionDeCambiosRepository;
    private final CorrelacionCambioIncidenteRepository correlacionCambioIncidenteRepository;

    private final Duration ventana;

    public CorrelacionDeCambiosService(
        CorrelacionDeCambiosRepository correlacionDeCambiosRepository,
        CorrelacionCambioIncidenteRepository correlacionCambioIncidenteRepository,
        @Value("${oncall.cambios.ventana-correlacion-minutos:" + VENTANA_POR_DEFECTO_MINUTOS + "}") int ventanaMinutos
    ) {
        if (ventanaMinutos < 1) {
            throw new IllegalArgumentException("oncall.cambios.ventana-correlacion-minutos debe ser al menos 1, y es " + ventanaMinutos);
        }
        this.correlacionDeCambiosRepository = correlacionDeCambiosRepository;
        this.correlacionCambioIncidenteRepository = correlacionCambioIncidenteRepository;
        this.ventana = Duration.ofMinutes(ventanaMinutos);
    }

    /**
     * Correlaciona el incidente con los despliegues sospechosos y devuelve a cual escalar primero.
     *
     * @param incidente el incidente, con sus servicios accesibles.
     * @param ahora el instante de la corrida, que queda como {@code detectadaEn} de las
     *        correlaciones nuevas.
     * @return la correlacion vigente mas cercana a la deteccion, o vacio si no hay ninguna.
     */
    public Optional<CorrelacionCambioIncidente> sospechosoPrincipal(Incidente incidente, Instant ahora) {
        List<CorrelacionCambioIncidente> correlaciones = new ArrayList<>(
            correlacionDeCambiosRepository.buscarCorrelaciones(incidente.getId())
        );
        correlaciones.addAll(correlacionarAutomaticamente(incidente, correlaciones, ahora));

        return correlaciones
            .stream()
            .filter(correlacion -> correlacion.getEstado() != EstadoCorrelacion.DESCARTADA)
            .max(
                Comparator.comparing((CorrelacionCambioIncidente correlacion) ->
                    correlacion.getDespliegue().getDesplegadoEn()
                ).thenComparing(CorrelacionCambioIncidente::getId, Comparator.nullsFirst(Comparator.naturalOrder()))
            );
    }

    /**
     * Crea una correlacion {@code AUTOMATICA} por cada despliegue de la ventana que todavia no
     * este correlacionado con el incidente, en ningun estado: si alguien ya la descarto, no se
     * vuelve a sugerir.
     */
    private List<CorrelacionCambioIncidente> correlacionarAutomaticamente(
        Incidente incidente,
        List<CorrelacionCambioIncidente> existentes,
        Instant ahora
    ) {
        Set<Long> servicioIds = incidente.getServicios().stream().map(Servicio::getId).collect(Collectors.toSet());
        if (servicioIds.isEmpty() || incidente.getDetectadoEn() == null) {
            return List.of();
        }
        Set<Long> yaCorrelacionados = existentes
            .stream()
            .map(correlacion -> correlacion.getDespliegue().getId())
            .collect(Collectors.toSet());

        Instant detectadoEn = incidente.getDetectadoEn();
        List<CorrelacionCambioIncidente> nuevas = new ArrayList<>();
        for (DespliegueDeServicio despliegue : correlacionDeCambiosRepository.buscarDesplieguesEntre(
            servicioIds,
            detectadoEn.minus(ventana),
            detectadoEn
        )) {
            if (yaCorrelacionados.contains(despliegue.getId())) {
                continue;
            }
            long minutosAntes = Duration.between(despliegue.getDesplegadoEn(), detectadoEn).toMinutes();
            String nota =
                "Despliegue de la version " +
                despliegue.getVersion() +
                " sobre " +
                despliegue.getServicio().getNombre() +
                ", " +
                minutosAntes +
                " minutos antes de la deteccion.";
            CorrelacionCambioIncidente correlacion = new CorrelacionCambioIncidente()
                .origen(OrigenCorrelacion.AUTOMATICA)
                .estado(EstadoCorrelacion.SUGERIDA)
                .detectadaEn(ahora)
                .nota(nota.length() <= MAX_NOTA ? nota : nota.substring(0, MAX_NOTA - 3) + "...")
                .incidente(incidente)
                .despliegue(despliegue);
            nuevas.add(correlacionCambioIncidenteRepository.save(correlacion));
            LOG.debug("Incidente {}: {}", incidente.getId(), nota);
        }
        return nuevas;
    }
}
