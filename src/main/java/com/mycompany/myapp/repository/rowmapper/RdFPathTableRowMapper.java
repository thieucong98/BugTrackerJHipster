package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.RdFPathTable;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link RdFPathTable}, with proper type conversions.
 */
@Service
public class RdFPathTableRowMapper implements BiFunction<Row, String, RdFPathTable> {

    private final ColumnConverter converter;

    public RdFPathTableRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link RdFPathTable} stored in the database.
     */
    @Override
    public RdFPathTable apply(Row row, String prefix) {
        RdFPathTable entity = new RdFPathTable();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPath(converter.fromRow(row, prefix + "_path", String.class));
        entity.setContentsXslt(converter.fromRow(row, prefix + "_contents_xslt", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        return entity;
    }
}
