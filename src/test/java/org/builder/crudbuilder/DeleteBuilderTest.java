package org.builder.crudbuilder;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.builder.crudbuilder.DeleteBuilder.deleteQuery;

class DeleteBuilderTest {

    @Test
    void testBasicDelete() {
        String result = deleteQuery()
                .deleteFrom("users")
                .getQuery();

        assertThat(result).isEqualTo("DELETE FROM users");
    }

    @Test
    void deleteWhereEqConditionTest() {
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("users")
                .whereEq("id", 1);
        String query = builder.getQuery();
        List<Object> parameters = builder.getParameters();

        assertThat(query).isEqualTo("DELETE FROM users WHERE id = ?");
        assertThat(parameters.get(0)).isEqualTo(1);
    }

    @Test
    void testDeleteWithMultipleConditions() {
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("users")
                .whereEq("name", "John")
                .andGt("age", 30)
                .orEq("city", "New York");

        String query = builder.getQuery();
        List<Object> parameters = builder.getParameters();

        assertThat(query).isEqualTo("DELETE FROM users WHERE name = ? AND age > ? OR city = ?");
        assertThat(parameters.get(0)).isEqualTo("John");
        assertThat(parameters.get(1)).isEqualTo(30);
        assertThat(parameters.get(2)).isEqualTo("New York");
    }

    @Test
    void testWhereLteDelete(){
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("users")
                .whereLte("age", 30);
        String query = builder.getQuery();
        List<Object> parameters = builder.getParameters();

        assertThat(query).isEqualTo("DELETE FROM users WHERE age <= ?");
        assertThat(parameters).containsExactly(30);
    }

    @Test
    void testWhereGtDelete(){
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("users")
                .whereGt("age", 30);
        String query = builder.getQuery();
        List<Object> parameters = builder.getParameters();

        assertThat(query).isEqualTo("DELETE FROM users WHERE age > ?");
        assertThat(parameters).containsExactly(30);
    }

    @Test
    void testWhereGteDelete(){
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("users")
                .whereGte("age", 30);

        String query = builder.getQuery();
        List<Object> parameters = builder.getParameters();

        assertThat(query).isEqualTo("DELETE FROM users WHERE age >= ?");
        assertThat(parameters).containsExactly(30);
    }

    @Test
    void testDeleteWithEmptyWhereCondition() {
        String result = deleteQuery()
                .deleteFrom("users")
                .whereEq("id",null)
                .getQuery();

        assertThat(result).isEqualTo("DELETE FROM users");
    }

    @Test
    void testDeleteWithWhitespaceWhereCondition() {
        String result = deleteQuery()
                .deleteFrom("users")
                .whereEq("     ", null)
                .getQuery();

        assertThat(result).isEqualTo("DELETE FROM users");
    }

    @Test
    void testDeleteWithNullWhereCondition() {
        String result = deleteQuery()
                .deleteFrom("users")
                .whereEq(null, null)
                .getQuery();

        assertThat(result).isEqualTo("DELETE FROM users");
    }

    @Test
    void testDeleteWithChainedAndConditions() {
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("products")
                .whereEq("category", "Electronics")
                .andLt("price", 1000)
                .andEq("brand", "Apple");

        String query = builder.getQuery();
        List<Object> parameters = builder.getParameters();

        assertThat(query).isEqualTo("DELETE FROM products WHERE category = ? AND price < ? AND brand = ?");
        assertThat(parameters.get(0)).isEqualTo("Electronics");
        assertThat(parameters.get(1)).isEqualTo(1000);
        assertThat(parameters.get(2)).isEqualTo("Apple");
    }

    @Test
    void testDeleteWithChainedOrConditions() {
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("products")
                .whereEq("category", "Electronics")
                .orEq("category", "Home Appliances")
                .orEq("category", "Toys");

        String query = builder.getQuery();
        List<Object> parameters = builder.getParameters();

        assertThat(query).isEqualTo("DELETE FROM products WHERE category = ? OR category = ? OR category = ?");
        assertThat(parameters.get(0)).isEqualTo("Electronics");
        assertThat(parameters.get(1)).isEqualTo("Home Appliances");
        assertThat(parameters.get(2)).isEqualTo("Toys");
    }

    @Test
    void testDeleteManyCaseConditions() {
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("users")
                .whereEq("username", "john")
                .andGt("age", 25)
                .orEq("isAdmin", true);

        assertThat(builder.getQuery()).isEqualTo("DELETE FROM users WHERE username = ? AND age > ? OR isAdmin = ?");
        assertThat(builder.getParameters()).containsExactly("john", 25, true);
    }

