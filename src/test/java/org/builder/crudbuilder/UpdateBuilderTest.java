package org.builder.crudbuilder;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.builder.crudbuilder.UpdateBuilder.updateQuery;

class UpdateBuilderTest {

    @Test
    void allRecordUpdate() {
        UpdateBuilder builder = updateQuery()
                .updateTable("users")
                .setValues(Map.of("is_active", false));

        assertThat(builder.getQuery()).isEqualTo("UPDATE users SET is_active = ?");
        assertThat(builder.getParameters()).containsExactly(false);
    }

    @Test
    void singleColumUpdateTest() {
        UpdateBuilder builder = updateQuery()
                .updateTable("users")
                .setValues(Map.of("age", 15))
                .whereEq("user_id", 1);

        assertThat(builder.getQuery()).isEqualTo("UPDATE users SET age = ? WHERE user_id = ?");
        assertThat(builder.getParameters()).containsExactly(15,1);
    }

    @Test
    void multiColumUpdateTest() {
        Map<String, Object> maps = new LinkedHashMap<>();
        maps.put("age", 30);
        maps.put("name", "kim");
        UpdateBuilder builder = updateQuery()
                .updateTable("users")
                .setValues(maps)
                .whereEq("user_id", 1);

        assertThat(builder.getQuery()).isEqualTo("UPDATE users SET age = ?, name = ? WHERE user_id = ?");
        assertThat(builder.getParameters()).containsExactly(30,"kim",1);
    }

    @Test
    void multiConditionUpdateTest() {
        UpdateBuilder builder = updateQuery()
                .updateTable("users")
                .setValues(Map.of("age", "age + 1"))
                .whereGte("age", 20)
                .andLte("age", 30);

        assertThat(builder.getQuery()).isEqualTo("UPDATE users SET age = ? WHERE age >= ? AND age <= ?");
        assertThat(builder.getParameters()).containsExactly("age + 1", 20, 30);
    }

    @Test
    void joinUpdateTest() {
        UpdateBuilder builder = updateQuery()
                .updateTable("orders o")
                .join("customers c", "o.customer_id", "c.customer_id")
                .setValues(Map.of("o.status", "processed"))
                .whereEq("c.premium_member", true);

        assertThat(builder.getQuery()).isEqualTo("UPDATE orders o JOIN customers c ON o.customer_id = c.customer_id SET o.status = ? WHERE c.premium_member = ?");
        assertThat(builder.getParameters()).containsExactly("processed", true);
    }

    @Test
    void setCaseTest() {
        UpdateBuilder builder = updateQuery()
                .updateTable("members")
                .setCase("member_grade")
                .when("points").gte(1000).then("Platinum")
                .when("points").gte(500).then("Gold")
                .when("points").gte(100).then("Silver")
                .endCase("Bronze");
        assertThat(builder.getQuery()).isEqualTo("UPDATE members SET member_grade = CASE WHEN points >= ? THEN ? WHEN points >= ? THEN ? WHEN points >= ? THEN ? ELSE ? END");
        assertThat(builder.getParameters()).containsExactly(1000, "Platinum", 500, "Gold", 100, "Silver", "Bronze");
    }

    @Test
    void otherSetCaseTest() {
        UpdateBuilder builder = updateQuery()
                .updateTable("products")
                .setCase("price")
                .when("category").eq("electronics").thenColum("price").multiply(0.9)
                .when("category").eq("apparel").thenColum("price").multiply(0.8)
                .endCase("price");
        assertThat(builder.getQuery()).isEqualTo("UPDATE products SET price = CASE WHEN category = ? THEN price * ? WHEN category = ? THEN price * ? ELSE ? END");
        assertThat(builder.getParameters()).containsExactly("electronics", 0.9, "apparel", 0.8, "price");
    }

    @Test
    void testLeftJoin() {
        UpdateBuilder builder = updateQuery().updateTable("users")
                .leftJoin("orders", "users.id", "orders.user_id");
        assertThat(builder.getQuery()).isEqualTo("UPDATE users LEFT JOIN orders ON users.id = orders.user_id");
    }

