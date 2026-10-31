package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.DespliegueDeServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DespliegueDeServicio entity.
 */
@Repository
public interface DespliegueDeServicioRepository extends JpaRepository<DespliegueDeServicio, Long> {
    default Optional<DespliegueDeServicio> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DespliegueDeServicio> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DespliegueDeServicio> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select despliegueDeServicio from DespliegueDeServicio despliegueDeServicio left join fetch despliegueDeServicio.cambio left join fetch despliegueDeServicio.servicio",
        countQuery = "select count(despliegueDeServicio) from DespliegueDeServicio despliegueDeServicio"
    )
    Page<DespliegueDeServicio> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select despliegueDeServicio from DespliegueDeServicio despliegueDeServicio left join fetch despliegueDeServicio.cambio left join fetch despliegueDeServicio.servicio"
    )
    List<DespliegueDeServicio> findAllWithToOneRelationships();

    @Query(
        "select despliegueDeServicio from DespliegueDeServicio despliegueDeServicio left join fetch despliegueDeServicio.cambio left join fetch despliegueDeServicio.servicio where despliegueDeServicio.id =:id"
    )
    Optional<DespliegueDeServicio> findOneWithToOneRelationships(@Param("id") Long id);
}
