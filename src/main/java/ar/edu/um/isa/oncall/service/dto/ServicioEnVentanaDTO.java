package ar.edu.um.isa.oncall.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.ServicioEnVentana} entity.
 */
@Schema(description = "Un servicio cubierto por una ventana.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicioEnVentanaDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String nota;

    @NotNull
    private VentanaDeMantenimientoDTO ventana;

    @NotNull
    private ServicioDTO servicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public VentanaDeMantenimientoDTO getVentana() {
        return ventana;
    }

    public void setVentana(VentanaDeMantenimientoDTO ventana) {
        this.ventana = ventana;
    }

    public ServicioDTO getServicio() {
        return servicio;
    }

    public void setServicio(ServicioDTO servicio) {
        this.servicio = servicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicioEnVentanaDTO)) {
            return false;
        }

        ServicioEnVentanaDTO servicioEnVentanaDTO = (ServicioEnVentanaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, servicioEnVentanaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioEnVentanaDTO{" +
            "id=" + getId() +
            ", nota='" + getNota() + "'" +
            ", ventana=" + getVentana() +
            ", servicio=" + getServicio() +
            "}";
    }
}
