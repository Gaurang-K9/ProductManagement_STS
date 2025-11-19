package com.demo.repo;

import com.demo.model.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProduct_ProductId(Long productId);

    @Query("SELECT i FROM Inventory i WHERE i.stockThreshold > i.stockQuantity")
    List<Inventory> findWithLowStock();
}
