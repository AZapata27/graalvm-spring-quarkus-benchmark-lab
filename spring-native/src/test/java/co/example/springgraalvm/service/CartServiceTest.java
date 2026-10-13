package co.example.springgraalvm.service;

import co.example.springgraalvm.model.entity.Cart;
import co.example.springgraalvm.model.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    private static Cart cart(long id) {
        Cart cart = new Cart();
        cart.setId(id);
        cart.setUserId(id * 10);
        return cart;
    }

    @Test
    void getAllCartsDelegatesToRepository() {
        List<Cart> carts = List.of(cart(1), cart(2));
        when(cartRepository.findAll()).thenReturn(carts);

        assertThat(cartService.getAllCarts()).isEqualTo(carts);
    }

    @Test
    void getCartByIdReturnsOptionalFromRepository() {
        Cart cart = cart(1);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.findById(2L)).thenReturn(Optional.empty());

        assertThat(cartService.getCartById(1L)).containsSame(cart);
        assertThat(cartService.getCartById(2L)).isEmpty();
    }

    @Test
    void saveCartReturnsPersistedEntity() {
        Cart input = new Cart();
        input.setUserId(7L);
        Cart saved = cart(3);
        when(cartRepository.save(input)).thenReturn(saved);

        assertThat(cartService.saveCart(input)).isSameAs(saved);
    }

    @Test
    void deleteCartDeletesById() {
        cartService.deleteCart(4L);

        verify(cartRepository).deleteById(4L);
    }
}
