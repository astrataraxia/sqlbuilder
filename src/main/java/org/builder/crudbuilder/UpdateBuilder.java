package org.builder.crudbuilder;

import jakarta.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UpdateBuilder {

    private final StringBuilder query;
    private final List<Object> parameters;
    private boolean isInsideCase;

    private UpdateBuilder() {
        this.query = new StringBuilder();
        this.parameters = new LinkedList<>();
    }

    public static UpdateBuilder updateQuery() {
        return new UpdateBuilder();
    }

    /**
     * Specifies the table from which records will be update.
     *
     * @param table The name of the table.
     * @return The current UpdateBuilder instance.
     * @throws IllegalArgumentException If the table name is null or empty.
     */

    public UpdateBuilder updateTable(String table) {
        if (!hasText(table)) {
            throw new IllegalArgumentException("Table can not be null or empty");
        }
        query.append("UPDATE ").append(table);
        return this;
    }

    public UpdateBuilder join(String table, String onColumn, String equalToColumn) {
        return addJoin("JOIN", table, onColumn, equalToColumn);
    }

    public UpdateBuilder leftJoin(String table, String onColumn, String equalToColumn) {
        return addJoin("LEFT JOIN", table, onColumn, equalToColumn);
    }

    public UpdateBuilder rightJoin(String table, String onColumn, String equalToColumn) {
        return addJoin("RIGHT JOIN", table, onColumn, equalToColumn);
    }

    public UpdateBuilder setValues(Map<String,Object> columnAndValues) {
        if (columnAndValues == null || columnAndValues.isEmpty()) {
            throw new IllegalArgumentException("Row data can not be null or empty");
        }
        String setString = columnAndValues.entrySet().stream()
                .map(entry -> {
                    parameters.add(entry.getValue());
                    return entry.getKey() + " = ?";
                })
                .collect(Collectors.joining(", "));
        query.append(" SET ").append(setString);
        return this;
    }

    public UpdateBuilder setCase(String column) {
        if (column == null || column.trim().isEmpty()) {
            throw new IllegalArgumentException("Column data cannot be null or empty");
        }
        query.append(" SET ").append(column).append(" = CASE");
        isInsideCase = true;
        return this;
    }

    public UpdateBuilder when(String column) {
        if (!isInsideCase) {
            throw new IllegalStateException("Cannot call 'when()' outside of a CASE statement.");
        }
        query.append(" WHEN ").append(column);
        return this;
    }

    public UpdateBuilder then(Object value) {
        if (!isInsideCase) {
            throw new IllegalStateException("Cannot call 'then()' outside of a CASE statement.");
        }
        query.append(" THEN ?");
        parameters.add(value);
        return this;
    }

    public UpdateBuilder thenColum(String column) {
        if (!isInsideCase) {
            throw new IllegalStateException("Cannot call 'then()' outside of a CASE statement.");
        }
        query.append(" THEN ").append(column);
        return this;
    }

    public UpdateBuilder eq(Object values) {
        query.append(" = ?");
        parameters.add(values);
        return this;
    }

    public UpdateBuilder gt(Object values) {
        query.append(" > ?");
        parameters.add(values);
        return this;
    }

    public UpdateBuilder gte(Object values) {
        query.append(" >= ?");
        parameters.add(values);
        return this;
    }
    public UpdateBuilder lt(Object values) {
        query.append(" < ?");
        parameters.add(values);
        return this;
    }
    public UpdateBuilder lte(String values) {
        query.append(" <= ?");
        parameters.add(values);
        return this;
    }

    public UpdateBuilder multiply(Object values) {
        query.append(" * ?");
        parameters.add(values);
        return this;
    }

    public UpdateBuilder add(Object values) {
        query.append(" + ?");
        parameters.add(values);
        return this;
    }

    public UpdateBuilder odd(Object values) {
        query.append(" - ?");
        parameters.add(values);
        return this;
    }

    public UpdateBuilder endCase(String value) {
        if (!isInsideCase) {
            throw new IllegalStateException("Cannot set default value outside of a CASE statement.");
        }
        query.append(" ELSE ? END");
        parameters.add(value);
        isInsideCase = false;
        return this;
    }

    public UpdateBuilder whereEq(String column, Object condition) {
        return addCondition("=", column, condition, "WHERE");
    }

    public UpdateBuilder whereLt(String column, Object condition) {
        return addCondition("<", column, condition, "WHERE");
    }

    public UpdateBuilder whereLte(String column, Object condition) {
        return addCondition("<=", column, condition, "WHERE");
    }

    public UpdateBuilder whereGt(String column, Object condition) {
        return addCondition(">", column, condition ,"WHERE");
    }

    public UpdateBuilder whereGte(String column, Object condition) {
        return addCondition(">=", column, condition, "WHERE");
    }

    public UpdateBuilder whereIn(String column, List<Object> values) {
        return addInCondition(column, values);
    }

    public UpdateBuilder whereNotIn(String column, List<Object> values) {
        return addNotInCondition(column, values);
    }

    public UpdateBuilder whereIsNull(String column) {
        return addIsNull(column, "IS NULL", "WHERE");
    }

    public UpdateBuilder whereIsNotNull(String column) {
        return addIsNull(column, "IS NOT NULL", "WHERE");
    }

    public UpdateBuilder whereBetween(String column, Object start, Object end) {
        return addBetween(column, start, end);
    }

    public UpdateBuilder whereLike(String column, String pattern) {
        return addCondition("LIKE", column, pattern, "WHERE");
    }

    public UpdateBuilder orEq(String column, Object condition) {
        return addCondition("=", column, condition, "OR");
    }

    public UpdateBuilder orLt(String column, Object condition) {
        return addCondition("<", column, condition, "OR");
    }
    public UpdateBuilder orLte(String column, Object condition) {
        return addCondition("<=", column, condition, "OR");
    }

    public UpdateBuilder orGt(String column, Object condition) {
        return addCondition(">", column, condition, "OR");
    }

    public UpdateBuilder orGte(String column, Object condition) {
        return addCondition(">=", column, condition, "OR");
    }

    public UpdateBuilder orIsNull(String column) {
        return addIsNull(column, "IS NULL", "OR");
    }

    public UpdateBuilder orIsNotNull(String column) {
        return addIsNull(column, "IS NOT NULL", "OR");
    }

    public UpdateBuilder andEq(String column, Object condition) {
        return addCondition("=", column, condition, "AND");
    }

    public UpdateBuilder andLt(String column, Object condition) {
        return addCondition("<", column, condition, "AND");
    }

    public UpdateBuilder andLte(String column, Object condition) {
        return addCondition("<=", column, condition, "AND");
    }

    public UpdateBuilder andGt(String column, Object condition) {
        return addCondition(">", column, condition, "AND");
    }

    public UpdateBuilder andGte(String column, Object condition) {
        return addCondition(">=", column, condition, "AND");
    }

    public String getQuery() {
        return query.toString();
    }

    public List<Object> getParameters() {
        return parameters;
    }


    private UpdateBuilder addCondition(String operator, String column, Object condition, String conditionOperator) {
        if (!hasText(column) || !hasValue(condition)) {
            return this;
        }
        addConditionPrefix(conditionOperator);
        query.append(column).append(" ").append(operator).append(" ?");
        parameters.add(condition);
        return this;
    }

    private UpdateBuilder addJoin(String joinType, String table, String onColumn, String equalToColumn) {
        if (!hasText(table) || !hasText(onColumn) || !hasText(equalToColumn)) {
            throw new IllegalArgumentException("Join parameters cannot be null or empty");
        }
        query.append(" ").append(joinType).append(" ").append(table)
                .append(" ON ").append(onColumn).append(" = ").append(equalToColumn);
        return this;
    }

    private UpdateBuilder addInCondition(String column, List<Object> values) {
        if (!hasText(column) || values == null || values.isEmpty()) {
            return this;
        }
        addConditionPrefix("WHERE");
        query.append(column).append(" IN (")
                .append(values.stream()
                        .map(value -> {
                            parameters.add(value);
                            return "?";
                        })
                        .collect(Collectors.joining(", ")))
                .append(")");
        return this;
    }

    private UpdateBuilder addNotInCondition(String column, List<Object> values) {
        if (!hasText(column) || values == null || values.isEmpty()) {
            return this;
        }
        addConditionPrefix("WHERE");
        query.append(column).append(" NOT IN (")
                .append(values.stream()
                        .map(value -> {
                            parameters.add(value);
                            return "?";
                        })
                        .collect(Collectors.joining(", ")))
                .append(")");
        return this;
    }

    private UpdateBuilder addIsNull(String column, String nullOrNotNull, String conditionOperator) {
        if (!hasText(column)) {
            return this;
        }
        addConditionPrefix(conditionOperator);
        query.append(column).append(" ").append(nullOrNotNull);
        return this;
    }

    private UpdateBuilder addBetween(String column, Object start, Object end) {
        if (!hasText(column) || !hasValue(start) || !hasValue(end)) {
            return this;
        }
        addConditionPrefix("WHERE");
        query.append(column).append(" BETWEEN ? AND ?");
        parameters.add(start);
        parameters.add(end);
        return this;
    }

    private void addConditionPrefix(String conditionOperator) {
        query.append(" ").append(conditionOperator).append(" ");
    }

    private boolean hasText(@Nullable CharSequence str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasValue(Object condition) {
        return condition != null && (!(condition instanceof String) || hasText((String) condition));
    }
}
