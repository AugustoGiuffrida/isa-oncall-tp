package ar.edu.um.isa.oncall.domain;

import ar.edu.um.isa.oncall.domain.enumeration.Recurrencia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Rango programado en el que las alertas de sus servicios no abren
 * incidentes. desde/hasta son la primera ocurrencia; la recurrencia la
 * repite en hora local de la zona, hasta repetirHasta (inclusive).
 */
@Entity
@Table(name = "ventana_de_mantenimiento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VentanaDeMantenimiento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "nombre", length = 80, nullable = false)
    private String nombre;

    @Size(max = 500)
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @NotNull
    @Column(name = "desde", nullable = false)
    private Instant desde;

    @NotNull
    @Column(name = "hasta", nullable = false)
    private Instant hasta;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "recurrencia", nullable = false)
    private Recurrencia recurrencia;

    @NotNull
    @Size(max = 50)
    @Column(name = "zona_horaria", length = 50, nullable = false)
    private String zonaHoraria;

    @Column(name = "repetir_hasta")
    private LocalDate repetirHasta;

    @NotNull
    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ventana")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ventana", "servicio" }, allowSetters = true)
    private Set<ServicioEnVentana> servicios = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ventana")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ventana" }, allowSetters = true)
    private Set<ExcepcionDeVentana> excepcions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "suprimidaPor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "servicio", "incidente", "suprimidaPor" }, allowSetters = true)
    private Set<Alerta> alertaSuprimidas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VentanaDeMantenimiento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public VentanaDeMantenimiento nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public VentanaDeMantenimiento descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getDesde() {
        return this.desde;
    }

    public VentanaDeMantenimiento desde(Instant desde) {
        this.setDesde(desde);
        return this;
    }

    public void setDesde(Instant desde) {
        this.desde = desde;
    }

    public Instant getHasta() {
        return this.hasta;
    }

    public VentanaDeMantenimiento hasta(Instant hasta) {
        this.setHasta(hasta);
        return this;
    }

    public void setHasta(Instant hasta) {
        this.hasta = hasta;
    }

    public Recurrencia getRecurrencia() {
        return this.recurrencia;
    }

    public VentanaDeMantenimiento recurrencia(Recurrencia recurrencia) {
        this.setRecurrencia(recurrencia);
        return this;
    }

    public void setRecurrencia(Recurrencia recurrencia) {
        this.recurrencia = recurrencia;
    }

    public String getZonaHoraria() {
        return this.zonaHoraria;
    }

    public VentanaDeMantenimiento zonaHoraria(String zonaHoraria) {
        this.setZonaHoraria(zonaHoraria);
        return this;
    }

    public void setZonaHoraria(String zonaHoraria) {
        this.zonaHoraria = zonaHoraria;
    }

    public LocalDate getRepetirHasta() {
        return this.repetirHasta;
    }

    public VentanaDeMantenimiento repetirHasta(LocalDate repetirHasta) {
        this.setRepetirHasta(repetirHasta);
        return this;
    }

    public void setRepetirHasta(LocalDate repetirHasta) {
        this.repetirHasta = repetirHasta;
    }

    public Boolean getActiva() {
        return this.activa;
    }

    public VentanaDeMantenimiento activa(Boolean activa) {
        this.setActiva(activa);
        return this;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Set<ServicioEnVentana> getServicios() {
        return this.servicios;
    }

    public void setServicios(Set<ServicioEnVentana> servicioEnVentanas) {
        if (this.servicios != null) {
            this.servicios.forEach(i -> i.setVentana(null));
        }
        if (servicioEnVentanas != null) {
            servicioEnVentanas.forEach(i -> i.setVentana(this));
        }
        this.servicios = servicioEnVentanas;
    }

    public VentanaDeMantenimiento servicios(Set<ServicioEnVentana> servicioEnVentanas) {
        this.setServicios(servicioEnVentanas);
        return this;
    }

    public VentanaDeMantenimiento addServicio(ServicioEnVentana servicioEnVentana) {
        this.servicios.add(servicioEnVentana);
        servicioEnVentana.setVentana(this);
        return this;
    }

    public VentanaDeMantenimiento removeServicio(ServicioEnVentana servicioEnVentana) {
        this.servicios.remove(servicioEnVentana);
        servicioEnVentana.setVentana(null);
        return this;
    }

    public Set<ExcepcionDeVentana> getExcepcions() {
        return this.excepcions;
    }

    public void setExcepcions(Set<ExcepcionDeVentana> excepcionDeVentanas) {
        if (this.excepcions != null) {
            this.excepcions.forEach(i -> i.setVentana(null));
        }
        if (excepcionDeVentanas != null) {
            excepcionDeVentanas.forEach(i -> i.setVentana(this));
        }
        this.excepcions = excepcionDeVentanas;
    }

    public VentanaDeMantenimiento excepcions(Set<ExcepcionDeVentana> excepcionDeVentanas) {
        this.setExcepcions(excepcionDeVentanas);
        return this;
    }

    public VentanaDeMantenimiento addExcepcion(ExcepcionDeVentana excepcionDeVentana) {
        this.excepcions.add(excepcionDeVentana);
        excepcionDeVentana.setVentana(this);
        return this;
    }

    public VentanaDeMantenimiento removeExcepcion(ExcepcionDeVentana excepcionDeVentana) {
        this.excepcions.remove(excepcionDeVentana);
        excepcionDeVentana.setVentana(null);
        return this;
    }

    public Set<Alerta> getAlertaSuprimidas() {
        return this.alertaSuprimidas;
    }

    public void setAlertaSuprimidas(Set<Alerta> alertas) {
        if (this.alertaSuprimidas != null) {
            this.alertaSuprimidas.forEach(i -> i.setSuprimidaPor(null));
        }
        if (alertas != null) {
            alertas.forEach(i -> i.setSuprimidaPor(this));
        }
        this.alertaSuprimidas = alertas;
    }

    public VentanaDeMantenimiento alertaSuprimidas(Set<Alerta> alertas) {
        this.setAlertaSuprimidas(alertas);
        return this;
    }

    public VentanaDeMantenimiento addAlertaSuprimida(Alerta alerta) {
        this.alertaSuprimidas.add(alerta);
        alerta.setSuprimidaPor(this);
        return this;
    }

    public VentanaDeMantenimiento removeAlertaSuprimida(Alerta alerta) {
        this.alertaSuprimidas.remove(alerta);
        alerta.setSuprimidaPor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VentanaDeMantenimiento)) {
            return false;
        }
        return getId() != null && getId().equals(((VentanaDeMantenimiento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentanaDeMantenimiento{" +
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
