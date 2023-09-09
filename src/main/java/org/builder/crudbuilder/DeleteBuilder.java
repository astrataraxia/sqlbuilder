package org.builder.crudbuilder;

import jakarta.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A builder class to construct SQL DELETE queries in a fluent manner.
 * This builder provides methods for adding various conditions to the DELETE statement.
 * It also tracks the parameters to be used in the query.
 *
 * @author Kim SeuongYong
 */

public class DeleteBuilder {

    private final StringBuilder query;
    private final List<Object> parameters;
    private boolean tableSpecified;


    /**
     * Private constructor to ensure instantiation through the static factory method.
     */
    private DeleteBuilder() {
        this.query = new StringBuilder();
        this.parameters = new LinkedList<>();
        this.tableSpecified = false;
    }

    /**
     * Creates a new instance of the DeleteBuilder.
     *
     * @return A new DeleteBuilder instance.
     */
    public static DeleteBuilder deleteQuery() {
        return new DeleteBuilder();
    }

    /**
     * Specifies the table from which records will be deleted.
     *
     * @param table The name of the table.
     * @return The current DeleteBuilder instance.
     * @throws IllegalArgumentException If the table name is null or empty.
     */
    public DeleteBuilder deleteFrom(String table) {
        if (!hasText(table)) {
            throw new IllegalArgumentException("Table name cannot be null or empty");
        }
        query.append("DELETE FROM ").append(table);
        tableSpecified = true;
        return this;
    }

    public DeleteBuilder whereEq(String column, Object condition) {
        return addCondition("=", column, condition, "WHERE");
    }

    public DeleteBuilder whereLt(String column, Object condition) {
        return addCondition("<", column, condition, "WHERE");
    }

    public DeleteBuilder whereLte(String column, Object condition) {
        return addCondition("<=", column, condition, "WHERE");
    }

    public DeleteBuilder whereGt(String column, Object condition) {
        return addCondition(">", column, condition ,"WHERE");
    }

    public DeleteBuilder whereGte(String column, Object condition) {
        return addCondition(">=", column, condition, "WHERE");
    }

    public DeleteBuilder whereIn(String column, List<Object> values) {
        return addInCondition(column, values);
    }

    public DeleteBuilder whereIsNull(String column) {
        return addIsNull(column, "IS NULL", "WHERE");
    }

    public DeleteBuilder whereIsNotNull(String column) {
        return addIsNull(column, "IS NOT NULL", "WHERE");
    }

    public DeleteBuilder whereBetween(String column, Object start, Object end) {
        return addBetween(column, start, end);
    }

    public DeleteBuilder whereLike(String column, String pattern) {
        return addCondition("LIKE", column, pattern, "WHERE");
    }

    public DeleteBuilder orEq(String column, Object condition) {
        return addCondition("=", column, condition, "OR");
    }

    public DeleteBuilder orLt(String column, Object condition) {
        return addCondition("<", column, condition, "OR");
    }
    public DeleteBuilder orLte(String column, Object condition) {
        return addCondition("<=", column, condition, "OR");
    }

    public DeleteBuilder orGt(String column, Object condition) {
        return addCondition(">", column, condition, "OR");
    }

    public DeleteBuilder orGte(String column, Object condition) {
        return addCondition(">=", column, condition, "OR");
    }

    public DeleteBuilder orIsNull(String column) {
        return addIsNull(column, "IS NULL", "OR");
    }

    public DeleteBuilder orIsNotNull(String column) {
        return addIsNull(column, "IS NOT NULL", "OR");
    }

    public DeleteBuilder andEq(String column, Object condition) {
        return addCondition("=", column, condition, "AND");
    }

    public DeleteBuilder andLt(String column, Object condition) {
        return addCondition("<", column, condition, "AND");
    }

    public DeleteBuilder andLte(String column, Object condition) {
        return addCondition("<=", column, condition, "AND");
    }

    public DeleteBuilder andGt(String column, Object condition) {
        return addCondition(">", column, condition, "AND");
    }

    public DeleteBuilder andGte(String column, Object condition) {
        return addCondition(">=", column, condition, "AND");
    }

    /**
     * Makes the delete query a string.
     *
     * <p>This method calls the Delete Query statement you created by converting
     * it into a real string. The state of the string depends on the Builder you created.</p>
     *
     * @return A string representation of the DELETE query.
     */
    public String getQuery() {
        return query.toString();
    }


    /**
     * Retrieves the list of parameters associated with the DELETE query.
     *
     * <p>This method provides the caller with the parameters that are set to be used
     * in the DELETE query. These parameters are often values that will replace placeholders
     * (like ?) in the query during execution.</p>
     *
     * @return A list of parameters for the DELETE query.
     */
    public List<Object> getParameters() {
        return parameters;
    }


    /**
     * Adds a condition to the DELETE query.
     *
     * @param operator The SQL operator for the condition (e.g., "=", "<", etc.)
     * @param column The column name.
     * @param condition The condition value.
     * @param conditionOperator The SQL condition operator ("WHERE", "AND", or "OR")
     * @return The current DeleteBuilder instance.
     */
    private DeleteBuilder addCondition(String operator, String column, Object condition, String conditionOperator) {
        if (!hasText(column) || !hasValue(condition)) {
            return this;
        }
        addConditionPrefix(conditionOperator);
        query.append(column).append(" ").append(operator).append(" ?");
        parameters.add(condition);
        return this;
    }

    /**
     * Adds an 'IS NULL, IS NOT NULL'  condition to DELETE query.
     *
     * @param column The column name
     * @param nullOrNotNull The SQL operator for the condition( IS NULL, IS NOT NULL)
     * @param conditionOperator The SQL condition operator ("WHERE", "AND", or "OR")
     * @return The current DeleteBuilder instance.
     */
    private DeleteBuilder addIsNull(String column, String nullOrNotNull, String conditionOperator) {
        if (!hasText(column)) {
            return this;
        }
        addConditionPrefix(conditionOperator);
        query.append(column).append(" ").append(nullOrNotNull);
        return this;
    }

    /**
     * Adds an 'BETWEEN' condition to DELETE query.
     *
     * @param column The column name
     * @param start The start value of condition Between
     * @param end The end value of condition Between
     * @return The current DeleteBuilder instance
     */
    private DeleteBuilder addBetween(String column, Object start, Object end) {
        if (!hasText(column) || !hasValue(start) || !hasValue(end)) {
            return this;
        }
        addConditionPrefix("WHERE");
        query.append(column).append(" BETWEEN ? AND ?");
        parameters.add(start);
        parameters.add(end);
        return this;
    }

    /**
     * Adds an 'IN' condition to the DELETE query.
     *
     * @param column The column name for the IN condition.
     * @param values A list of values for the IN condition.
     * @return The current DeleteBuilder instance.
     */
    private DeleteBuilder addInCondition(String column, List<Object> values) {
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

    /**
     * Appends a condition prefix (e.g., WHERE, AND, OR) to the DELETE query.
     *
     * <p>It checks if the table has been specified before appending the condition prefix.
     * If the table has not been specified, an IllegalStateException is thrown.</p>
     *
     * @param conditionOperator The condition prefix to be added (e.g., WHERE, AND, OR).
     * @throws IllegalStateException If the table has not been previously specified.
     */
    private void addConditionPrefix(String conditionOperator) {
        if (!tableSpecified) {
            throw new IllegalStateException("You should specify the table first using deleteFrom method.");
        }
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
