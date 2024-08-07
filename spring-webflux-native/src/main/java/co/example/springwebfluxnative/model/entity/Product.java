package co.example.springwebfluxnative.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "products")
public class Product {
    
    @Id
    private Long id;
    private String name;
    private Double price;
    private String description;
}
