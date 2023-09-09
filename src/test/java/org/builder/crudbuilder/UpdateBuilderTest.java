package org.builder.crudbuilder;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.builder.crudbuilder.UpdateBuilder.*;

class UpdateBuilderTest {

    @Test
    void singleColumnUpdateTest() {
        UpdateBuilder builder = updateQuery()
                .updateTable("users")
                .setValues(Map.of("age", 1, "name", "kim"))
                .whereEq("user_id", 1);

        assertThat(builder.getQuery()).isEqualTo("UPDATE users SET age = ?, name = ? WHERE user_id = ?");
        assertThat(builder.getParameters()).containsExactly(30,"kim",1);
    }
}