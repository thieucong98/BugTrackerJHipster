package com.mycompany.myapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.RdFBatchRegister} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RdFBatchRegisterDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 65535)
    private String workflowId;

    /**
     * 論文DB名
     */
    @NotNull(message = "must not be null")
    @Size(max = 45)
    @Schema(description = "論文DB名", required = true)
    private String dbname;

    /**
     * 論文ID
     */
    @NotNull(message = "must not be null")
    @Size(max = 45)
    @Schema(description = "論文ID", required = true)
    private String feedId;

    /**
     * 処理内容
     */
    @NotNull(message = "must not be null")
    @Size(max = 20)
    @Schema(description = "処理内容", required = true)
    private String func;

    /**
     * 要求日時
     */
    @NotNull(message = "must not be null")
    @Size(max = 14)
    @Schema(description = "要求日時", required = true)
    private String reqDatetime;

    /**
     * 実行者のシステムID
     */
    @NotNull(message = "must not be null")
    @Size(max = 45)
    @Schema(description = "実行者のシステムID", required = true)
    private String execUser;

    /**
     * チェックした著者候補のシステムIDの羅列「;」区切り
     */

    @Schema(description = "チェックした著者候補のシステムIDの羅列「;」区切り", required = true)
    @Lob
    private String systemIds;

    /**
     * 登録時の書き込みモード
     */
    @NotNull(message = "must not be null")
    @Size(max = 20)
    @Schema(description = "登録時の書き込みモード", required = true)
    private String mode;

    /**
     * 実行フラグ
     */
    @NotNull(message = "must not be null")
    @Size(max = 1)
    @Schema(description = "実行フラグ", required = true)
    private String done;

    @NotNull(message = "must not be null")
    private Instant createdTimestamp;

    @NotNull(message = "must not be null")
    private Instant updatedTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getReqDatetime() {
        return reqDatetime;
    }

    public void setReqDatetime(String reqDatetime) {
        this.reqDatetime = reqDatetime;
    }

    public String getExecUser() {
        return execUser;
    }

    public void setExecUser(String execUser) {
        this.execUser = execUser;
    }

    public String getSystemIds() {
        return systemIds;
    }

    public void setSystemIds(String systemIds) {
        this.systemIds = systemIds;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
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
        if (!(o instanceof RdFBatchRegisterDTO)) {
            return false;
        }

        RdFBatchRegisterDTO rdFBatchRegisterDTO = (RdFBatchRegisterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rdFBatchRegisterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RdFBatchRegisterDTO{" +
            "id=" + getId() +
            ", workflowId='" + getWorkflowId() + "'" +
            ", dbname='" + getDbname() + "'" +
            ", feedId='" + getFeedId() + "'" +
            ", func='" + getFunc() + "'" +
            ", reqDatetime='" + getReqDatetime() + "'" +
            ", execUser='" + getExecUser() + "'" +
            ", systemIds='" + getSystemIds() + "'" +
            ", mode='" + getMode() + "'" +
            ", done='" + getDone() + "'" +
            ", createdTimestamp='" + getCreatedTimestamp() + "'" +
            ", updatedTimestamp='" + getUpdatedTimestamp() + "'" +
            "}";
    }
}
