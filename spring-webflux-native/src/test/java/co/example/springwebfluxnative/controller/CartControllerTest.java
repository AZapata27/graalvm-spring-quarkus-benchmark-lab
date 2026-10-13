package co.example.springwebfluxnative.controller;

import co.example.springwebfluxnative.model.entity.Cart;
import co.example.springwebfluxnative.model.entity.CartItem;
import co.example.springwebfluxnative.service.CartService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// @MockBean is not supported by Spring AOT test processing (processTestAot / nativeTest).
@DisabledInAotMode
@WebFluxTest(CartController.class)
class CartControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CartService cartService;

    private static Cart cart(long id, long userId) {
        Cart cart = new Cart();
        cart.setId(id);
        cart.setUserId(userId);
        return cart;
    }

    private static CartItem item(long id, long cartId, long productId, int quantity) {
        CartItem item = new CartItem();
        item.setId(id);
        item.setCartId(cartId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        return item;
    }

    @Test
    void getAllStreamsCarts() {
        when(cartService.findAll()).thenReturn(Flux.just(cart(1, 10), cart(2, 20)));

        webTestClient.get().uri("/carts")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Cart.class).hasSize(2);
    }

    @Test
    void getByIdReturnsCart() {
        when(cartService.findById(1L)).thenReturn(Mono.just(cart(1, 10)));

        webTestClient.get().uri("/carts/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.userId").isEqualTo(10);
    }

    @Test
    void createReturnsSavedCart() {
        when(cartService.create(any(Cart.class))).thenReturn(Mono.just(cart(5, 50)));

        webTestClient.post().uri("/carts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"userId\": 50}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(5);
    }

    @Test
    void updateUsesIdFromPath() {
        when(cartService.update(any(Cart.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        webTestClient.put().uri("/carts/{id}", 7)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\": 999, \"userId\": 70}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(7)
                .jsonPath("$.userId").isEqualTo(70);
    }

    @Test
    void deleteDelegatesToService() {
        when(cartService.delete(3L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/carts/{id}", 3)
                .exchange()
                .expectStatus().isOk();

        verify(cartService).delete(3L);
    }

    @Test
    void getItemsReturnsItemsOfCart() {
        when(cartService.findItemsByCartId(1L)).thenReturn(Flux.just(item(1, 1, 1, 2), item(2, 1, 2, 1)));

        webTestClient.get().uri("/carts/{cartId}/items", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CartItem.class).hasSize(2);
    }

    @Test
    void addItemUsesCartIdFromPath() {
        when(cartService.addItemToCart(any(CartItem.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        webTestClient.post().uri("/carts/{cartId}/items", 4)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"productId\": 2, \"quantity\": 3}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.cartId").isEqualTo(4)
                .jsonPath("$.quantity").isEqualTo(3);

        ArgumentCaptor<CartItem> captor = ArgumentCaptor.forClass(CartItem.class);
        verify(cartService).addItemToCart(captor.capture());
        assertThat(captor.getValue().getCartId()).isEqualTo(4L);
    }

    @Test
    void removeItemDelegatesToService() {
        when(cartService.removeItemFromCart(9L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/carts/{cartId}/items/{itemId}", 1, 9)
                .exchange()
                .expectStatus().isOk();

        verify(cartService).removeItemFromCart(9L);
    }
}
