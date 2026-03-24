package com.demo.repo;

import com.demo.model.inventory.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {

    String lowInventoryQuery = "SELECT i FROM Inventory i WHERE i.stockThreshold > i.stockQuantity";

    Optional<Inventory> findByProduct_ProductId(Long productId);

    @Query(value = lowInventoryQuery)
    Page<Inventory> findWithLowStock(Pageable pageable);
}
