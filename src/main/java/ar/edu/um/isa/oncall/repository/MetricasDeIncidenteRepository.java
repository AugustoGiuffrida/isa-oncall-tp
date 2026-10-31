package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.ObjetivoDeServicio;
import ar.edu.um.isa.oncall.domain.enumeration.TipoObjetivo;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Consultas que necesita la regla de MTTA / MTTR. Aparte de los repositorios generados, por
 * la misma razon que {@link IncidenteDeduplicacionRepository}.
 */
@Repository
public interface MetricasDeIncidenteRepository extends org.springframework.data.repository.Repository<Incidente, Long> {
    /**
     * El incidente con sus servicios, que son los que definen que objetivos le aplican.
     *
     * @param id el incidente.
     * @return el incidente, o vacio si no existe.
     */
    @Query("select incidente from Incidente incidente left join fetch incidente.servicios where incidente.id = :id")
    Optional<Incidente> buscarConServicios(@Param("id") Long id);

    /**
     * Los objetivos de tiempo de un conjunto de servicios. La severidad se filtra despues, en
     * la regla.
     *
     * @param servicioIds los servicios.
     * @param tipos los tipos de objetivo que se miden.
     * @return los objetivos, con su servicio.
     */
    @Query(
        """
        select objetivo from ObjetivoDeServicio objetivo
            join fetch objetivo.servicio servicio
        where servicio.id in :servicioIds and objetivo.tipo in :tipos
        """
    )
    List<ObjetivoDeServicio> buscarObjetivos(
        @Param("servicioIds") Collection<Long> servicioIds,
        @Param("tipos") Collection<TipoObjetivo> tipos
    );
}
