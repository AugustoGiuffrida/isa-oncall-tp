package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VentanaDeMantenimiento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VentanaDeMantenimientoRepository extends JpaRepository<VentanaDeMantenimiento, Long> {}
