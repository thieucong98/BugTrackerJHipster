package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class RdFBatchRegisterSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("workflow_id", table, columnPrefix + "_workflow_id"));
        columns.add(Column.aliased("dbname", table, columnPrefix + "_dbname"));
        columns.add(Column.aliased("feed_id", table, columnPrefix + "_feed_id"));
        columns.add(Column.aliased("func", table, columnPrefix + "_func"));
        columns.add(Column.aliased("req_datetime", table, columnPrefix + "_req_datetime"));
        columns.add(Column.aliased("exec_user", table, columnPrefix + "_exec_user"));
        columns.add(Column.aliased("system_ids", table, columnPrefix + "_system_ids"));
        columns.add(Column.aliased("mode", table, columnPrefix + "_mode"));
        columns.add(Column.aliased("done", table, columnPrefix + "_done"));
        columns.add(Column.aliased("created_timestamp", table, columnPrefix + "_created_timestamp"));
        columns.add(Column.aliased("updated_timestamp", table, columnPrefix + "_updated_timestamp"));

        return columns;
    }
}
