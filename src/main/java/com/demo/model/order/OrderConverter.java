package com.demo.model.order;

import java.util.List;

public class OrderConverter {

    public static OrderResponseDTO toOrderResponseDTO(Order order){
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderCode(order.getOrderCode());
        responseDTO.setUsername(order.getUser().getUsername());
        responseDTO.setEmail(order.getUser().getEmail());
        List<ItemResponseDTO> items = OrderItemConverter.toItemResponseDTO(order);
        responseDTO.setItems(items);
        responseDTO.setOrderTime(order.getOrderTime());
        responseDTO.setShippingAddress(order.getOrderAddress());
        responseDTO.setTotal(order.getTotal());
        responseDTO.setOrderStatus(order.getOrderStatus());
        return responseDTO;
    }

    public static OrderDTO toOrderDTO(Order order){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderCode(order.getOrderCode());
        orderDTO.setUsername(order.getUser().getUsername());
        orderDTO.setEmail(order.getUser().getEmail());
        orderDTO.setOrderTime(order.getOrderTime());
        orderDTO.setShippingAddress(order.getOrderAddress());
        orderDTO.setTotal(order.getTotal());
        orderDTO.setOrderStatus(order.getOrderStatus());
        return orderDTO;
    }

    public static List<OrderDTO> toOrderDTOList(List<Order> list){
        return list.stream().map(OrderConverter::toOrderDTO).toList();
    }
}
