package co.example.service;

import co.example.model.Cart;
import co.example.model.CartItem;
import co.example.repository.CartRepository;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Plain unit test (no Quarkus runtime): the reactive Panache repository is mocked.
 */
class CartServiceTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private CartRepository cartRepository;
    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartRepository = mock(CartRepository.class);
        cartService = new CartService(cartRepository);
    }

    private static Cart cart(long id) {
        Cart cart = new Cart();
        cart.setId(id);
        return cart;
    }

    @Test
    void findAllDelegatesToRepository() {
        List<Cart> carts = List.of(cart(1), cart(2));
        when(cartRepository.listAll()).thenReturn(Uni.createFrom().item(carts));

        assertEquals(carts, cartService.findAll().await().atMost(TIMEOUT));
    }

    @Test
    void findByIdDelegatesToRepository() {
        Cart cart = cart(7);
        when(cartRepository.findById(7L)).thenReturn(Uni.createFrom().item(cart));

        assertSame(cart, cartService.findById(7L).await().atMost(TIMEOUT));
    }

    @Test
    void createPersistsCart() {
        Cart cart = cart(1);
        when(cartRepository.persist(cart)).thenReturn(Uni.createFrom().item(cart));

        assertSame(cart, cartService.create(cart).await().atMost(TIMEOUT));
        verify(cartRepository).persist(cart);
    }

    @Test
    void updateCopiesItemsOntoExistingCart() {
        Cart existing = cart(3);
        Cart incoming = cart(3);
        List<CartItem> newItems = List.of(new CartItem());
        incoming.setItems(newItems);
        when(cartRepository.findById(3L)).thenReturn(Uni.createFrom().item(existing));
        when(cartRepository.persist(existing)).thenReturn(Uni.createFrom().item(existing));

        Cart updated = cartService.update(incoming).await().atMost(TIMEOUT);

        assertSame(existing, updated);
        assertSame(newItems, existing.getItems());
        verify(cartRepository).persist(existing);
    }

    @Test
    void updateReturnsNullAndDoesNotPersistWhenCartIsMissing() {
        Cart incoming = cart(99);
        when(cartRepository.findById(99L)).thenReturn(Uni.createFrom().nullItem());

        assertNull(cartService.update(incoming).await().atMost(TIMEOUT));
        verify(cartRepository, never()).persist(any(Cart.class));
    }

    @Test
    void deleteDelegatesToRepository() {
        when(cartRepository.deleteById(5L)).thenReturn(Uni.createFrom().item(true));

        assertTrue(cartService.delete(5L).await().atMost(TIMEOUT));
    }
}
