package co.example.springwebfluxnative.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "carts")
public class Cart {

    @Id
    private Long id;

    @Column(value = "user_id")
    private Long userId;

}
