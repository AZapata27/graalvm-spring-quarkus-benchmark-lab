package co.example.springwebfluxnative.migration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Proves that Flyway runs the versioned scripts in db/migration on startup even though the
 * application only exposes an R2DBC ConnectionFactory (Flyway connects through spring.flyway.url).
 */
@DataR2dbcTest
class FlywayMigrationTest {

    @Autowired
    private Flyway flyway;

    @Autowired
    private DatabaseClient databaseClient;

    @Test
    void versionedMigrationsAreApplied() {
        MigrationInfo[] applied = flyway.info().applied();

        assertThat(Arrays.stream(applied).map(info -> info.getVersion().getVersion()))
                .containsExactly("1", "2");
        assertThat(applied).allSatisfy(info -> assertThat(info.getState().isApplied()).isTrue());
        assertThat(flyway.info().pending()).isEmpty();
    }

    @Test
    void schemaAndSeedDataAreVisibleThroughR2dbc() {
        StepVerifier.create(databaseClient.sql("SELECT COUNT(*) AS total FROM products WHERE name = 'Product 1'")
                        .map(row -> row.get("total", Long.class))
                        .one())
                .expectNext(1L)
                .verifyComplete();
    }
}
