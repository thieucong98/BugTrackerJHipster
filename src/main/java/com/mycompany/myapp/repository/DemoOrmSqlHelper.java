package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class DemoOrmSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("code_id", table, columnPrefix + "_code_id"));
        columns.add(Column.aliased("code_name", table, columnPrefix + "_code_name"));
        columns.add(Column.aliased("item_key", table, columnPrefix + "_item_key"));
        columns.add(Column.aliased("item_value_ja", table, columnPrefix + "_item_value_ja"));
        columns.add(Column.aliased("item_value_en", table, columnPrefix + "_item_value_en"));
        columns.add(Column.aliased("item_value_pair", table, columnPrefix + "_item_value_pair"));
        columns.add(Column.aliased("parent_code_id", table, columnPrefix + "_parent_code_id"));
        columns.add(Column.aliased("parent_item_key", table, columnPrefix + "_parent_item_key"));
        columns.add(Column.aliased("parent_item_key_backup", table, columnPrefix + "_parent_item_key_backup"));
        columns.add(Column.aliased("parent_item_key_new", table, columnPrefix + "_parent_item_key_new"));
        columns.add(Column.aliased("created_timestamp", table, columnPrefix + "_created_timestamp"));
        columns.add(Column.aliased("updated_timestamp", table, columnPrefix + "_updated_timestamp"));

        return columns;
    }
}
