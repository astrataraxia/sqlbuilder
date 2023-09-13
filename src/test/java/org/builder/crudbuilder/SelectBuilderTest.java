package org.builder.crudbuilder;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.builder.crudbuilder.SelectBuilder.selectBuilder;

class SelectBuilderTest {

    @Test
    void singleSelectQueryTest() {
        String query = selectBuilder()
                .select(List.of("email", "age", "gender"))
                .from("users")
                .getQuery();
        assertThat(query).isEqualTo("SELECT (email, age, gender) FROM users");
    }

    @Test
    void selectFrom() {
        String query = selectBuilder().selectFrom("users").getQuery();
        assertThat(query).isEqualTo("SELECT * FROM users");
    }

    @Test
    void selectWhereConditionEqTest() {
        SelectBuilder builder = selectBuilder()
                .select(List.of("email", "age", "gender"))
                .from("users")
                .where("id").eq(1);
        assertThat(builder.getQuery()).isEqualTo("SELECT (email, age, gender) FROM users WHERE id = ?");
        assertThat(builder.getParameters()).containsExactly(1);
    }

    @Test
    void selectWhereConditionGtTest() {
        SelectBuilder builder = selectBuilder()
                .select(List.of("email", "age", "gender"))
                .from("users")
                .where("id").gt(1);
        assertThat(builder.getQuery()).isEqualTo("SELECT (email, age, gender) FROM users WHERE id > ?");
        assertThat(builder.getParameters()).containsExactly(1);
    }

    @Test
    void selectWhereConditionGteTest() {
        SelectBuilder builder = selectBuilder()
                .select(List.of("email", "age", "gender"))
                .from("users")
                .where("id").gte(1);
        assertThat(builder.getQuery()).isEqualTo("SELECT (email, age, gender) FROM users WHERE id >= ?");
        assertThat(builder.getParameters()).containsExactly(1);
    }

    @Test
    void selectWhereConditionLt() {
        SelectBuilder builder = selectBuilder()
                .selectFrom("users")
                .where("id").lt(4);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE id < ?");
        assertThat(builder.getParameters()).containsExactly(4);
    }

    @Test
    void selectWhereConditionLte() {
        SelectBuilder builder = selectBuilder()
                .selectFrom("users")
                .where("id").lte(4);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE id <= ?");
        assertThat(builder.getParameters()).containsExactly(4);
    }

