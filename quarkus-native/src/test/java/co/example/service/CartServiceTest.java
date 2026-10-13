package co.example.service;

import co.example.model.Cart;
import co.example.repository.CartRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Plain unit test (no Quarkus runtime): the repository is mocked.
 */
class CartServiceTest {

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
        when(cartRepository.listAll()).thenReturn(carts);

        assertEquals(carts, cartService.findAll());
    }

    @Test
    void findByIdReturnsCartWhenPresent() {
        Cart cart = cart(7);
        when(cartRepository.findById(7L)).thenReturn(cart);

        assertSame(cart, cartService.findById(7L));
    }

    @Test
    void findByIdReturnsNullWhenMissing() {
        assertNull(cartService.findById(42L));
    }

    @Test
    void createPersistsCart() {
        Cart cart = cart(1);

        cartService.create(cart);

        verify(cartRepository).persist(cart);
    }

    @Test
    void updateMergesCartThroughEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        when(cartRepository.getEntityManager()).thenReturn(entityManager);
        Cart cart = cart(3);

        cartService.update(cart);

        verify(entityManager).merge(cart);
    }

    @Test
    void deleteRemovesById() {
        cartService.delete(5L);

        verify(cartRepository).deleteById(5L);
    }
}
