package co.example.springgraalvm.model.repository;

import co.example.springgraalvm.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}