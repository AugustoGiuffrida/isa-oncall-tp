package ar.edu.um.isa.oncall.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Un servicio cubierto por una ventana.
 */
@Entity
@Table(name = "servicio_en_ventana")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicioEnVentana implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "nota", length = 255)
    private String nota;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "servicios", "excepcions", "alertaSuprimidas" }, allowSetters = true)
    private VentanaDeMantenimiento ventana;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "equipo", "objetivos", "alertas", "politicas", "ventanas", "incidentes" }, allowSetters = true)
    private Servicio servicio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServicioEnVentana id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNota() {
        return this.nota;
    }

    public ServicioEnVentana nota(String nota) {
        this.setNota(nota);
        return this;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public VentanaDeMantenimiento getVentana() {
        return this.ventana;
    }

    public void setVentana(VentanaDeMantenimiento ventanaDeMantenimiento) {
        this.ventana = ventanaDeMantenimiento;
    }

    public ServicioEnVentana ventana(VentanaDeMantenimiento ventanaDeMantenimiento) {
        this.setVentana(ventanaDeMantenimiento);
        return this;
    }

    public Servicio getServicio() {
        return this.servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public ServicioEnVentana servicio(Servicio servicio) {
        this.setServicio(servicio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicioEnVentana)) {
            return false;
        }
        return getId() != null && getId().equals(((ServicioEnVentana) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioEnVentana{" +
            "id=" + getId() +
            ", nota='" + getNota() + "'" +
            "}";
    }
}
