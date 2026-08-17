package com.demo.model.order;

import com.demo.model.user.UserConverter;

import java.util.List;

public class OrderConverter {

    public static OrderResponseDTO toOrderResponseDTO(Order order){
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderCode(order.getOrderCode());
        responseDTO.setUser(UserConverter.toSimpleUserDTO(order.getUser()));
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
        orderDTO.setUser(UserConverter.toSimpleUserDTO(order.getUser()));
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
