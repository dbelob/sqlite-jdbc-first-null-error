package acme.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PreparedStatementTest {
    private Connection conn;
    private Statement stat;

    @BeforeEach
    public void connect() throws Exception {
        conn = DriverManager.getConnection("jdbc:sqlite::memory:");
        stat = conn.createStatement();
    }

    @AfterEach
    public void close() throws SQLException {
        stat.close();
        conn.close();
    }

    @Test
    void updateWithoutNulls() throws SQLException {
        stat.executeUpdate("create table if not exists test_table(first_field integer, second_field text)");
        stat.executeUpdate("insert into test_table (first_field, second_field) values (1, 'text 1')");

        try (PreparedStatement ps = conn.prepareStatement("update test_table set first_field = ?, second_field = ?")) {
            // Successfully in any SQLite JDBC version
            ps.setLong(1, 2);
            ps.setString(2, "text 2");

            int count = ps.executeUpdate();

            assertEquals(1, count);
        }
    }

    @Test
    void updateWithFirstNull() throws SQLException {
        stat.executeUpdate("create table if not exists test_table(first_field integer, second_field text)");
        stat.executeUpdate("insert into test_table (first_field, second_field) values (1, 'text 1')");

        try (PreparedStatement ps = conn.prepareStatement("update test_table set first_field = ?, second_field = ?")) {
            // NullPointerException since SQLite JDBC 3.41.2.2
            assertThrows(SQLException.class, () -> ps.getParameterMetaData().getParameterType(1));
        }
    }

    @Test
    void updateWithSecondNull() throws SQLException {
        stat.executeUpdate("create table if not exists test_table(first_field integer, second_field text)");
        stat.executeUpdate("insert into test_table (first_field, second_field) values (1, 'text 1')");

        try (PreparedStatement ps = conn.prepareStatement("update test_table set first_field = ?, second_field = ?")) {
            ps.setLong(1, 2);

            // Successfully in any SQLite JDBC version
            int sqlTypeToUse = ps.getParameterMetaData().getParameterType(2);
            ps.setNull(2, sqlTypeToUse);

            int count = ps.executeUpdate();

            assertEquals(1, count);
        }
    }
}
