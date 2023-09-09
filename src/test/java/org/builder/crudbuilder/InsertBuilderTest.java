package org.builder.crudbuilder;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.builder.crudbuilder.InsertBuilder.insert;

class InsertBuilderTest {

    @Test
    void testInsertColumnAndValuesMap() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("name", "jim");
        values.put("age", 1);
        values.put("gender", "man");
        InsertBuilder builder = insert().columnsAndValues("users", values);

        String query = builder.getQuery();
        List<Object> parameters = builder.getParameters();
        assertThat(query).isEqualTo("INSERT INTO users (name, age, gender) VALUES (?, ?, ?)");
        assertThat(parameters.get(0)).isEqualTo("jim");
        assertThat(parameters.get(1)).isEqualTo(1);
        assertThat(parameters.get(2)).isEqualTo("man");
    }

    @Test
    public void testInsertMultiColumnValues() {
        List<Map<String, Object>> rows = Arrays.asList(
                createLinkedMap("name", "Alice", "age", 25, "gender", "female"),
                createLinkedMap("name", "Bob", "age", 30, "gender", "male"),
                createLinkedMap("name", "Bob", "age", 20, "gender", "male"),
                createLinkedMap("name", "jim", "age", 15, "gender", "male")
        );

        InsertBuilder builder = insert().columnsAndMultiValues("users", rows);

        String query = builder.getQuery();
        List<Object> parameters = builder.getParameters();

        assertThat(query).isEqualTo("INSERT INTO users (name, age, gender) VALUES (?, ?, ?), (?, ?, ?), (?, ?, ?), (?, ?, ?)");
        assertThat(parameters).containsExactly("Alice", 25, "female",
                "Bob", 30, "male",
                "Bob", 20, "male", "jim", 15, "male");
    }

    @Test
    public void testInsertDirectValues() {
        InsertBuilder builder = insert().values("users", Arrays.asList("Charlie", 35, "male"));

        String query = builder.getQuery();
        List<Object> parameters = builder.getParameters();

        assertThat(query).isEqualTo("INSERT INTO users VALUES (?, ?, ?)");
        assertThat(parameters.get(0)).isEqualTo("Charlie");
        assertThat(parameters.get(1)).isEqualTo(35);
        assertThat(parameters.get(2)).isEqualTo("male");
    }

    @Test
    public void testMultiValuesInsert() {
        List<List<Object>> rows = Arrays.asList(
                Arrays.asList("Alice", 25, "female"),
                Arrays.asList("Bob", 30, "male"),
                Arrays.asList("Charlie", 35, "male")
        );

        InsertBuilder builder = insert()
                .multiValues("users", rows);

        String query = builder.getQuery();
        List<Object> parameters = builder.getParameters();

        assertThat(query).isEqualTo("INSERT INTO users VALUES (?, ?, ?), (?, ?, ?), (?, ?, ?)");
        assertThat(parameters).containsExactly("Alice", 25, "female", "Bob", 30, "male", "Charlie", 35, "male");
    }

    private Map<String, Object> createLinkedMap(Object... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid number of key/value pairs");
        }
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            map.put((String) keyValues[i], keyValues[i + 1]);
        }
        return map;
    }
}