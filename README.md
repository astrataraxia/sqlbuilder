## QueryBuilder

Please use it if you need it. However, be aware that there may be errors or insufficient functions

I was making a native query in the project, and I wanted to use it as Java code than Mybatis, 
and I made the query sentence more easy to see, so I made it like this.

## Introduce
QueryBuilder is a class of utilities designed to help Java developers organize SQL queries in a chain and legible manner rather than in pure strings.
This Builder reduces the likelihood of errors when configuring direct queries and provides a more structured way to create and correct Query statements.

## Table of Contents
- [How to Use](#how-to-use)
    - [DeleteBuilder](#deletebuilder)
    - [InsertBuilder](#insertbuilder)
    - [UpdateBuilder](#updatebuilder)
    - [SelectBuilder](#selectbuilder)

## How to Use it?
I will explain, but it may not be enough. In that case, 
I experimented with various queries on the test code, so take a look. 
There may be a way to create the query you want.

Common parameter 
- `getQuery()` : The role of transforming the builder into a string
- `getParameters()` : List that stores parameters used in Query.

The use of the rest of the builders is written in the examples below or in the test.

### DeleteBuilder

```java
// Query =>  DELETE FROM tables WHERE id = ?;
// Parameters - id
DeleteBuilder builder = deleteQuery()
        .deleteFrom(tables)
        .whereEq("id", id);

jdbcTemplate.update(builder.getQuery(), builder.getParameters());

// Query => DELETE FROM users where name = ? AND age > ? OR city = ?
// parameters - "John", 30, "New York"
DeleteBuilder builder = deleteQuery()
            .deleteFrom("users")
            .whereEq("name", "John")
            .andGt("age", 30)
            .orEq("city", "New York");

// If you don't have any parameters, you can pull it out with a string right away
String query = deleteQuery()
        .deleteFrom("users")
        .getQuery();
```

### InsertBuilder

It supports four types of Insert.
1. columns and vales 
2. columns and multiValues 
3. INSERT INTO values (...);
4. mltiValues Insert
 
```java

//"INSERT INTO users (name, age, gender) VALUES (?, ?, ?)";
InsertBuilder builder = insert().columnsAndValues(tableName, 
        Map.of("column", value,"column", value));

//"INSERT INTO users (name, age, gender) VALUES (?, ?, ?), (?, ?, ?), (?, ?, ?)";
InsertBuilder builder = insert().columnsAndMultiValues(tableName, List.of(
        Map.of(column, value, column, value, column, value),
        Map.of(column, value, column, value, column, value),
        Map.of(column, value, column, value, column, value)));

// INSERT INTO users VALUES(value , vaule, value);
InsertBuilder builder = insert().values("users", Arrays.asList("Charlie", 35, "male"));

```
multiValues is similar values ..

### UpdateBuilder

```java

// UPDATE users SET age = ? WHERE age <= ?
UpdateBuilder builder = updateQuery().updateTable("users")
        .setValues(Map.of("age", "age + 1"))
        .whereLte("age", 25);

// UPDATE products SET price = CASE WHEN category = ? THEN price * ? WHEN category = ? THEN price * ? ELSE ? END
UpdateBuilder builder = updateQuery()
        .updateTable("products")
        .setCase("price")
        .when("category").eq("electronics").thenColum("price").multiply(0.9)
        .when("category").eq("apparel").thenColum("price").multiply(0.8)
        .endCase("price");
```

### SelectBuilder

```java
String query = selectBuilder()
        .select(List.of("u.username", "o.order_id", "p.product_name"))
        .from("users u")
        .join("orders o").on("u.user_id", "o.user_id")
        .join("products p").on("o.product_id", "p.product_id")
        .getQuery();

"SELECT (u.username, o.order_id, p.product_name) FROM users u " +
"JOIN orders o ON u.user_id = o.user_id JOIN products p ON o.product_id = p.product_id");
```
