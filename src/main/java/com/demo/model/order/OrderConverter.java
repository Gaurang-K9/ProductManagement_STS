package com.demo.model.order;

import java.util.List;

public class OrderConverter {

    public static OrderResponseDTO toOrderResponseDTO(Order order){
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderId(order.getOrderId());
        responseDTO.setUsername(order.getUser().getUsername());
        responseDTO.setEmail(order.getUser().getEmail());
        List<ItemResponseDTO> items = OrderItemConverter.toItemResponseDTO(order);
        responseDTO.setItems(items);
        responseDTO.setOrderTime(order.getOrderTime());
        responseDTO.setShippingAddress(order.getOrderAddress());
        responseDTO.setTotal(order.getTotal());
        return responseDTO;
    }
}
