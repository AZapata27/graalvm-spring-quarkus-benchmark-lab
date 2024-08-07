package co.example.springwebfluxnative.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Getter
@Setter
@Table(name = "cart_items")
public class CartItem {

    @Id
    private Long id;

    @Column(value = "product_id")
    private Long productId;

    @Column(value = "cart_id")
    private Long cartId;

    @Column
    private Integer quantity;
}
