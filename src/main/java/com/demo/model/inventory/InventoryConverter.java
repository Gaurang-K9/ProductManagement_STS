package com.demo.model.inventory;

import java.util.List;

public class InventoryConverter {

    public static InventoryResponseDTO toInventoryResponseDTO(Inventory inventory){
        InventoryResponseDTO inventoryResponseDTO = new InventoryResponseDTO();
        inventoryResponseDTO.setProductId(inventory.getProduct().getProductId());
        inventoryResponseDTO.setProductName(inventory.getProduct().getProductName());
        inventoryResponseDTO.setStockQuantity(inventory.getStockQuantity());
        inventoryResponseDTO.setReservedQuantity(inventory.getReservedQuantity());
        inventoryResponseDTO.setStockThreshold(inventory.getStockThreshold());
        return inventoryResponseDTO;
    }

    public static List<InventoryResponseDTO> toInventoryResponseDTOList(List<Inventory> inventoryList){
        return inventoryList.stream().map(InventoryConverter::toInventoryResponseDTO).toList();
    }

}