    @Test
    void testRightJoin() {
        UpdateBuilder builder = updateQuery().updateTable("users")
                .rightJoin("orders", "users.id", "orders.user_id");
        assertThat(builder.getQuery()).isEqualTo("UPDATE users RIGHT JOIN orders ON users.id = orders.user_id");
    }

    @Test
    void testGt() {
        UpdateBuilder builder = updateQuery().updateTable("users")
                .setValues(Map.of("age", "age + 1"))
                .whereGt("age", 25);
        assertThat(builder.getQuery()).isEqualTo("UPDATE users SET age = ? WHERE age > ?");
        assertThat(builder.getParameters()).containsExactly("age + 1",25);
    }

    @Test
    void testLt() {
        UpdateBuilder builder = updateQuery().updateTable("users")
                .setValues(Map.of("age", "age + 1"))
                .whereLt("age", 25);
        assertThat(builder.getQuery()).isEqualTo("UPDATE users SET age = ? WHERE age < ?");
        assertThat(builder.getParameters()).containsExactly("age + 1",25);
    }

    @Test
    void testLte() {
        UpdateBuilder builder = updateQuery().updateTable("users")
                .setValues(Map.of("age", "age + 1"))
                .whereLte("age", 25);
        assertThat(builder.getQuery()).isEqualTo("UPDATE users SET age = ? WHERE age <= ?");
        assertThat(builder.getParameters()).containsExactly("age + 1",25);
    }

    @Test
    void testWhereIn() {
        UpdateBuilder builder = updateQuery().updateTable("users")
                .setValues(Map.of("age", "age + 1"))
                .whereIn("age", Arrays.asList(25, 26, 27));
        assertThat(builder.getQuery()).isEqualTo("UPDATE users SET age = ? WHERE age IN (?, ?, ?)");
        assertThat(builder.getParameters()).containsExactly("age + 1", 25, 26, 27);
    }

    @Test
    void testWhereNotIn() {
        UpdateBuilder builder = updateQuery().updateTable("users")
                .setValues(Map.of("age", "age + 1"))
                .whereNotIn("age", Arrays.asList(25, 26, 27));
        assertThat(builder.getQuery()).isEqualTo("UPDATE users SET age = ? WHERE age NOT IN (?, ?, ?)");
        assertThat(builder.getParameters()).containsExactly("age + 1", 25, 26, 27);
    }

    @Test
    void testWhereIsNull() {
        UpdateBuilder builder = updateQuery().updateTable("users")
                .setValues(Map.of("age", "age + 1"))
                .whereIsNull("age");
        assertThat(builder.getQuery()).isEqualTo("UPDATE users SET age = ? WHERE age IS NULL");
        assertThat(builder.getParameters()).containsExactly("age + 1");
    }

    @Test
    void testWhereIsNotNull() {
        UpdateBuilder builder = updateQuery().updateTable("users")
                .setValues(Map.of("age", "age + 1"))
                .whereIsNotNull("age");
        assertThat(builder.getQuery()).isEqualTo("UPDATE users SET age = ? WHERE age IS NOT NULL");
        assertThat(builder.getParameters()).containsExactly("age + 1");
    }

    @Test
    void testWhereLike() {
        UpdateBuilder builder = updateQuery().updateTable("users")
                .setValues(Map.of("age", "age + 1"))
                .whereLike("name", "John%");
        assertThat(builder.getQuery()).isEqualTo("UPDATE users SET age = ? WHERE name LIKE ?");
        assertThat(builder.getParameters()).containsExactly("age + 1", "John%");
    }

    @Test
    void testWhereBetween() {
        UpdateBuilder builder = updateQuery().updateTable("users")
                .setValues(Map.of("age", "age + 1"))
                .whereBetween("age", 25, 30);
        assertThat(builder.getQuery()).isEqualTo("UPDATE users SET age = ? WHERE age BETWEEN ? AND ?");
        assertThat(builder.getParameters()).containsExactly("age + 1", 25, 30);
    }

}