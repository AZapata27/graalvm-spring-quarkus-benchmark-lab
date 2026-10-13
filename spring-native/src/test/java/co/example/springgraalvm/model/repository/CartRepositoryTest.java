package co.example.springgraalvm.model.repository;

import co.example.springgraalvm.model.entity.Cart;
import co.example.springgraalvm.model.entity.CartItem;
import co.example.springgraalvm.model.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JPA mapping test against an embedded H2 database.
 */
@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void savesCartAndCascadesItems() {
        Product product = new Product();
        product.setName("Keyboard");
        product.setPrice(49.9);
        entityManager.persist(product);

        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.setItems(new ArrayList<>());
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setCart(cart);
        item.setQuantity(2);
        cart.getItems().add(item);

        Long id = cartRepository.save(cart).getId();
        entityManager.flush();
        entityManager.clear();

        Cart reloaded = cartRepository.findById(id).orElseThrow();
        assertThat(reloaded.getUserId()).isEqualTo(1L);
        assertThat(reloaded.getItems()).hasSize(1);
        assertThat(reloaded.getItems().get(0).getQuantity()).isEqualTo(2);
        assertThat(reloaded.getItems().get(0).getProduct().getName()).isEqualTo("Keyboard");
    }

    @Test
    void deleteByIdRemovesCartAndOrphanItems() {
        Product product = new Product();
        product.setName("Mouse");
        product.setPrice(19.9);
        entityManager.persist(product);

        Cart cart = new Cart();
        cart.setUserId(2L);
        cart.setItems(new ArrayList<>());
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setCart(cart);
        item.setQuantity(1);
        cart.getItems().add(item);
        Long id = cartRepository.save(cart).getId();
        entityManager.flush();
        entityManager.clear();

        cartRepository.deleteById(id);
        entityManager.flush();

        assertThat(cartRepository.findById(id)).isEmpty();
        assertThat(entityManager.getEntityManager()
                .createQuery("select count(i) from CartItem i", Long.class)
                .getSingleResult()).isZero();
    }
}
