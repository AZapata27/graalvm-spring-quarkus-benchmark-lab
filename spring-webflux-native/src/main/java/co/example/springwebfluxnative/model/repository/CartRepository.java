package co.example.springwebfluxnative.model.repository;

import co.example.springwebfluxnative.model.entity.Cart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface CartRepository extends ReactiveCrudRepository<Cart, Long> {
}
