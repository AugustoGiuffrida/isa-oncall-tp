package ar.edu.um.isa.oncall.domain;

import ar.edu.um.isa.oncall.domain.enumeration.EstadoCorrelacion;
import ar.edu.um.isa.oncall.domain.enumeration.OrigenCorrelacion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Sospecha de que un despliegue causo un incidente. La crea la regla de
 * escalamiento (AUTOMATICA) o una persona (MANUAL); una DESCARTADA no cuenta.
 */
@Entity
@Table(name = "correlacion_cambio_incidente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CorrelacionCambioIncidente implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "origen", nullable = false)
    private OrigenCorrelacion origen;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCorrelacion estado;

    @NotNull
    @Column(name = "detectada_en", nullable = false)
    private Instant detectadaEn;

    @Size(max = 255)
    @Column(name = "nota", length = 255)
    private String nota;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "comandante",
            "servicios",
            "postmortem",
            "alertas",
            "eventos",
            "ejecucionDeEscalamientos",
            "notificacions",
            "correlacions",
        },
        allowSetters = true
    )
    private Incidente incidente;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cambio", "servicio", "correlacions" }, allowSetters = true)
    private DespliegueDeServicio despliegue;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CorrelacionCambioIncidente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrigenCorrelacion getOrigen() {
        return this.origen;
    }

    public CorrelacionCambioIncidente origen(OrigenCorrelacion origen) {
        this.setOrigen(origen);
        return this;
    }

    public void setOrigen(OrigenCorrelacion origen) {
        this.origen = origen;
    }

    public EstadoCorrelacion getEstado() {
        return this.estado;
    }

    public CorrelacionCambioIncidente estado(EstadoCorrelacion estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoCorrelacion estado) {
        this.estado = estado;
    }

    public Instant getDetectadaEn() {
        return this.detectadaEn;
    }

    public CorrelacionCambioIncidente detectadaEn(Instant detectadaEn) {
        this.setDetectadaEn(detectadaEn);
        return this;
    }

    public void setDetectadaEn(Instant detectadaEn) {
        this.detectadaEn = detectadaEn;
    }

    public String getNota() {
        return this.nota;
    }

    public CorrelacionCambioIncidente nota(String nota) {
        this.setNota(nota);
        return this;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Incidente getIncidente() {
        return this.incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }

    public CorrelacionCambioIncidente incidente(Incidente incidente) {
        this.setIncidente(incidente);
        return this;
    }

    public DespliegueDeServicio getDespliegue() {
        return this.despliegue;
    }

    public void setDespliegue(DespliegueDeServicio despliegueDeServicio) {
        this.despliegue = despliegueDeServicio;
    }

    public CorrelacionCambioIncidente despliegue(DespliegueDeServicio despliegueDeServicio) {
        this.setDespliegue(despliegueDeServicio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CorrelacionCambioIncidente)) {
            return false;
        }
        return getId() != null && getId().equals(((CorrelacionCambioIncidente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CorrelacionCambioIncidente{" +
            "id=" + getId() +
            ", origen='" + getOrigen() + "'" +
            ", estado='" + getEstado() + "'" +
            ", detectadaEn='" + getDetectadaEn() + "'" +
            ", nota='" + getNota() + "'" +
            "}";
    }
}
