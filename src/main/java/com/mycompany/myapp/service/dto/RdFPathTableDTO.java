package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.RdFPathTable} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RdFPathTableDTO implements Serializable {

    private Long id;

    @Size(max = 512)
    private String path;

    @Size(max = 65535)
    private String contentsXslt;

    @Size(max = 256)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContentsXslt() {
        return contentsXslt;
    }

    public void setContentsXslt(String contentsXslt) {
        this.contentsXslt = contentsXslt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RdFPathTableDTO)) {
            return false;
        }

        RdFPathTableDTO rdFPathTableDTO = (RdFPathTableDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rdFPathTableDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RdFPathTableDTO{" +
            "id=" + getId() +
            ", path='" + getPath() + "'" +
            ", contentsXslt='" + getContentsXslt() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
