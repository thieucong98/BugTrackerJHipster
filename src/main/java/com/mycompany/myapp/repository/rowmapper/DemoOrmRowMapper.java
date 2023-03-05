package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.DemoOrm;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DemoOrm}, with proper type conversions.
 */
@Service
public class DemoOrmRowMapper implements BiFunction<Row, String, DemoOrm> {

    private final ColumnConverter converter;

    public DemoOrmRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DemoOrm} stored in the database.
     */
    @Override
    public DemoOrm apply(Row row, String prefix) {
        DemoOrm entity = new DemoOrm();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCodeId(converter.fromRow(row, prefix + "_code_id", String.class));
        entity.setCodeName(converter.fromRow(row, prefix + "_code_name", String.class));
        entity.setItemKey(converter.fromRow(row, prefix + "_item_key", String.class));
        entity.setItemValueJa(converter.fromRow(row, prefix + "_item_value_ja", String.class));
        entity.setItemValueEn(converter.fromRow(row, prefix + "_item_value_en", String.class));
        entity.setItemValuePair(converter.fromRow(row, prefix + "_item_value_pair", String.class));
        entity.setParentCodeId(converter.fromRow(row, prefix + "_parent_code_id", String.class));
        entity.setParentItemKey(converter.fromRow(row, prefix + "_parent_item_key", String.class));
        entity.setParentItemKeyBackup(converter.fromRow(row, prefix + "_parent_item_key_backup", String.class));
        entity.setParentItemKeyNew(converter.fromRow(row, prefix + "_parent_item_key_new", String.class));
        entity.setCreatedTimestamp(converter.fromRow(row, prefix + "_created_timestamp", Instant.class));
        entity.setUpdatedTimestamp(converter.fromRow(row, prefix + "_updated_timestamp", Instant.class));
        return entity;
    }
}
