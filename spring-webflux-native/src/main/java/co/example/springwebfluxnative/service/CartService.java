package co.example.springwebfluxnative.service;

import co.example.springwebfluxnative.model.entity.Cart;
import co.example.springwebfluxnative.model.entity.CartItem;
import co.example.springwebfluxnative.model.repository.CartItemRepository;
import co.example.springwebfluxnative.model.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    public Flux<Cart> findAll() {
        return cartRepository.findAll();
    }

    public Mono<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }

    public Mono<Cart> create(Cart cart) {
        return cartRepository.save(cart);
    }

    public Mono<Cart> update(Cart cart) {
        return cartRepository.save(cart);
    }

    public Mono<Void> delete(Long id) {
        return cartRepository.deleteById(id);
    }

    public Flux<CartItem> findItemsByCartId(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    public Mono<CartItem> addItemToCart(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public Mono<Void> removeItemFromCart(Long cartItemId) {
        return cartItemRepository.deleteById(cartItemId);
    }
}
