package ar.edu.um.isa.oncall.service.dto;

import ar.edu.um.isa.oncall.domain.enumeration.Recurrencia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento} entity.
 */
@Schema(
    description = "Rango programado en el que las alertas de sus servicios no abren\nincidentes. desde/hasta son la primera ocurrencia; la recurrencia la\nrepite en hora local de la zona, hasta repetirHasta (inclusive)."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VentanaDeMantenimientoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 80)
    private String nombre;

    @Size(max = 500)
    private String descripcion;

    @NotNull
    private Instant desde;

    @NotNull
    private Instant hasta;

    @NotNull
    private Recurrencia recurrencia;

    @NotNull
    @Size(max = 50)
    private String zonaHoraria;

    private LocalDate repetirHasta;

    @NotNull
    private Boolean activa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getDesde() {
        return desde;
    }

    public void setDesde(Instant desde) {
        this.desde = desde;
    }

    public Instant getHasta() {
        return hasta;
    }

    public void setHasta(Instant hasta) {
        this.hasta = hasta;
    }

    public Recurrencia getRecurrencia() {
        return recurrencia;
    }

    public void setRecurrencia(Recurrencia recurrencia) {
        this.recurrencia = recurrencia;
    }

    public String getZonaHoraria() {
        return zonaHoraria;
    }

    public void setZonaHoraria(String zonaHoraria) {
        this.zonaHoraria = zonaHoraria;
    }

    public LocalDate getRepetirHasta() {
        return repetirHasta;
    }

    public void setRepetirHasta(LocalDate repetirHasta) {
        this.repetirHasta = repetirHasta;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VentanaDeMantenimientoDTO)) {
            return false;
        }

        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = (VentanaDeMantenimientoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ventanaDeMantenimientoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentanaDeMantenimientoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", desde='" + getDesde() + "'" +
            ", hasta='" + getHasta() + "'" +
            ", recurrencia='" + getRecurrencia() + "'" +
            ", zonaHoraria='" + getZonaHoraria() + "'" +
            ", repetirHasta='" + getRepetirHasta() + "'" +
            ", activa='" + getActiva() + "'" +
            "}";
    }
}