    @Test
    void selectWhereEqTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("id", 1);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE id = ?");
        assertThat(builder.getParameters()).containsExactly(1);
    }

    @Test
    void selectWhereGtTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereGt("id", 1);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE id > ?");
        assertThat(builder.getParameters()).containsExactly(1);
    }

    @Test
    void selectWhereGteTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereGte("id", 1);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE id >= ?");
        assertThat(builder.getParameters()).containsExactly(1);
    }

    @Test
    void selectWhereLtTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereLt("id", 1);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE id < ?");
        assertThat(builder.getParameters()).containsExactly(1);
    }

    @Test
    void selectWhereLteTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereLte("id", 1);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE id <= ?");
        assertThat(builder.getParameters()).containsExactly(1);
    }

    @Test
    void selectWhereInTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereIn("id", List.of(1,2,3,4,5));
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE id IN (?, ?, ?, ?, ?)");
        assertThat(builder.getParameters()).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    void selectWhereNotInTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereNotIn("id", List.of(1,2,3,4,5));
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE id NOT IN (?, ?, ?, ?, ?)");
        assertThat(builder.getParameters()).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    void selectWhereIsNullTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereIsNull("name");
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE name IS NULL");
    }

    @Test
    void selectWhereIsNotNullTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereIsNotNull("name");
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE name IS NOT NULL");
    }

    @Test
    void selectWhereBetweenTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereBetween("age",15, 35);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age BETWEEN ? AND ?");
        assertThat(builder.getParameters()).containsExactly(15, 35);
    }

    @Test
    void selectWhereLikeTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereLike("name", "김%");
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE name LIKE ?");
        assertThat(builder.getParameters()).containsExactly("김%");
    }

    @Test
    void selectOrConditionEqTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .or("age").eq(45);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? OR age = ?");
        assertThat(builder.getParameters()).containsExactly(15, 45);
    }

    @Test
    void selectAndConditionEqTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .and("age").eq(35);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? AND age = ?");
        assertThat(builder.getParameters()).containsExactly(15, 35);
    }

    @Test
    void selectOrEqTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .orEq("age", 35);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? OR age = ?");
        assertThat(builder.getParameters()).containsExactly(15, 35);
    }

    @Test
    void selectOrGtTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .orGt("age", 35);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? OR age > ?");
        assertThat(builder.getParameters()).containsExactly(15, 35);
    }

    @Test
    void selectOrGteTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .orGte("age", 35);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? OR age >= ?");
        assertThat(builder.getParameters()).containsExactly(15, 35);
    }

    @Test
    void selectorOrLtTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .orLt("age", 35);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? OR age < ?");
        assertThat(builder.getParameters()).containsExactly(15, 35);
    }

    @Test
    void selectorOrLte() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .orLte("age", 35);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? OR age <= ?");
        assertThat(builder.getParameters()).containsExactly(15, 35);
    }

    @Test
    void selectorOrIsNull() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .orIsNull("age");
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? OR age IS NULL");
        assertThat(builder.getParameters()).containsExactly(15);
    }

    @Test
    void selectorOrIsNotNull() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .orIsNotNull("age");
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? OR age IS NOT NULL");
        assertThat(builder.getParameters()).containsExactly(15);
    }

    @Test
    void selectorOrLike() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .orLike("name", "김%");
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? OR name LIKE ?");
        assertThat(builder.getParameters()).containsExactly(15, "김%");
    }

    @Test
    void selectAndEqTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .andEq("age", 35);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? AND age = ?");
        assertThat(builder.getParameters()).containsExactly(15, 35);
    }

    @Test
    void selectAndGtTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .andGt("age", 35);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? AND age > ?");
        assertThat(builder.getParameters()).containsExactly(15, 35);
    }

    @Test
    void selectAndGteTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .andGte("age", 35);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? AND age >= ?");
        assertThat(builder.getParameters()).containsExactly(15, 35);
    }

    @Test
    void selectAndLtTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .andLt("age", 35);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? AND age < ?");
        assertThat(builder.getParameters()).containsExactly(15, 35);
    }

    @Test
    void selectAndLteTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .andLte("age", 35);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? AND age <= ?");
        assertThat(builder.getParameters()).containsExactly(15, 35);
    }

    @Test
    void selectAndIsNullTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .andIsNull("age");
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? AND age IS NULL");
        assertThat(builder.getParameters()).containsExactly(15);
    }

    @Test
    void selectAndIsNotNullTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .andIsNotNull("age");
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? AND age IS NOT NULL");
        assertThat(builder.getParameters()).containsExactly(15);
    }

    @Test
    void selectOrderByTest() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .orderBy("age", OrderType.DESC);
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? ORDER BY age DESC");
        assertThat(builder.getParameters()).containsExactly(15);
    }

    @Test
    void selectAndLike() {
        SelectBuilder builder = selectBuilder().selectFrom("users")
                .whereEq("age", 15)
                .andLike("name", "김%");
        assertThat(builder.getQuery()).isEqualTo("SELECT * FROM users WHERE age = ? AND name LIKE ?");
        assertThat(builder.getParameters()).containsExactly(15, "김%");
    }

    @Test
    void selectJoinTest() {
        SelectBuilder builder = selectBuilder()
                .select(List.of("u.username", "o.order_id", "p.product_name"))
                .from("users u")
                .join("orders o").on("u.user_id", "o.user_id")
                .join("products p").on("o.product_id", "p.product_id");

        assertThat(builder.getQuery()).isEqualTo("SELECT (u.username, o.order_id, p.product_name) FROM users u JOIN orders o ON u.user_id = o.user_id JOIN products p ON o.product_id = p.product_id");
    }

    @Test
    void selectLefJoinTest() {
        SelectBuilder builder = selectBuilder()
                .select(List.of("u.username", "o.order_id", "p.product_name"))
                .from("users u")
                .leftJoin("orders o").on("u.user_id", "o.user_id");

        assertThat(builder.getQuery()).isEqualTo("SELECT (u.username, o.order_id, p.product_name) FROM users u LEFT JOIN orders o ON u.user_id = o.user_id");
    }
}