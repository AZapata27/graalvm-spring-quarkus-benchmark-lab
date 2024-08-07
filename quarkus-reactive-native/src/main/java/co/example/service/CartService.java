package co.example.service;

import co.example.model.Cart;
import co.example.repository.CartRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@ApplicationScoped
public class CartService {

    private final CartRepository cartRepository;

    public Uni<List<Cart>> findAll() {
        return cartRepository.listAll();
    }

    public Uni<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }

    @Transactional
    public Uni<Cart> create(Cart cart) {
        return cartRepository.persist(cart);
    }

    @Transactional
    public Uni<Cart> update(Cart cart) {
        return cartRepository.findById(cart.getId())
                .onItem().ifNotNull().transformToUni(existingCart -> {
                    existingCart.setItems(cart.getItems());
                    return cartRepository.persist(existingCart);
                });
    }

    @Transactional
    public Uni<Boolean> delete(Long id) {
        return cartRepository.deleteById(id);
    }
}
