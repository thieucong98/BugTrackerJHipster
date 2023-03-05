package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A RdFBatchRegister.
 */
@Table("rd_f_batch_register")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "rdfbatchregister")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RdFBatchRegister implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 65535)
    @Column("workflow_id")
    private String workflowId;

    /**
     * 論文DB名
     */
    @NotNull(message = "must not be null")
    @Size(max = 45)
    @Column("dbname")
    private String dbname;

    /**
     * 論文ID
     */
    @NotNull(message = "must not be null")
    @Size(max = 45)
    @Column("feed_id")
    private String feedId;

    /**
     * 処理内容
     */
    @NotNull(message = "must not be null")
    @Size(max = 20)
    @Column("func")
    private String func;

    /**
     * 要求日時
     */
    @NotNull(message = "must not be null")
    @Size(max = 14)
    @Column("req_datetime")
    private String reqDatetime;

    /**
     * 実行者のシステムID
     */
    @NotNull(message = "must not be null")
    @Size(max = 45)
    @Column("exec_user")
    private String execUser;

    /**
     * チェックした著者候補のシステムIDの羅列「;」区切り
     */
    @Column("system_ids")
    private String systemIds;

    /**
     * 登録時の書き込みモード
     */
    @NotNull(message = "must not be null")
    @Size(max = 20)
    @Column("mode")
    private String mode;

    /**
     * 実行フラグ
     */
    @NotNull(message = "must not be null")
    @Size(max = 1)
    @Column("done")
    private String done;

    @NotNull(message = "must not be null")
    @Column("created_timestamp")
    private Instant createdTimestamp;

    @NotNull(message = "must not be null")
    @Column("updated_timestamp")
    private Instant updatedTimestamp;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RdFBatchRegister id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkflowId() {
        return this.workflowId;
    }

    public RdFBatchRegister workflowId(String workflowId) {
        this.setWorkflowId(workflowId);
        return this;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getDbname() {
        return this.dbname;
    }

    public RdFBatchRegister dbname(String dbname) {
        this.setDbname(dbname);
        return this;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getFeedId() {
        return this.feedId;
    }

    public RdFBatchRegister feedId(String feedId) {
        this.setFeedId(feedId);
        return this;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getFunc() {
        return this.func;
    }

    public RdFBatchRegister func(String func) {
        this.setFunc(func);
        return this;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getReqDatetime() {
        return this.reqDatetime;
    }

    public RdFBatchRegister reqDatetime(String reqDatetime) {
        this.setReqDatetime(reqDatetime);
        return this;
    }

    public void setReqDatetime(String reqDatetime) {
        this.reqDatetime = reqDatetime;
    }

    public String getExecUser() {
        return this.execUser;
    }

    public RdFBatchRegister execUser(String execUser) {
        this.setExecUser(execUser);
        return this;
    }

    public void setExecUser(String execUser) {
        this.execUser = execUser;
    }

    public String getSystemIds() {
        return this.systemIds;
    }

    public RdFBatchRegister systemIds(String systemIds) {
        this.setSystemIds(systemIds);
        return this;
    }

    public void setSystemIds(String systemIds) {
        this.systemIds = systemIds;
    }

    public String getMode() {
        return this.mode;
    }

    public RdFBatchRegister mode(String mode) {
        this.setMode(mode);
        return this;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDone() {
        return this.done;
    }

    public RdFBatchRegister done(String done) {
        this.setDone(done);
        return this;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public Instant getCreatedTimestamp() {
        return this.createdTimestamp;
    }

    public RdFBatchRegister createdTimestamp(Instant createdTimestamp) {
        this.setCreatedTimestamp(createdTimestamp);
        return this;
    }

    public void setCreatedTimestamp(Instant createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Instant getUpdatedTimestamp() {
        return this.updatedTimestamp;
    }

    public RdFBatchRegister updatedTimestamp(Instant updatedTimestamp) {
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
        if (!(o instanceof RdFBatchRegister)) {
            return false;
        }
        return id != null && id.equals(((RdFBatchRegister) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RdFBatchRegister{" +
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
