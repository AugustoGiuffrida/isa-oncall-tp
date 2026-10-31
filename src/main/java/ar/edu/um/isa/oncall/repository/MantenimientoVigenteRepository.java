package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Consultas que necesita la regla de ventanas de mantenimiento. Aparte de los repositorios
 * generados, por la misma razon que {@link IncidenteDeduplicacionRepository}.
 */
@Repository
public interface MantenimientoVigenteRepository extends org.springframework.data.repository.Repository<VentanaDeMantenimiento, Long> {
    /**
     * Las ventanas activas que cubren al servicio, con sus excepciones. Que alguna este
     * vigente en un instante lo decide la regla: la recurrencia no se puede expresar en JPQL.
     *
     * @param servicioId el servicio.
     * @return las ventanas activas del servicio.
     */
    @Query(
        """
        select distinct ventana from VentanaDeMantenimiento ventana
            left join fetch ventana.excepcions
        where ventana.activa = true
            and exists (
                select 1 from ServicioEnVentana enVentana
                where enVentana.ventana = ventana and enVentana.servicio.id = :servicioId
            )
        """
    )
    List<VentanaDeMantenimiento> buscarVentanasActivasDeServicio(@Param("servicioId") Long servicioId);
}
