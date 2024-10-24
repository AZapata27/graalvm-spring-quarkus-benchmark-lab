package co.example.service;

import co.example.model.Cart;
import co.example.repository.CartRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Cart> findAll() {
        return cartRepository.listAll();
    }

    public Cart findById(Long id) {
        return cartRepository.findById(id);
    }

    @Transactional
    public void create(Cart cart) {
        cartRepository.persist(cart);
    }

    @Transactional
    public void update(Cart cart) {
        cartRepository.getEntityManager().merge(cart);
    }

    @Transactional
    public void delete(Long id) {
        cartRepository.deleteById(id);
    }

    public void processCart(Cart cart) {
    }

    public void processBatch(List<Cart> carts) {

    }
}
