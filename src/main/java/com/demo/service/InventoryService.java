package com.demo.service;

import com.demo.exception.ResourceNotFoundException;
import com.demo.model.inventory.Inventory;
import com.demo.model.order.Order;
import com.demo.model.order.OrderItem;
import com.demo.model.product.Product;
import com.demo.repo.InventoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    InventoryRepo inventoryRepo;

    @Autowired
    ProductService productService;

    private Integer defaultStockThreshold(Product product){
        BigDecimal productPrice = product.getPrice();
        if (productPrice.compareTo(new BigDecimal("100")) < 0) {
            return 1000;
        } else if (productPrice.compareTo(new BigDecimal("500")) < 0) {
            return 500;
        } else if (productPrice.compareTo(new BigDecimal("2000")) < 0) {
            return 250;
        } else {
            return 50;
        }
    }

    public Inventory createInventory(Long productId, Integer quantity){
        Product product = productService.findProductById(productId);
        Integer stockThreshold = defaultStockThreshold(product);

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setStockQuantity(quantity);
        inventory.setReservedQuantity(0);
        inventory.setStockThreshold(stockThreshold);
        return inventoryRepo.save(inventory);
    }

    public Inventory resupplyStock(Long productId, Integer quantity){
        Inventory inventory = inventoryRepo.findByProduct_ProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException(Inventory.class, "productId", productId));

        if(quantity <= 0){
            throw new IllegalArgumentException("Added Quantity cannot be "+quantity);
        }

        Integer newQuantity = inventory.getStockQuantity() + quantity;
        inventory.setStockQuantity(newQuantity);
        return inventoryRepo.save(inventory);
    }

    public void updateStockAndReserveQuantity(Order order){
        List<OrderItem> items = order.getItems();

        switch (order.getOrderStatus()) {
            case CREATED -> placeOrder(items);
            case DELIVERED -> deliverOrder(items);
            case CANCELLED -> cancelOrder(items);
            case RETURNED -> returnOrder(items);
        }
    }

    private void validateItem(OrderItem item) {
        if (item.getQuantity() <= 0)
            throw new IllegalArgumentException("Invalid item quantity: " + item.getQuantity());
    }

    private void deliverOrder(List<OrderItem> items) {
        for(OrderItem item: items){
            Inventory inventory = findInventoryByProductId(item.getProduct().getProductId());
            validateItem(item);

            int reserveQuantity =  inventory.getReservedQuantity() - item.getQuantity();
            if (reserveQuantity < 0) {
                throw new IllegalStateException("Invalid reserve stock for product: " + item.getProduct().getProductName());
            }
            inventory.setReservedQuantity(reserveQuantity);
            inventoryRepo.save(inventory);
        }
    }

    private void placeOrder(List<OrderItem> items) {
        for(OrderItem item: items){
            Inventory inventory = findInventoryByProductId(item.getProduct().getProductId());
            validateItem(item);

            if (inventory.getStockQuantity() < item.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + item.getProduct().getProductName());
            }
            Integer stockQuantity = inventory.getStockQuantity() - item.getQuantity();
            Integer reserveQuantity =  inventory.getReservedQuantity() + item.getQuantity();
            inventory.setStockQuantity(stockQuantity);
            inventory.setReservedQuantity(reserveQuantity);
            inventoryRepo.save(inventory);
        }
    }

    private void cancelOrder(List<OrderItem> items) {
        for(OrderItem item: items){
            Inventory inventory = findInventoryByProductId(item.getProduct().getProductId());
            validateItem(item);

            int reserveQuantity =  inventory.getReservedQuantity() - item.getQuantity();
            if (reserveQuantity < 0) {
                throw new IllegalStateException("Invalid reserve stock for cancelling product: " + item.getProduct().getProductName());
            }
            Integer stockQuantity = inventory.getStockQuantity() + item.getQuantity();

            inventory.setStockQuantity(stockQuantity);
            inventory.setReservedQuantity(reserveQuantity);
            inventoryRepo.save(inventory);
        }
    }

    private void returnOrder(List<OrderItem> items) {
        for(OrderItem item: items){
            Inventory inventory = findInventoryByProductId(item.getProduct().getProductId());
            validateItem(item);

            Integer stockQuantity = inventory.getStockQuantity() + item.getQuantity();
            inventory.setStockQuantity(stockQuantity);
            inventoryRepo.save(inventory);
        }
    }

    public Inventory findInventoryByProductId(Long productId){
        return inventoryRepo.findByProduct_ProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException(Inventory.class, "productId", productId));
    }

    public Inventory updateInventory(Inventory inventory){
        return inventoryRepo.save(inventory);
    }

    public Inventory updateStockThreshold(Long productId, Integer stockThreshold){
        Inventory inventory = findInventoryByProductId(productId);
        inventory.setStockThreshold(stockThreshold);
        return updateInventory(inventory);
    }

    public List<Inventory> findInventoryBelowThreshold(){
        return inventoryRepo.findWithLowStock();
    }

}
