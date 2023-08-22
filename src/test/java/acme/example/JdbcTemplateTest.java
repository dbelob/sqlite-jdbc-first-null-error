package acme.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class JdbcTemplateTest {
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.sqlite.JDBC");
            dataSource.setUrl("jdbc:sqlite:test.db");

            return dataSource;
        }

        @Bean
        public JdbcTemplate jdbcTemplate() {
            return new JdbcTemplate(dataSource());
        }
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("create table if not exists test_table(first_field integer, second_field text)");
        jdbcTemplate.execute("delete from test_table");
        jdbcTemplate.execute("insert into test_table (first_field, second_field) values (1, 'text 1')");
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("drop table test_table");
    }

    @Test
    void updateWithoutNulls() {
        // Successfully in any SQLite JDBC version
        int count = jdbcTemplate.update("update test_table set first_field = ?, second_field = ?", 2, "text 2");

        assertEquals(1, count);
    }

    @Test
    void updateWithFirstNull() {
        // NullPointerException since SQLite JDBC 3.41.2.2
        int count = jdbcTemplate.update("update test_table set first_field = ?, second_field = ?", null, "text 2");

        assertEquals(1, count);
    }

    @Test
    void updateWithSecondNull() {
        // Successfully in any SQLite JDBC version
        int count = jdbcTemplate.update("update test_table set first_field = ?, second_field = ?", 2, null);

        assertEquals(1, count);
    }
}
