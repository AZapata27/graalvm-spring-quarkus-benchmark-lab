package co.example.resource;

import co.example.model.Cart;
import co.example.repository.CartRepository;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Exercises the full JAX-RS -> service -> Panache repository stack against an in-memory H2 database
 * (see the %test profile in application.properties).
 */
@QuarkusTest
class CartResourceTest {

    @Inject
    CartRepository cartRepository;

    @BeforeEach
    void cleanDatabase() {
        QuarkusTransaction.requiringNew().run(() -> cartRepository.deleteAll());
    }

    private Long persistCart() {
        return QuarkusTransaction.requiringNew().call(() -> {
            Cart cart = new Cart();
            cartRepository.persist(cart);
            return cart.getId();
        });
    }

    @Test
    void getAllReturnsPersistedCarts() {
        Long id = persistCart();

        given()
                .when().get("/carts")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", hasItem(id.intValue()));
    }

    @Test
    void getByIdReturnsCart() {
        Long id = persistCart();

        given()
                .when().get("/carts/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id.intValue()));
    }

    @Test
    void createPersistsCartAndReturns201() {
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().post("/carts")
                .then()
                .statusCode(201);

        assertEquals(1L, QuarkusTransaction.requiringNew().call(() -> cartRepository.count()));
    }

    @Test
    void updateUnknownCartReturns404() {
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().put("/carts/{id}", 999_999)
                .then()
                .statusCode(404);
    }

    @Test
    void updateExistingCartReturns200() {
        Long id = persistCart();

        // "items" must be sent: Cart.items uses orphanRemoval, so merging a null collection is rejected by Hibernate.
        given()
                .contentType(ContentType.JSON)
                .body("{\"items\": []}")
                .when().put("/carts/{id}", id)
                .then()
                .statusCode(200);
    }

    @Test
    void deleteRemovesCartAndReturns204() {
        Long id = persistCart();

        given()
                .when().delete("/carts/{id}", id)
                .then()
                .statusCode(204);

        assertNull(QuarkusTransaction.requiringNew().call(() -> cartRepository.findById(id)));
    }
}
