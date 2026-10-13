package co.example.springwebfluxnative.model.repository;

import co.example.springwebfluxnative.model.entity.Cart;
import co.example.springwebfluxnative.model.entity.CartItem;
import co.example.springwebfluxnative.model.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * R2DBC repository test against an in-memory H2 database (schema-test.sql).
 */
@DataR2dbcTest
class CartItemRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private R2dbcEntityTemplate template;

    private Mono<Long> newCart(long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartRepository.save(cart).map(Cart::getId);
    }

    private Mono<CartItem> newItem(long cartId, long productId, int quantity) {
        CartItem item = new CartItem();
        item.setCartId(cartId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Test
    void findByCartIdReturnsOnlyItemsOfThatCart() {
        Product product = new Product();
        product.setName("Monitor");
        product.setPrice(199.0);

        Mono<Long> productId = template.insert(product).map(Product::getId).cache();
        Mono<Long> cartA = newCart(1L).cache();
        Mono<Long> cartB = newCart(2L).cache();

        Mono<Long> setup = Mono.zip(productId, cartA, cartB)
                .flatMap(ids -> newItem(ids.getT2(), ids.getT1(), 2)
                        .then(newItem(ids.getT2(), ids.getT1(), 1))
                        .then(newItem(ids.getT3(), ids.getT1(), 5))
                        .thenReturn(ids.getT2()));

        StepVerifier.create(setup.flatMapMany(cartItemRepository::findByCartId).map(CartItem::getQuantity).collectList())
                .assertNext(quantities -> org.assertj.core.api.Assertions.assertThat(quantities)
                        .containsExactlyInAnyOrder(2, 1))
                .verifyComplete();
    }

    @Test
    void savedCartGetsGeneratedIdAndCanBeDeleted() {
        StepVerifier.create(newCart(3L)
                        .flatMap(id -> cartRepository.deleteById(id).then(cartRepository.existsById(id))))
                .expectNext(false)
                .verifyComplete();
    }
}
