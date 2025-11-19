package com.demo.controller;

import com.demo.model.inventory.Inventory;
import com.demo.model.inventory.InventoryConverter;
import com.demo.model.inventory.InventoryResponseDTO;
import com.demo.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @PostMapping("/product/{id}/create")
    public ResponseEntity<InventoryResponseDTO> createInventory(@PathVariable Long id, @RequestParam Integer qty){
        Inventory inventory = inventoryService.createInventory(id, qty);
        InventoryResponseDTO responseDTO = InventoryConverter.toInventoryResponseDTO(inventory);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<InventoryResponseDTO> findInventoryByProductId(@PathVariable Long id){
        Inventory inventory = inventoryService.findInventoryByProductId(id);
        InventoryResponseDTO responseDTO = InventoryConverter.toInventoryResponseDTO(inventory);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PatchMapping("/product/{id}/resupply")
    public ResponseEntity<InventoryResponseDTO> resupplyStock(@PathVariable Long id, @RequestParam Integer qty){
        Inventory inventory = inventoryService.resupplyStock(id, qty);
        InventoryResponseDTO responseDTO = InventoryConverter.toInventoryResponseDTO(inventory);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PatchMapping("/product/{id}/threshold")
    public ResponseEntity<InventoryResponseDTO> updateStockThreshold (@PathVariable Long id, @RequestParam Integer qty){
        Inventory inventory = inventoryService.updateStockThreshold(id, qty);
        InventoryResponseDTO responseDTO = InventoryConverter.toInventoryResponseDTO(inventory);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/lowstock")
    public ResponseEntity<List<InventoryResponseDTO>> findInventoryBelowThreshold(){
        List<Inventory> inventories = inventoryService.findInventoryBelowThreshold();
        List<InventoryResponseDTO> inventoryList = InventoryConverter.toInventoryResponseDTOList(inventories);
        return ResponseEntity.status(HttpStatus.OK).body(inventoryList);
    }
}
