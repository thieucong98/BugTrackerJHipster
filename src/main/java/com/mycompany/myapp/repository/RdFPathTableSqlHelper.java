package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class RdFPathTableSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("path", table, columnPrefix + "_path"));
        columns.add(Column.aliased("contents_xslt", table, columnPrefix + "_contents_xslt"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));

        return columns;
    }
}
