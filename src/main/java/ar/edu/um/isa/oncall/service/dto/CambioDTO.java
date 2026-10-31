package ar.edu.um.isa.oncall.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.Cambio} entity.
 */
@Schema(
    description = "Un cambio en produccion: un despliegue, una migracion, un cambio de\nconfiguracion. Su autor es a quien se escala primero si el cambio\nqueda correlacionado con un incidente."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CambioDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 140)
    private String titulo;

    @Size(max = 2000)
    private String descripcion;

    @Size(max = 255)
    private String ticketUrl;

    @NotNull
    private Instant creadoEn;

    @NotNull
    private UserDTO autor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(Instant creadoEn) {
        this.creadoEn = creadoEn;
    }

    public UserDTO getAutor() {
        return autor;
    }

    public void setAutor(UserDTO autor) {
        this.autor = autor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CambioDTO)) {
            return false;
        }

        CambioDTO cambioDTO = (CambioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cambioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CambioDTO{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", ticketUrl='" + getTicketUrl() + "'" +
            ", creadoEn='" + getCreadoEn() + "'" +
            ", autor=" + getAutor() +
            "}";
    }
}
