package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.Cambio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cambio entity.
 */
@Repository
public interface CambioRepository extends JpaRepository<Cambio, Long> {
    @Query("select cambio from Cambio cambio where cambio.autor.login = ?#{authentication.name}")
    List<Cambio> findByAutorIsCurrentUser();

    default Optional<Cambio> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Cambio> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Cambio> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select cambio from Cambio cambio left join fetch cambio.autor", countQuery = "select count(cambio) from Cambio cambio")
    Page<Cambio> findAllWithToOneRelationships(Pageable pageable);

    @Query("select cambio from Cambio cambio left join fetch cambio.autor")
    List<Cambio> findAllWithToOneRelationships();

    @Query("select cambio from Cambio cambio left join fetch cambio.autor where cambio.id =:id")
    Optional<Cambio> findOneWithToOneRelationships(@Param("id") Long id);
}
