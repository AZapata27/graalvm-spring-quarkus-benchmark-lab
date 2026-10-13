package co.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Never null: with orphanRemoval, merging a cart whose collection is null makes Hibernate fail with
    // "A collection with cascade=all-delete-orphan was no longer referenced" (HTTP 500 on PUT /carts/{id}).
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    /**
     * A missing or {@code null} "items" in the request body means an empty cart (PUT replaces the whole cart).
     * Hibernate uses field access here, so this setter only affects application/Jackson code.
     */
    public void setItems(List<CartItem> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

}
