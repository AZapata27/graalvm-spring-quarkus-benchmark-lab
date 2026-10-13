package co.example.resource;

import co.example.model.Cart;
import co.example.model.CartItem;
import co.example.model.Product;
import co.example.repository.CartRepository;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
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
        QuarkusTransaction.requiringNew().run(() -> {
            cartRepository.getEntityManager().createQuery("delete from CartItem").executeUpdate();
            cartRepository.deleteAll();
            cartRepository.getEntityManager().createQuery("delete from Product").executeUpdate();
        });
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

        given()
                .contentType(ContentType.JSON)
                .body("{\"items\": []}")
                .when().put("/carts/{id}", id)
                .then()
                .statusCode(200);
    }

    @Test
    void updateWithoutItemsReturns200() {
        Long id = persistCart();

        // PUT replaces the whole cart: a body without "items" means an empty cart, same as POST /carts with "{}".
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().put("/carts/{id}", id)
                .then()
                .statusCode(200);

        given()
                .when().get("/carts/{id}", id)
                .then()
                .statusCode(200)
                .body("items", empty());
    }

    @Test
    void updateWithNullItemsReturns200() {
        Long id = persistCart();

        given()
                .contentType(ContentType.JSON)
                .body("{\"items\": null}")
                .when().put("/carts/{id}", id)
                .then()
                .statusCode(200);
    }

    @Test
    void updateWithoutItemsRemovesExistingItems() {
        Long id = QuarkusTransaction.requiringNew().call(() -> {
            Product product = new Product();
            product.setName("Monitor");
            product.setPrice(199.0);
            cartRepository.getEntityManager().persist(product);

            Cart cart = new Cart();
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setCart(cart);
            item.setQuantity(2);
            cart.setItems(new ArrayList<>(List.of(item)));
            cartRepository.persist(cart);
            return cart.getId();
        });

        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().put("/carts/{id}", id)
                .then()
                .statusCode(200);

        assertEquals(0L, QuarkusTransaction.requiringNew().call(
                () -> cartRepository.getEntityManager()
                        .createQuery("select count(i) from CartItem i where i.cart.id = :id", Long.class)
                        .setParameter("id", id)
                        .getSingleResult()));
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
