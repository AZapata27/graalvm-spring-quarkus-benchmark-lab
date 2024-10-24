package co.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

//@QuarkusTest
class CartLoadTest {

    //@Test
    void testWithWarmup() throws Exception {
        // Warmup
        System.out.println("Iniciando warmup...");
        for (int i = 0; i < 50; i++) {
            given()
                    .when()
                    .get("/carts")
                    .then()
                    .statusCode(200);
        }
        System.out.println("Warmup completado");

        // Test real
        performLoadTest(1000, 50);
    }

    private void performLoadTest(int numRequests, int concurrency) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
        List<Future<TestResult>> futures = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numRequests; i++) {
            futures.add(executor.submit(() -> {
                long requestStart = System.currentTimeMillis();

                Response response = given()
                        .when()
                        .get("/carts")
                        .then()
                        .extract()
                        .response();

                return new TestResult(
                        System.currentTimeMillis() - requestStart,
                        response.getStatusCode()
                );
            }));
        }

        List<TestResult> results = new ArrayList<>();
        for (Future<TestResult> future : futures) {
            results.add(future.get());
        }

        long totalTime = System.currentTimeMillis() - startTime;

        // Análisis de resultados
        printStatistics(results, totalTime, numRequests);

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    private void printStatistics(List<TestResult> results, long totalTime, int numRequests) {
        DoubleSummaryStatistics stats = results.stream()
                .mapToDouble(TestResult::getResponseTime)
                .summaryStatistics();

        long successfulRequests = results.stream()
                .filter(r -> r.getStatusCode() == 200)
                .count();

        System.out.println("\nResultados del test de carga:");
        System.out.println("Total requests: " + numRequests);
        System.out.println("Tiempo total: " + totalTime + "ms");
        System.out.println("Requests exitosos: " + successfulRequests);
        System.out.println("Tiempo promedio: " + stats.getAverage() + "ms");
        System.out.println("Tiempo máximo: " + stats.getMax() + "ms");
        System.out.println("Tiempo mínimo: " + stats.getMin() + "ms");
        System.out.println("Requests por segundo: " + (numRequests * 1000.0 / totalTime));
    }

    private static class TestResult {
        private final long responseTime;
        private final int statusCode;

        public TestResult(long responseTime, int statusCode) {
            this.responseTime = responseTime;
            this.statusCode = statusCode;
        }

        public long getResponseTime() {
            return responseTime;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }
}