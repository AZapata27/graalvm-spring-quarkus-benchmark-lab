package co.example.repository;

import co.example.model.Cart;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CartRepository implements PanacheRepository<Cart> {
    // Métodos adicionales si es necesario
}
