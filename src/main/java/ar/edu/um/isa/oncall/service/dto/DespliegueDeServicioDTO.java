package ar.edu.um.isa.oncall.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.DespliegueDeServicio} entity.
 */
@Schema(
    description = "Cada vez que un cambio llega a un servicio. Es lo que se correlaciona\ncon los incidentes: el mismo cambio puede desplegarse en varios."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DespliegueDeServicioDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 60)
    private String version;

    @NotNull
    private Instant desplegadoEn;

    @NotNull
    private CambioDTO cambio;

    @NotNull
    private ServicioDTO servicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Instant getDesplegadoEn() {
        return desplegadoEn;
    }

    public void setDesplegadoEn(Instant desplegadoEn) {
        this.desplegadoEn = desplegadoEn;
    }

    public CambioDTO getCambio() {
        return cambio;
    }

    public void setCambio(CambioDTO cambio) {
        this.cambio = cambio;
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
        if (!(o instanceof DespliegueDeServicioDTO)) {
            return false;
        }

        DespliegueDeServicioDTO despliegueDeServicioDTO = (DespliegueDeServicioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, despliegueDeServicioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DespliegueDeServicioDTO{" +
            "id=" + getId() +
            ", version='" + getVersion() + "'" +
            ", desplegadoEn='" + getDesplegadoEn() + "'" +
            ", cambio=" + getCambio() +
            ", servicio=" + getServicio() +
            "}";
    }
}
