package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A RdFPathTable.
 */
@Table("rd_f_path_table")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "rdfpathtable")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RdFPathTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Size(max = 512)
    @Column("path")
    private String path;

    @Size(max = 65535)
    @Column("contents_xslt")
    private String contentsXslt;

    @Size(max = 256)
    @Column("description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RdFPathTable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return this.path;
    }

    public RdFPathTable path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContentsXslt() {
        return this.contentsXslt;
    }

    public RdFPathTable contentsXslt(String contentsXslt) {
        this.setContentsXslt(contentsXslt);
        return this;
    }

    public void setContentsXslt(String contentsXslt) {
        this.contentsXslt = contentsXslt;
    }

    public String getDescription() {
        return this.description;
    }

    public RdFPathTable description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RdFPathTable)) {
            return false;
        }
        return id != null && id.equals(((RdFPathTable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RdFPathTable{" +
            "id=" + getId() +
            ", path='" + getPath() + "'" +
            ", contentsXslt='" + getContentsXslt() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
