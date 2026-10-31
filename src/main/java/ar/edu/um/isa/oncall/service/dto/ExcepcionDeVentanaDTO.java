package ar.edu.um.isa.oncall.service.dto;

import ar.edu.um.isa.oncall.domain.enumeration.TipoExcepcionDeVentana;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.ExcepcionDeVentana} entity.
 */
@Schema(
    description = "Una ocurrencia puntual de la ventana que se cancela o se mueve. fecha es\nel dia, en hora local, en que empezaba la ocurrencia afectada."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExcepcionDeVentanaDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private TipoExcepcionDeVentana tipo;

    private Instant nuevoDesde;

    private Instant nuevoHasta;

    @Size(max = 255)
    private String motivo;

    @NotNull
    private VentanaDeMantenimientoDTO ventana;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public TipoExcepcionDeVentana getTipo() {
        return tipo;
    }

    public void setTipo(TipoExcepcionDeVentana tipo) {
        this.tipo = tipo;
    }

    public Instant getNuevoDesde() {
        return nuevoDesde;
    }

    public void setNuevoDesde(Instant nuevoDesde) {
        this.nuevoDesde = nuevoDesde;
    }

    public Instant getNuevoHasta() {
        return nuevoHasta;
    }

    public void setNuevoHasta(Instant nuevoHasta) {
        this.nuevoHasta = nuevoHasta;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public VentanaDeMantenimientoDTO getVentana() {
        return ventana;
    }

    public void setVentana(VentanaDeMantenimientoDTO ventana) {
        this.ventana = ventana;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExcepcionDeVentanaDTO)) {
            return false;
        }

        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = (ExcepcionDeVentanaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, excepcionDeVentanaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExcepcionDeVentanaDTO{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", nuevoDesde='" + getNuevoDesde() + "'" +
            ", nuevoHasta='" + getNuevoHasta() + "'" +
            ", motivo='" + getMotivo() + "'" +
            ", ventana=" + getVentana() +
            "}";
    }
}
