package ar.edu.um.isa.oncall.domain;

import ar.edu.um.isa.oncall.domain.enumeration.TipoExcepcionDeVentana;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Una ocurrencia puntual de la ventana que se cancela o se mueve. fecha es
 * el dia, en hora local, en que empezaba la ocurrencia afectada.
 */
@Entity
@Table(name = "excepcion_de_ventana")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExcepcionDeVentana implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoExcepcionDeVentana tipo;

    @Column(name = "nuevo_desde")
    private Instant nuevoDesde;

    @Column(name = "nuevo_hasta")
    private Instant nuevoHasta;

    @Size(max = 255)
    @Column(name = "motivo", length = 255)
    private String motivo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "servicios", "excepcions", "alertaSuprimidas" }, allowSetters = true)
    private VentanaDeMantenimiento ventana;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExcepcionDeVentana id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public ExcepcionDeVentana fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public TipoExcepcionDeVentana getTipo() {
        return this.tipo;
    }

    public ExcepcionDeVentana tipo(TipoExcepcionDeVentana tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoExcepcionDeVentana tipo) {
        this.tipo = tipo;
    }

    public Instant getNuevoDesde() {
        return this.nuevoDesde;
    }

    public ExcepcionDeVentana nuevoDesde(Instant nuevoDesde) {
        this.setNuevoDesde(nuevoDesde);
        return this;
    }

    public void setNuevoDesde(Instant nuevoDesde) {
        this.nuevoDesde = nuevoDesde;
    }

    public Instant getNuevoHasta() {
        return this.nuevoHasta;
    }

    public ExcepcionDeVentana nuevoHasta(Instant nuevoHasta) {
        this.setNuevoHasta(nuevoHasta);
        return this;
    }

    public void setNuevoHasta(Instant nuevoHasta) {
        this.nuevoHasta = nuevoHasta;
    }

    public String getMotivo() {
        return this.motivo;
    }

    public ExcepcionDeVentana motivo(String motivo) {
        this.setMotivo(motivo);
        return this;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public VentanaDeMantenimiento getVentana() {
        return this.ventana;
    }

    public void setVentana(VentanaDeMantenimiento ventanaDeMantenimiento) {
        this.ventana = ventanaDeMantenimiento;
    }

    public ExcepcionDeVentana ventana(VentanaDeMantenimiento ventanaDeMantenimiento) {
        this.setVentana(ventanaDeMantenimiento);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExcepcionDeVentana)) {
            return false;
        }
        return getId() != null && getId().equals(((ExcepcionDeVentana) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExcepcionDeVentana{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", nuevoDesde='" + getNuevoDesde() + "'" +
            ", nuevoHasta='" + getNuevoHasta() + "'" +
            ", motivo='" + getMotivo() + "'" +
            "}";
    }
}
