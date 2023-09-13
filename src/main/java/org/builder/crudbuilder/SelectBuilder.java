package org.builder.crudbuilder;

import jakarta.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SelectBuilder {

    private final StringBuilder query;
    private final List<Object> parameters;

    private SelectBuilder() {
        this.query = new StringBuilder();
        this.parameters = new LinkedList<>();
    }

    public static SelectBuilder selectBuilder() {
        return new SelectBuilder();
    }

    public SelectBuilder select(List<String> columns) {
        String join = String.join(", ", columns);
        query.append("SELECT ").append("(").append(join).append(")");
        return this;
    }

    public SelectBuilder from(String table) {
        query.append(" FROM ").append(table);
        return this;
    }

    public SelectBuilder selectFrom(String table) {
        query.append("SELECT * FROM ").append(table);
        return this;
    }

    public SelectBuilder where(String condition) {
        if (hasText(condition)) {
            query.append(" WHERE ").append(condition);
        }
        return this;
    }

    public SelectBuilder or(String column) {
        if (hasText(column)) {
            query.append(" OR ").append(column);
        }
        return this;
    }

    public SelectBuilder and(String column) {
        if (hasText(column)) {
            query.append(" AND ").append(column);
        }
        return this;
    }

    public SelectBuilder join(String table) {
        query.append(" JOIN ").append(table);
        return this;
    }

    public SelectBuilder leftJoin(String table) {
        query.append(" LEFT JOIN ").append(table);
        return this;
    }

    public SelectBuilder on(String leftColum, String rightColum) {
        query.append(" ON ").append(leftColum).append(" = ").append(rightColum);
        return this;
    }

    public SelectBuilder orderBy(String column, OrderType orderType) {
        query.append(" ORDER BY ").append(column).append(" ").append(orderType);
        return this;
    }

    public SelectBuilder whereEq(String column, Object condition) {
        return addCondition("=", column, condition, "WHERE");
    }

    public SelectBuilder whereGt(String column, Object condition) {
        return addCondition(">", column, condition, "WHERE");
    }

    public SelectBuilder whereGte(String column, Object condition) {
        return addCondition(">=", column, condition, "WHERE");
    }

    public SelectBuilder whereLt(String column, Object condition) {
        return addCondition("<", column, condition, "WHERE");
    }

    public SelectBuilder whereLte(String column, Object condition) {
        return addCondition("<=", column, condition, "WHERE");
    }

    public SelectBuilder whereIn(String column, List<Object> values) {
        return addInCondition(column, values);
    }

    public SelectBuilder whereNotIn(String column, List<Object> values) {
        return addNotInCondition(column, values);
    }

    public SelectBuilder whereIsNull(String column) {
        return addIsNull(column, "IS NULL", "WHERE");
    }

    public SelectBuilder whereIsNotNull(String column) {
        return addIsNull(column, "IS NOT NULL", "WHERE");
    }

    public SelectBuilder whereBetween(String column, Object start, Object end) {
        return addBetween(column, start, end);
    }

    public SelectBuilder whereLike(String column, String pattern) {
        return addCondition("LIKE", column, pattern, "WHERE");
    }

    public SelectBuilder orEq(String column, Object condition) {
        return addCondition("=", column, condition, "OR");
    }

    public SelectBuilder orGt(String column, Object condition) {
        return addCondition(">", column, condition, "OR");
    }

    public SelectBuilder orGte(String column, Object condition) {
        return addCondition(">=", column, condition, "OR");
    }

    public SelectBuilder orLt(String column, Object condition) {
        return addCondition("<", column, condition, "OR");
    }

    public SelectBuilder orLte(String column, Object condition) {
        return addCondition("<=", column, condition, "OR");
    }

    public SelectBuilder orIsNull(String column) {
        return addIsNull(column, "IS NULL", "OR");
    }

    public SelectBuilder orIsNotNull(String column) {
        return addIsNull(column, "IS NOT NULL", "OR");
    }

    public SelectBuilder orLike(String column, String pattern) {
        return addCondition("LIKE", column, pattern, "OR");
    }

    public SelectBuilder andEq(String column, Object condition) {
        return addCondition("=", column, condition, "AND");
    }

    public SelectBuilder andGt(String column, Object condition) {
        return addCondition(">", column, condition, "AND");
    }

    public SelectBuilder andGte(String column, Object condition) {
        return addCondition(">=", column, condition, "AND");
    }

    public SelectBuilder andLt(String column, Object condition) {
        return addCondition("<", column, condition, "AND");
    }

    public SelectBuilder andLte(String column, Object condition) {
        return addCondition("<=", column, condition, "AND");
    }

    public SelectBuilder andIsNull(String column) {
        return addIsNull(column, "IS NULL", "AND");
    }

    public SelectBuilder andIsNotNull(String column) {
        return addIsNull(column, "IS NOT NULL", "AND");
    }

    public SelectBuilder andLike(String column, String pattern) {
        return addCondition("LIKE", column, pattern, "AND");
    }

    public SelectBuilder eq(Object values) {
        query.append(" = ?");
        parameters.add(values);
        return this;
    }

    public SelectBuilder gt(Object values) {
        query.append(" > ?");
        parameters.add(values);
        return this;
    }

    public SelectBuilder gte(Object values) {
        query.append(" >= ?");
        parameters.add(values);
        return this;
    }
    public SelectBuilder lt(Object values) {
        query.append(" < ?");
        parameters.add(values);
        return this;
    }
    public SelectBuilder lte(Object values) {
        query.append(" <= ?");
        parameters.add(values);
        return this;
    }

    public String getQuery() {
        return query.toString();
    }

    public List<Object> getParameters() {
        return parameters;
    }

    private SelectBuilder addCondition(String operator, String column, Object condition, String conditionOperator) {
        if (!hasText(column) || !hasValue(condition)) {
            return this;
        }
        addConditionPrefix(conditionOperator);
        query.append(column).append(" ").append(operator).append(" ?");
        parameters.add(condition);
        return this;
    }

    private SelectBuilder addBetween(String column, Object start, Object end) {
        if (!hasText(column) || !hasValue(start) || !hasValue(end)) {
            return this;
        }
        addConditionPrefix("WHERE");
        query.append(column).append(" BETWEEN ? AND ?");
        parameters.add(start);
        parameters.add(end);
        return this;
    }

    private SelectBuilder addIsNull(String column, String nullOrNotNull, String conditionOperator) {
        if (!hasText(column)) {
            return this;
        }
        addConditionPrefix(conditionOperator);
        query.append(column).append(" ").append(nullOrNotNull);
        return this;
    }

    private SelectBuilder addInCondition(String column, List<Object> values) {
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

    private SelectBuilder addNotInCondition(String column, List<Object> values) {
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
