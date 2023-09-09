package org.builder.crudbuilder;

import jakarta.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UpdateBuilder {

    private final StringBuilder query;
    private final List<Object> parameters;

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
            throw new IllegalArgumentException("Row data can not be null or empty");
        }
        query.append("UPDATE ").append(table).append(" SET ");
        return this;
    }

    public UpdateBuilder setValues(Map<String,Object> columnAndValues) {
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
        return null;
    }

    private UpdateBuilder addInCondition(String column, List<Object> values) {
        return null;
    }

    private UpdateBuilder addIsNull(String column, String isNull, String where) {
        return null;
    }

    private UpdateBuilder addBetween(String column, Object start, Object end) {
        return null;
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
}
