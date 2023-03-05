package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.DemoOrm} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DemoOrmDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 10)
    private String codeId;

    @NotNull(message = "must not be null")
    @Size(max = 128)
    private String codeName;

    @NotNull(message = "must not be null")
    @Size(max = 64)
    private String itemKey;

    @Size(max = 256)
    private String itemValueJa;

    @Size(max = 256)
    private String itemValueEn;

    @Size(max = 255)
    private String itemValuePair;

    @Size(max = 5)
    private String parentCodeId;

    @Size(max = 128)
    private String parentItemKey;

    @Size(max = 45)
    private String parentItemKeyBackup;

    @Size(max = 45)
    private String parentItemKeyNew;

    private Instant createdTimestamp;

    private Instant updatedTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemValueJa() {
        return itemValueJa;
    }

    public void setItemValueJa(String itemValueJa) {
        this.itemValueJa = itemValueJa;
    }

    public String getItemValueEn() {
        return itemValueEn;
    }

    public void setItemValueEn(String itemValueEn) {
        this.itemValueEn = itemValueEn;
    }

    public String getItemValuePair() {
        return itemValuePair;
    }

    public void setItemValuePair(String itemValuePair) {
        this.itemValuePair = itemValuePair;
    }

    public String getParentCodeId() {
        return parentCodeId;
    }

    public void setParentCodeId(String parentCodeId) {
        this.parentCodeId = parentCodeId;
    }

    public String getParentItemKey() {
        return parentItemKey;
    }

    public void setParentItemKey(String parentItemKey) {
        this.parentItemKey = parentItemKey;
    }

    public String getParentItemKeyBackup() {
        return parentItemKeyBackup;
    }

    public void setParentItemKeyBackup(String parentItemKeyBackup) {
        this.parentItemKeyBackup = parentItemKeyBackup;
    }

    public String getParentItemKeyNew() {
        return parentItemKeyNew;
    }

    public void setParentItemKeyNew(String parentItemKeyNew) {
        this.parentItemKeyNew = parentItemKeyNew;
    }

    public Instant getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Instant createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Instant getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(Instant updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemoOrmDTO)) {
            return false;
        }

        DemoOrmDTO demoOrmDTO = (DemoOrmDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, demoOrmDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemoOrmDTO{" +
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