    @Test
    void testDeleteWithoutTable() {
        assertThatThrownBy(() -> deleteQuery().deleteFrom(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Table name cannot be null or empty");
    }
    @Test
    void testDeleteWithOrLtCondition() {
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("products")
                .whereEq("category", "electronics")
                .orLt("price", 1000);

        assertThat(builder.getQuery()).isEqualTo("DELETE FROM products WHERE category = ? OR price < ?");
        assertThat(builder.getParameters()).containsExactly("electronics", 1000);
    }

    @Test
    void testDeleteWithOrLteAndOrGteConditions() {
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("products")
                .whereEq("category", "electronics")
                .orLte("stock", 10)
                .orGte("rating", 4.5);

        String expectedQuery = "DELETE FROM products WHERE category = ? OR stock <= ? OR rating >= ?";
        assertThat(builder.getQuery()).isEqualTo(expectedQuery);
        assertThat(builder.getParameters()).containsExactly("electronics", 10, 4.5);
    }

    @Test
    void testDeleteWithWhereLtAndAndGteConditions() {
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("employees")
                .whereLt("age", 30)
                .andGte("salary", 50000);

        String expectedQuery = "DELETE FROM employees WHERE age < ? AND salary >= ?";
        assertThat(builder.getQuery()).isEqualTo(expectedQuery);
        assertThat(builder.getParameters()).containsExactly(30, 50000);
    }

    @Test
    void testDeleteWithMultipleAndOrConditions() {
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("books")
                .whereEq("genre", "fiction")
                .andLte("pages", 400)
                .orGte("price", 20);

        String expectedQuery = "DELETE FROM books WHERE genre = ? AND pages <= ? OR price >= ?";
        assertThat(builder.getQuery()).isEqualTo(expectedQuery);
        assertThat(builder.getParameters()).containsExactly("fiction", 400, 20);
    }
    @Test
    void testDeleteWithWhereLteCondition() {
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("products")
                .whereEq("category", "electronics")
                .andLte("price", 2000);

        assertThat(builder.getQuery()).isEqualTo("DELETE FROM products WHERE category = ? AND price <= ?");
        assertThat(builder.getParameters()).containsExactly("electronics", 2000);
    }

    @Test
    void testDeleteWithWhereGtCondition() {
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("products")
                .whereEq("category", "electronics")
                .andGt("price", 1000);

        assertThat(builder.getQuery()).isEqualTo("DELETE FROM products WHERE category = ? AND price > ?");
        assertThat(builder.getParameters()).containsExactly("electronics", 1000);
    }

    @Test
    void testDeleteWithWhereGteCondition() {
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("products")
                .whereEq("category", "electronics")
                .andGte("price", 1500);

        assertThat(builder.getQuery()).isEqualTo("DELETE FROM products WHERE category = ? AND price >= ?");
        assertThat(builder.getParameters()).containsExactly("electronics", 1500);
    }

    @Test
    void testDeleteWithOrGtCondition() {
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("products")
                .whereEq("category", "electronics")
                .orGt("price", 2500);

        String query = builder.getQuery();
        List<Object> parameters = builder.getParameters();

        assertThat(query).isEqualTo("DELETE FROM products WHERE category = ? OR price > ?");
        assertThat(parameters).containsExactly("electronics", 2500);
    }

    @Test
    void testDeleteIsNull() {
        String query = deleteQuery()
                .deleteFrom("user")
                .whereIsNull("address")
                .getQuery();

        assertThat(query).isEqualTo("DELETE FROM user WHERE address IS NULL");
    }

    @Test
    void testMultipleIsNull() {
        String query = deleteQuery()
                .deleteFrom("user")
                .whereIsNull("address")
                .orIsNull("name")
                .orIsNull("age")
                .getQuery();

        assertThat(query).isEqualTo("DELETE FROM user WHERE address IS NULL OR name IS NULL OR age IS NULL");
    }

    @Test
    void testMultipleIsNullNotNull() {
        String query = deleteQuery()
                .deleteFrom("user")
                .whereIsNull("address")
                .orIsNull("name")
                .orIsNotNull("age")
                .getQuery();

        assertThat(query).isEqualTo("DELETE FROM user WHERE address IS NULL OR name IS NULL OR age IS NOT NULL");
    }

    @Test
    void testBetween() {
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("user")
                .whereBetween("age", 10, 25);

        String query = builder.getQuery();
        List<Object> parameters = builder.getParameters();
        assertThat(query).isEqualTo("DELETE FROM user WHERE age BETWEEN ? AND ?");
        assertThat(parameters).containsExactly(10, 25);
    }

    @Test
    void testLike(){
        DeleteBuilder builder = deleteQuery()
                .deleteFrom("user")
                .whereLike("name", "김%");

        String query = builder.getQuery();
        List<Object> parameters = builder.getParameters();

        assertThat(query).isEqualTo("DELETE FROM user WHERE name LIKE ?");
        assertThat(parameters).containsExactly("김%");
    }

    @Test
    void testIsNotNull(){
        String query = deleteQuery()
                .deleteFrom("user")
                .whereIsNotNull("age")
                .getQuery();

        assertThat(query).isEqualTo("DELETE FROM user WHERE age IS NOT NULL");
    }

    @Test
    void testInDelete() {
        DeleteBuilder builder = deleteQuery().
                deleteFrom("users")
                .whereIn("age", List.of(15, 20, 25));

        String query = builder.getQuery();
        List<Object> parameters = builder.getParameters();

        assertThat(query).isEqualTo("DELETE FROM users WHERE age IN (?, ?, ?)");
        assertThat(parameters).containsExactly(15, 20, 25);
    }

}
