package acme.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class SqliteDataConfig {
    private static final String DRIVER_CLASS_NAME = "org.sqlite.JDBC";
    private static final String JDBC_URL = "jdbc:sqlite::memory:";

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setUrl(JDBC_URL);

        return dataSource;
    }
}
