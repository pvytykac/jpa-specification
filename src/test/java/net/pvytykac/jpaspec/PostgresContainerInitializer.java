package net.pvytykac.jpaspec;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class PostgresContainerInitializer implements ApplicationContextInitializer<@NonNull ConfigurableApplicationContext> {

    @Container
    PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18");

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        postgres.start();

        TestPropertyValues.of(
                "spring.datasource.url=" + postgres.getJdbcUrl(),
                "spring.datasource.username=" + postgres.getUsername(),
                "spring.datasource.password=" + postgres.getPassword()
        ).applyTo(context.getEnvironment());
    }
}
