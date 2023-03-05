package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DemoOrm.
 */
@Table("demo_orm")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "demoorm")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DemoOrm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 10)
    @Column("code_id")
    private String codeId;

    @NotNull(message = "must not be null")
    @Size(max = 128)
    @Column("code_name")
    private String codeName;

    @NotNull(message = "must not be null")
    @Size(max = 64)
    @Column("item_key")
    private String itemKey;

    @Size(max = 256)
    @Column("item_value_ja")
    private String itemValueJa;

    @Size(max = 256)
    @Column("item_value_en")
    private String itemValueEn;

    @Size(max = 255)
    @Column("item_value_pair")
    private String itemValuePair;

    @Size(max = 5)
    @Column("parent_code_id")
    private String parentCodeId;

    @Size(max = 128)
    @Column("parent_item_key")
    private String parentItemKey;

    @Size(max = 45)
    @Column("parent_item_key_backup")
    private String parentItemKeyBackup;

    @Size(max = 45)
    @Column("parent_item_key_new")
    private String parentItemKeyNew;

    @Column("created_timestamp")
    private Instant createdTimestamp;

    @Column("updated_timestamp")
    private Instant updatedTimestamp;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DemoOrm id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeId() {
        return this.codeId;
    }

    public DemoOrm codeId(String codeId) {
        this.setCodeId(codeId);
        return this;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCodeName() {
        return this.codeName;
    }

    public DemoOrm codeName(String codeName) {
        this.setCodeName(codeName);
        return this;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getItemKey() {
        return this.itemKey;
    }

    public DemoOrm itemKey(String itemKey) {
        this.setItemKey(itemKey);
        return this;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemValueJa() {
        return this.itemValueJa;
    }

    public DemoOrm itemValueJa(String itemValueJa) {
        this.setItemValueJa(itemValueJa);
        return this;
    }

    public void setItemValueJa(String itemValueJa) {
        this.itemValueJa = itemValueJa;
    }

    public String getItemValueEn() {
        return this.itemValueEn;
    }

    public DemoOrm itemValueEn(String itemValueEn) {
        this.setItemValueEn(itemValueEn);
        return this;
    }

    public void setItemValueEn(String itemValueEn) {
        this.itemValueEn = itemValueEn;
    }

    public String getItemValuePair() {
        return this.itemValuePair;
    }

    public DemoOrm itemValuePair(String itemValuePair) {
        this.setItemValuePair(itemValuePair);
        return this;
    }

    public void setItemValuePair(String itemValuePair) {
        this.itemValuePair = itemValuePair;
    }

    public String getParentCodeId() {
        return this.parentCodeId;
    }

    public DemoOrm parentCodeId(String parentCodeId) {
        this.setParentCodeId(parentCodeId);
        return this;
    }

    public void setParentCodeId(String parentCodeId) {
        this.parentCodeId = parentCodeId;
    }

    public String getParentItemKey() {
        return this.parentItemKey;
    }

    public DemoOrm parentItemKey(String parentItemKey) {
        this.setParentItemKey(parentItemKey);
        return this;
    }

    public void setParentItemKey(String parentItemKey) {
        this.parentItemKey = parentItemKey;
    }

    public String getParentItemKeyBackup() {
        return this.parentItemKeyBackup;
    }

    public DemoOrm parentItemKeyBackup(String parentItemKeyBackup) {
        this.setParentItemKeyBackup(parentItemKeyBackup);
        return this;
    }

    public void setParentItemKeyBackup(String parentItemKeyBackup) {
        this.parentItemKeyBackup = parentItemKeyBackup;
    }

    public String getParentItemKeyNew() {
        return this.parentItemKeyNew;
    }

    public DemoOrm parentItemKeyNew(String parentItemKeyNew) {
        this.setParentItemKeyNew(parentItemKeyNew);
        return this;
    }

    public void setParentItemKeyNew(String parentItemKeyNew) {
        this.parentItemKeyNew = parentItemKeyNew;
    }

    public Instant getCreatedTimestamp() {
        return this.createdTimestamp;
    }

    public DemoOrm createdTimestamp(Instant createdTimestamp) {
        this.setCreatedTimestamp(createdTimestamp);
        return this;
    }

    public void setCreatedTimestamp(Instant createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Instant getUpdatedTimestamp() {
        return this.updatedTimestamp;
    }

    public DemoOrm updatedTimestamp(Instant updatedTimestamp) {
        this.setUpdatedTimestamp(updatedTimestamp);
        return this;
    }

    public void setUpdatedTimestamp(Instant updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemoOrm)) {
            return false;
        }
        return id != null && id.equals(((DemoOrm) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemoOrm{" +
            "id=" + getId() +
            ", codeId='" + getCodeId() + "'" +
            ", codeName='" + getCodeName() + "'" +
            ", itemKey='" + getItemKey() + "'" +
            ", itemValueJa='" + getItemValueJa() + "'" +
            ", itemValueEn='" + getItemValueEn() + "'" +
            ", itemValuePair='" + getItemValuePair() + "'" +
            ", parentCodeId='" + getParentCodeId() + "'" +
            ", parentItemKey='" + getParentItemKey() + "'" +
            ", parentItemKeyBackup='" + getParentItemKeyBackup() + "'" +
            ", parentItemKeyNew='" + getParentItemKeyNew() + "'" +
            ", createdTimestamp='" + getCreatedTimestamp() + "'" +
            ", updatedTimestamp='" + getUpdatedTimestamp() + "'" +
            "}";
    }
}
