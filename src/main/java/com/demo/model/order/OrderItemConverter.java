package com.demo.model.order;

import com.demo.model.product.Product;
import com.demo.model.product.ProductConverter;
import com.demo.model.product.ProductResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class OrderItemConverter {

    public static List<ItemResponseDTO> toItemResponseDTO(Order order){
        List<ItemResponseDTO> items = new ArrayList<>();

        for(int i=0; i<order.getItems().size(); i++){
            ItemResponseDTO item = new ItemResponseDTO();
            item.setOrderCode(order.getOrderCode());
            Product product = order.getItems().get(i).getProduct();
            ProductResponseDTO dto = ProductConverter.toProductResponseDTO(product);
            item.setProduct(dto);
            item.setQuantity(order.getItems().get(i).getQuantity());
            items.add(item);
        }
        return items;
    }
}
