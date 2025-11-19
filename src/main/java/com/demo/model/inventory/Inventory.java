package com.demo.model.inventory;

import com.demo.model.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    private Integer stockQuantity;
    private Integer reservedQuantity;
    private Integer stockThreshold;
}
