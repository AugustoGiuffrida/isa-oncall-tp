package ar.edu.um.isa.oncall.service.dto;

import ar.edu.um.isa.oncall.domain.enumeration.EstadoCorrelacion;
import ar.edu.um.isa.oncall.domain.enumeration.OrigenCorrelacion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.CorrelacionCambioIncidente} entity.
 */
@Schema(
    description = "Sospecha de que un despliegue causo un incidente. La crea la regla de\nescalamiento (AUTOMATICA) o una persona (MANUAL); una DESCARTADA no cuenta."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CorrelacionCambioIncidenteDTO implements Serializable {

    private Long id;

    @NotNull
    private OrigenCorrelacion origen;

    @NotNull
    private EstadoCorrelacion estado;

    @NotNull
    private Instant detectadaEn;

    @Size(max = 255)
    private String nota;

    @NotNull
    private IncidenteDTO incidente;

    @NotNull
    private DespliegueDeServicioDTO despliegue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrigenCorrelacion getOrigen() {
        return origen;
    }

    public void setOrigen(OrigenCorrelacion origen) {
        this.origen = origen;
    }

    public EstadoCorrelacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoCorrelacion estado) {
        this.estado = estado;
    }

    public Instant getDetectadaEn() {
        return detectadaEn;
    }

    public void setDetectadaEn(Instant detectadaEn) {
        this.detectadaEn = detectadaEn;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public IncidenteDTO getIncidente() {
        return incidente;
    }

    public void setIncidente(IncidenteDTO incidente) {
        this.incidente = incidente;
    }

    public DespliegueDeServicioDTO getDespliegue() {
        return despliegue;
    }

    public void setDespliegue(DespliegueDeServicioDTO despliegue) {
        this.despliegue = despliegue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CorrelacionCambioIncidenteDTO)) {
            return false;
        }

        CorrelacionCambioIncidenteDTO correlacionCambioIncidenteDTO = (CorrelacionCambioIncidenteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, correlacionCambioIncidenteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CorrelacionCambioIncidenteDTO{" +
            "id=" + getId() +
            ", origen='" + getOrigen() + "'" +
            ", estado='" + getEstado() + "'" +
            ", detectadaEn='" + getDetectadaEn() + "'" +
            ", nota='" + getNota() + "'" +
            ", incidente=" + getIncidente() +
            ", despliegue=" + getDespliegue() +
            "}";
    }
}
