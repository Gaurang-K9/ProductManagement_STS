package com.demo.controller;

import com.demo.model.inventory.Inventory;
import com.demo.model.inventory.InventoryConverter;
import com.demo.model.inventory.InventoryResponseDTO;
import com.demo.service.InventoryService;
import com.demo.shared.ApiResponse;
import com.demo.shared.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @PostMapping("/product/{id}/create")
    public ResponseEntity<ApiResponse<InventoryResponseDTO>> createInventory(@PathVariable Long id, @RequestParam Integer qty){
        Inventory inventory = inventoryService.createInventory(id, qty);
        InventoryResponseDTO responseDTO = InventoryConverter.toInventoryResponseDTO(inventory);
        String message = "Inventory successfully created for product: "+responseDTO.getProductName();
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(responseDTO));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse<InventoryResponseDTO>> findInventoryByProductId(@PathVariable Long id){
        Inventory inventory = inventoryService.findInventoryByProductId(id);
        InventoryResponseDTO responseDTO = InventoryConverter.toInventoryResponseDTO(inventory);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(responseDTO));
    }

    @PatchMapping("/product/{id}/resupply")
    public ResponseEntity<ApiResponse<InventoryResponseDTO>> resupplyStock(@PathVariable Long id, @RequestParam Integer qty){
        Inventory inventory = inventoryService.resupplyStock(id, qty);
        InventoryResponseDTO responseDTO = InventoryConverter.toInventoryResponseDTO(inventory);
        String message = "Successfully resupplied stock for product: "+responseDTO.getProductName();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(responseDTO));
    }

    @PatchMapping("/product/{id}/threshold")
    public ResponseEntity<ApiResponse<InventoryResponseDTO>> updateStockThreshold (@PathVariable Long id, @RequestParam Integer qty){
        Inventory inventory = inventoryService.updateStockThreshold(id, qty);
        InventoryResponseDTO responseDTO = InventoryConverter.toInventoryResponseDTO(inventory);
        String message = "Successfully increased stock threshold for product: "+responseDTO.getProductName();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(responseDTO));
    }

    @GetMapping("/lowstock")
    public ResponseEntity<ApiResponse<PageResponse<InventoryResponseDTO>>> findInventoryBelowThreshold(
            @PageableDefault(sort = "stockQuantity", direction = Sort.Direction.ASC) Pageable pageable){
       var response = PageResponse
               .fromPage(inventoryService.findInventoryBelowThreshold(pageable)
               .map(InventoryConverter::toInventoryResponseDTO));
       return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }
}
