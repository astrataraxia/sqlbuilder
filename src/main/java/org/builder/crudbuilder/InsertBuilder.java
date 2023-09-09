package org.builder.crudbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InsertBuilder {

    private final StringBuilder query;
    private final List<Object> parameters;

    private InsertBuilder() {
        this.query = new StringBuilder();
        this.parameters = new LinkedList<>();
    }

    public static InsertBuilder insert() {
        return new InsertBuilder();
    }

    public InsertBuilder columnsAndValues(String table, Map<String, Object> columnValueMap) {
        appendInsertIntoTable(table);
        String columns = columnsExtraction(columnValueMap);
        parameters.addAll(columnValueMap.values());
        query.append(" (").append(columns).append(") VALUES (")
                .append(generateValueHolders(columnValueMap.size())).append(")");
        return this;
    }

    public InsertBuilder columnsAndMultiValues(String table, List<Map<String, Object>> rows) {
        if (rows == null || rows.isEmpty()) {
            throw new IllegalArgumentException("Row data cannot be null or empty");
        }
        appendInsertIntoTable(table);
        String columns = columnsExtraction(rows.get(0));
        query.append(" (").append(columns).append(") VALUES ");
        appendMultiValues(rows.stream()
                .map(map -> new ArrayList<>(map.values()))
                .collect(Collectors.toList()));
        return this;
    }

    public InsertBuilder values(String table, List<Object> values) {
        appendInsertIntoTable(table);
        parameters.addAll(values);
        query.append(" VALUES (").append(generateValueHolders(values.size())).append(")");
        return this;
    }

    public InsertBuilder multiValues(String table, List<List<Object>> valuesLists) {
        if (valuesLists == null || valuesLists.isEmpty()) {
            throw new IllegalArgumentException("Values cannot be null or empty");
        }
        appendInsertIntoTable(table);
        query.append(" VALUES ");
        appendMultiValues(valuesLists);
        return this;
    }

    public String getQuery() {
        return query.toString();
    }

    public List<Object> getParameters() {
        return parameters;
    }

    private void appendInsertIntoTable(String table) {
        query.append("INSERT INTO ").append(table);
    }

    private String columnsExtraction(Map<String, Object> map) {
        return String.join(", ", map.keySet());
    }

    private void appendMultiValues(List<List<Object>> multiValues) {
        query.append(multiValues.stream()
                .map(values -> {
                    parameters.addAll(values);
                    return "(" + generateValueHolders(values.size()) + ")";
                })
                .collect(Collectors.joining(", ")));
    }

    private String generateValueHolders(int count) {
        return String.join(", ", Collections.nCopies(count, "?"));
    }
}
