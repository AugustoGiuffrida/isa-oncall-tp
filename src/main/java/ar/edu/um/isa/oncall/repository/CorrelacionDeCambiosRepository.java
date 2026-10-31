package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.CorrelacionCambioIncidente;
import ar.edu.um.isa.oncall.domain.DespliegueDeServicio;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Consultas que necesita la correlacion entre cambios e incidentes. Aparte de los
 * repositorios generados, por la misma razon que {@link IncidenteDeduplicacionRepository}.
 */
@Repository
public interface CorrelacionDeCambiosRepository extends org.springframework.data.repository.Repository<DespliegueDeServicio, Long> {
    /**
     * Despliegues sobre alguno de los servicios dados en el periodo {@code [desde, hasta]},
     * con su cambio y el autor del cambio.
     *
     * @param servicioIds los servicios afectados por el incidente.
     * @param desde inicio de la ventana, inclusive.
     * @param hasta fin de la ventana, inclusive: un despliegue en el mismo instante de la
     *        deteccion es sospechoso.
     * @return los despliegues de la ventana.
     */
    @Query(
        """
        select despliegue from DespliegueDeServicio despliegue
            join fetch despliegue.cambio cambio
            left join fetch cambio.autor
            join fetch despliegue.servicio servicio
        where servicio.id in :servicioIds
            and despliegue.desplegadoEn >= :desde
            and despliegue.desplegadoEn <= :hasta
        """
    )
    List<DespliegueDeServicio> buscarDesplieguesEntre(
        @Param("servicioIds") Collection<Long> servicioIds,
        @Param("desde") Instant desde,
        @Param("hasta") Instant hasta
    );

    /**
     * Las correlaciones de un incidente, automaticas y manuales, con el despliegue, el cambio
     * y su autor.
     *
     * @param incidenteId el incidente.
     * @return sus correlaciones, en cualquier estado.
     */
    @Query(
        """
        select correlacion from CorrelacionCambioIncidente correlacion
            join fetch correlacion.despliegue despliegue
            join fetch despliegue.cambio cambio
            left join fetch cambio.autor
            join fetch despliegue.servicio
        where correlacion.incidente.id = :incidenteId
        """
    )
    List<CorrelacionCambioIncidente> buscarCorrelaciones(@Param("incidenteId") Long incidenteId);
}
