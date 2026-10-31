package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.ExcepcionDeVentana;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExcepcionDeVentana entity.
 */
@Repository
public interface ExcepcionDeVentanaRepository extends JpaRepository<ExcepcionDeVentana, Long> {
    default Optional<ExcepcionDeVentana> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ExcepcionDeVentana> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ExcepcionDeVentana> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select excepcionDeVentana from ExcepcionDeVentana excepcionDeVentana left join fetch excepcionDeVentana.ventana",
        countQuery = "select count(excepcionDeVentana) from ExcepcionDeVentana excepcionDeVentana"
    )
    Page<ExcepcionDeVentana> findAllWithToOneRelationships(Pageable pageable);

    @Query("select excepcionDeVentana from ExcepcionDeVentana excepcionDeVentana left join fetch excepcionDeVentana.ventana")
    List<ExcepcionDeVentana> findAllWithToOneRelationships();

    @Query(
        "select excepcionDeVentana from ExcepcionDeVentana excepcionDeVentana left join fetch excepcionDeVentana.ventana where excepcionDeVentana.id =:id"
    )
    Optional<ExcepcionDeVentana> findOneWithToOneRelationships(@Param("id") Long id);
}
