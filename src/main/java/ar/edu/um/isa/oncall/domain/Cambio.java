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
 * Un cambio en produccion: un despliegue, una migracion, un cambio de
 * configuracion. Su autor es a quien se escala primero si el cambio
 * queda correlacionado con un incidente.
 */
@Entity
@Table(name = "cambio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cambio implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 140)
    @Column(name = "titulo", length = 140, nullable = false)
    private String titulo;

    @Size(max = 2000)
    @Column(name = "descripcion", length = 2000)
    private String descripcion;

    @Size(max = 255)
    @Column(name = "ticket_url", length = 255)
    private String ticketUrl;

    @NotNull
    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    @ManyToOne(optional = false)
    @NotNull
    private User autor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cambio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cambio", "servicio", "correlacions" }, allowSetters = true)
    private Set<DespliegueDeServicio> despliegues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cambio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Cambio titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Cambio descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTicketUrl() {
        return this.ticketUrl;
    }

    public Cambio ticketUrl(String ticketUrl) {
        this.setTicketUrl(ticketUrl);
        return this;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public Instant getCreadoEn() {
        return this.creadoEn;
    }

    public Cambio creadoEn(Instant creadoEn) {
        this.setCreadoEn(creadoEn);
        return this;
    }

    public void setCreadoEn(Instant creadoEn) {
        this.creadoEn = creadoEn;
    }

    public User getAutor() {
        return this.autor;
    }

    public void setAutor(User user) {
        this.autor = user;
    }

    public Cambio autor(User user) {
        this.setAutor(user);
        return this;
    }

    public Set<DespliegueDeServicio> getDespliegues() {
        return this.despliegues;
    }

    public void setDespliegues(Set<DespliegueDeServicio> despliegueDeServicios) {
        if (this.despliegues != null) {
            this.despliegues.forEach(i -> i.setCambio(null));
        }
        if (despliegueDeServicios != null) {
            despliegueDeServicios.forEach(i -> i.setCambio(this));
        }
        this.despliegues = despliegueDeServicios;
    }

    public Cambio despliegues(Set<DespliegueDeServicio> despliegueDeServicios) {
        this.setDespliegues(despliegueDeServicios);
        return this;
    }

    public Cambio addDespliegue(DespliegueDeServicio despliegueDeServicio) {
        this.despliegues.add(despliegueDeServicio);
        despliegueDeServicio.setCambio(this);
        return this;
    }

    public Cambio removeDespliegue(DespliegueDeServicio despliegueDeServicio) {
        this.despliegues.remove(despliegueDeServicio);
        despliegueDeServicio.setCambio(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cambio)) {
            return false;
        }
        return getId() != null && getId().equals(((Cambio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cambio{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", ticketUrl='" + getTicketUrl() + "'" +
            ", creadoEn='" + getCreadoEn() + "'" +
            "}";
    }
}
