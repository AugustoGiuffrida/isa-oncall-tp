package ar.edu.um.isa.oncall.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Cada vez que un cambio llega a un servicio. Es lo que se correlaciona
 * con los incidentes: el mismo cambio puede desplegarse en varios.
 */
@Entity
@Table(name = "despliegue_de_servicio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DespliegueDeServicio implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "version", length = 60, nullable = false)
    private String version;

    @NotNull
    @Column(name = "desplegado_en", nullable = false)
    private Instant desplegadoEn;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "autor", "despliegues" }, allowSetters = true)
    private Cambio cambio;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "equipo", "objetivos", "alertas", "politicas", "despliegues", "incidentes" }, allowSetters = true)
    private Servicio servicio;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "despliegue")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "incidente", "despliegue" }, allowSetters = true)
    private Set<CorrelacionCambioIncidente> correlacions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DespliegueDeServicio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return this.version;
    }

    public DespliegueDeServicio version(String version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Instant getDesplegadoEn() {
        return this.desplegadoEn;
    }

    public DespliegueDeServicio desplegadoEn(Instant desplegadoEn) {
        this.setDesplegadoEn(desplegadoEn);
        return this;
    }

    public void setDesplegadoEn(Instant desplegadoEn) {
        this.desplegadoEn = desplegadoEn;
    }

    public Cambio getCambio() {
        return this.cambio;
    }

    public void setCambio(Cambio cambio) {
        this.cambio = cambio;
    }

    public DespliegueDeServicio cambio(Cambio cambio) {
        this.setCambio(cambio);
        return this;
    }

    public Servicio getServicio() {
        return this.servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public DespliegueDeServicio servicio(Servicio servicio) {
        this.setServicio(servicio);
        return this;
    }

    public Set<CorrelacionCambioIncidente> getCorrelacions() {
        return this.correlacions;
    }

    public void setCorrelacions(Set<CorrelacionCambioIncidente> correlacionCambioIncidentes) {
        if (this.correlacions != null) {
            this.correlacions.forEach(i -> i.setDespliegue(null));
        }
        if (correlacionCambioIncidentes != null) {
            correlacionCambioIncidentes.forEach(i -> i.setDespliegue(this));
        }
        this.correlacions = correlacionCambioIncidentes;
    }

    public DespliegueDeServicio correlacions(Set<CorrelacionCambioIncidente> correlacionCambioIncidentes) {
        this.setCorrelacions(correlacionCambioIncidentes);
        return this;
    }

    public DespliegueDeServicio addCorrelacion(CorrelacionCambioIncidente correlacionCambioIncidente) {
        this.correlacions.add(correlacionCambioIncidente);
        correlacionCambioIncidente.setDespliegue(this);
        return this;
    }

    public DespliegueDeServicio removeCorrelacion(CorrelacionCambioIncidente correlacionCambioIncidente) {
        this.correlacions.remove(correlacionCambioIncidente);
        correlacionCambioIncidente.setDespliegue(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DespliegueDeServicio)) {
            return false;
        }
        return getId() != null && getId().equals(((DespliegueDeServicio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DespliegueDeServicio{" +
            "id=" + getId() +
            ", version='" + getVersion() + "'" +
            ", desplegadoEn='" + getDesplegadoEn() + "'" +
            "}";
    }
}
