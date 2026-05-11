package pl.wolnikradoslaw.birdwatchbackend;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

public class DockerTest {

    @Test
    void containerStarts() {
        try (PostgreSQLContainer<?> pg = new PostgreSQLContainer<>(
                DockerImageName.parse("postgis/postgis:16-3.4-alpine")
                        .asCompatibleSubstituteFor("postgres"))) {
            pg.start();
            assertThat(pg.isRunning()).isTrue();
        } // try-with-resources automatically calls pg.close() / stops the container
    }
}