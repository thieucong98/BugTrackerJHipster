package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.RdFBatchRegister;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link RdFBatchRegister}, with proper type conversions.
 */
@Service
public class RdFBatchRegisterRowMapper implements BiFunction<Row, String, RdFBatchRegister> {

    private final ColumnConverter converter;

    public RdFBatchRegisterRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link RdFBatchRegister} stored in the database.
     */
    @Override
    public RdFBatchRegister apply(Row row, String prefix) {
        RdFBatchRegister entity = new RdFBatchRegister();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setWorkflowId(converter.fromRow(row, prefix + "_workflow_id", String.class));
        entity.setDbname(converter.fromRow(row, prefix + "_dbname", String.class));
        entity.setFeedId(converter.fromRow(row, prefix + "_feed_id", String.class));
        entity.setFunc(converter.fromRow(row, prefix + "_func", String.class));
        entity.setReqDatetime(converter.fromRow(row, prefix + "_req_datetime", String.class));
        entity.setExecUser(converter.fromRow(row, prefix + "_exec_user", String.class));
        entity.setSystemIds(converter.fromRow(row, prefix + "_system_ids", String.class));
        entity.setMode(converter.fromRow(row, prefix + "_mode", String.class));
        entity.setDone(converter.fromRow(row, prefix + "_done", String.class));
        entity.setCreatedTimestamp(converter.fromRow(row, prefix + "_created_timestamp", Instant.class));
        entity.setUpdatedTimestamp(converter.fromRow(row, prefix + "_updated_timestamp", Instant.class));
        return entity;
    }
}
