package co.example.repository;

import co.example.model.Cart;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CartRepository implements PanacheRepository<Cart> {

    public List<Cart> findByPage(int page, int size) {
        return find("ORDER BY id")
                .page(page, size)
                .list();
    }
}
