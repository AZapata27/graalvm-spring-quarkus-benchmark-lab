package co.example.springwebfluxnative.service;

import co.example.springwebfluxnative.model.entity.Cart;
import co.example.springwebfluxnative.model.entity.CartItem;
import co.example.springwebfluxnative.model.repository.CartItemRepository;
import co.example.springwebfluxnative.model.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    private static Cart cart(long id) {
        Cart cart = new Cart();
        cart.setId(id);
        cart.setUserId(id * 10);
        return cart;
    }

    @Test
    void findAllEmitsAllCarts() {
        Cart first = cart(1);
        Cart second = cart(2);
        when(cartRepository.findAll()).thenReturn(Flux.just(first, second));

        StepVerifier.create(cartService.findAll())
                .expectNext(first, second)
                .verifyComplete();
    }

    @Test
    void findByIdCompletesEmptyWhenMissing() {
        when(cartRepository.findById(42L)).thenReturn(Mono.empty());

        StepVerifier.create(cartService.findById(42L)).verifyComplete();
    }

    @Test
    void createAndUpdateSaveThroughRepository() {
        Cart cart = cart(3);
        when(cartRepository.save(cart)).thenReturn(Mono.just(cart));

        StepVerifier.create(cartService.create(cart)).expectNext(cart).verifyComplete();
        StepVerifier.create(cartService.update(cart)).expectNext(cart).verifyComplete();
    }

    @Test
    void deleteDeletesById() {
        when(cartRepository.deleteById(5L)).thenReturn(Mono.empty());

        StepVerifier.create(cartService.delete(5L)).verifyComplete();
        verify(cartRepository).deleteById(5L);
    }

    @Test
    void itemOperationsDelegateToCartItemRepository() {
        CartItem item = new CartItem();
        item.setCartId(1L);
        when(cartItemRepository.findByCartId(1L)).thenReturn(Flux.just(item));
        when(cartItemRepository.save(item)).thenReturn(Mono.just(item));
        when(cartItemRepository.deleteById(8L)).thenReturn(Mono.empty());

        StepVerifier.create(cartService.findItemsByCartId(1L)).expectNext(item).verifyComplete();
        StepVerifier.create(cartService.addItemToCart(item)).expectNext(item).verifyComplete();
        StepVerifier.create(cartService.removeItemFromCart(8L)).verifyComplete();
        verify(cartItemRepository).deleteById(8L);
    }
}
