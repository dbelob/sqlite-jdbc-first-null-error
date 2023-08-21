package acme.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private final ApplicationContextProvider applicationContextProvider;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public Application(ApplicationContextProvider applicationContextProvider, JdbcTemplate jdbcTemplate) {
        this.applicationContextProvider = applicationContextProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    void updateWithFirstNotNull() {
        jdbcTemplate.update("update test_table set first_field = ?, second_field = ?",
                1, "Hello");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        ApplicationContext context = applicationContextProvider.getApplicationContext();
        Application application = context.getBean(Application.class);

        application.updateWithFirstNotNull();
    }
}
