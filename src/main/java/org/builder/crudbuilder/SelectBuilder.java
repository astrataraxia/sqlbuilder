package org.builder.crudbuilder;

import jakarta.annotation.Nullable;

public class SelectBuilder {

    private final StringBuilder query;

    public SelectBuilder() {
        this.query = new StringBuilder();
    }

    public SelectBuilder select(String column) {
        query.append("SELECT ").append(column);
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

    public SelectBuilder or(String condition) {
        if (hasText(condition)) {
            query.append(" OR ").append(condition);
        }
        return this;
    }

    public SelectBuilder and(String condition) {
        if (hasText(condition)) {
            query.append(" AND ").append(condition);
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

    public SelectBuilder orderBy(String column, Object orderType) {
        query.append(" ORDER BY ").append(column).append(" ").append(orderType);
        return this;
    }

    public String build() {
        return query.toString();
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
