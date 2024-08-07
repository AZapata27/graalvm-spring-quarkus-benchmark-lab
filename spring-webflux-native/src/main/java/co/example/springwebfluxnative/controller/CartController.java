package co.example.springwebfluxnative.controller;

import co.example.springwebfluxnative.model.entity.Cart;
import co.example.springwebfluxnative.model.entity.CartItem;
import co.example.springwebfluxnative.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public Flux<Cart> getAll() {
        return cartService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Cart> getById(@PathVariable Long id) {
        return cartService.findById(id);
    }

    @PostMapping
    public Mono<Cart> create(@RequestBody Cart cart) {
        return cartService.create(cart);
    }

    @PutMapping("/{id}")
    public Mono<Cart> update(@PathVariable Long id, @RequestBody Cart cart) {
        cart.setId(id);
        return cartService.update(cart);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return cartService.delete(id);
    }

    @GetMapping("/{cartId}/items")
    public Flux<CartItem> getItems(@PathVariable Long cartId) {
        return cartService.findItemsByCartId(cartId);
    }

    @PostMapping("/{cartId}/items")
    public Mono<CartItem> addItem(@PathVariable Long cartId, @RequestBody CartItem cartItem) {
        cartItem.setCartId(cartId);
        return cartService.addItemToCart(cartItem);
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public Mono<Void> removeItem(@PathVariable Long cartId, @PathVariable Long itemId) {
        return cartService.removeItemFromCart(itemId);
    }
}
