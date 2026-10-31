package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.ServicioEnVentana;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServicioEnVentana entity.
 */
@Repository
public interface ServicioEnVentanaRepository extends JpaRepository<ServicioEnVentana, Long> {
    default Optional<ServicioEnVentana> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ServicioEnVentana> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ServicioEnVentana> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select servicioEnVentana from ServicioEnVentana servicioEnVentana left join fetch servicioEnVentana.ventana left join fetch servicioEnVentana.servicio",
        countQuery = "select count(servicioEnVentana) from ServicioEnVentana servicioEnVentana"
    )
    Page<ServicioEnVentana> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select servicioEnVentana from ServicioEnVentana servicioEnVentana left join fetch servicioEnVentana.ventana left join fetch servicioEnVentana.servicio"
    )
    List<ServicioEnVentana> findAllWithToOneRelationships();

    @Query(
        "select servicioEnVentana from ServicioEnVentana servicioEnVentana left join fetch servicioEnVentana.ventana left join fetch servicioEnVentana.servicio where servicioEnVentana.id =:id"
    )
    Optional<ServicioEnVentana> findOneWithToOneRelationships(@Param("id") Long id);
}
