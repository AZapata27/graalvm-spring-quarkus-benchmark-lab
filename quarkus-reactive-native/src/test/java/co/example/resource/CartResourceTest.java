package co.example.resource;

import co.example.model.Cart;
import co.example.service.CartService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * REST layer test: the reactive service is replaced by a Mockito mock, so no PostgreSQL is needed.
 */
@QuarkusTest
class CartResourceTest {

    @InjectMock
    CartService cartService;

    private static Cart cart(long id) {
        Cart cart = new Cart();
        cart.setId(id);
        cart.setItems(List.of());
        return cart;
    }

    @Test
    void getAllReturnsCarts() {
        when(cartService.findAll()).thenReturn(Uni.createFrom().item(List.of(cart(1), cart(2))));

        given()
                .when().get("/carts")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(2))
                .body("[0].id", equalTo(1));
    }

    @Test
    void getByIdReturnsCart() {
        when(cartService.findById(5L)).thenReturn(Uni.createFrom().item(cart(5)));

        given()
                .when().get("/carts/{id}", 5)
                .then()
                .statusCode(200)
                .body("id", equalTo(5));
    }

    @Test
    void getByIdReturns404WhenMissing() {
        when(cartService.findById(9L)).thenReturn(Uni.createFrom().nullItem());

        given()
                .when().get("/carts/{id}", 9)
                .then()
                .statusCode(404);
    }

    @Test
    void createReturns201WithCreatedCart() {
        when(cartService.create(any(Cart.class))).thenReturn(Uni.createFrom().item(cart(10)));

        given()
                .contentType(ContentType.JSON)
                .body("{\"items\": []}")
                .when().post("/carts")
                .then()
                .statusCode(201)
                .body("id", equalTo(10));
    }

    @Test
    void updateSetsPathIdAndReturns200() {
        when(cartService.update(any(Cart.class))).thenReturn(Uni.createFrom().item(cart(3)));

        given()
                .contentType(ContentType.JSON)
                .body("{\"items\": []}")
                .when().put("/carts/{id}", 3)
                .then()
                .statusCode(200)
                .body("id", equalTo(3));

        ArgumentCaptor<Cart> captor = ArgumentCaptor.forClass(Cart.class);
        verify(cartService).update(captor.capture());
        assertEquals(3L, captor.getValue().getId());
    }

    @Test
    void updateReturns404WhenMissing() {
        when(cartService.update(any(Cart.class))).thenReturn(Uni.createFrom().nullItem());

        given()
                .contentType(ContentType.JSON)
                .body("{\"items\": []}")
                .when().put("/carts/{id}", 99)
                .then()
                .statusCode(404);
    }

    @Test
    void deleteReturns204() {
        when(cartService.delete(4L)).thenReturn(Uni.createFrom().item(true));

        given()
                .when().delete("/carts/{id}", 4)
                .then()
                .statusCode(204);

        verify(cartService).delete(4L);
    }
}
