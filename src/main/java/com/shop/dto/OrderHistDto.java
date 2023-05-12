package com.shop.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.shop.constant.OrderStatus;
import com.shop.entity.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class OrderHistDto {
	
	public OrderHistDto(Order order) {
		this.orderId = order.getId();
		this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		this.orderStatus = order.getOrderStatus();
		this.size = order.getSize();
		this.d_name = order.getD_name();
		this.d_phone = order.getD_phone();
		this.d_address = order.getD_address();
		this.d_memo = order.getD_memo();
		this.totalprice = order.getTotalPrice();
	
	}
	
	private Long orderId;
	
	private String orderDate;
	
	private OrderStatus orderStatus;
	
	private String size; 
	
	// 배송지 정보도 같이 기입되도록
	private String d_name;
	private String d_address;
	private String d_phone;
	private String d_memo;
	private int totalprice;
	
	// 주문 상품 리스트
	
	private List<OrderItemDto> orderItemDtoList = new ArrayList<>();
	
	public void addOrderItemDto(OrderItemDto orderItemDto) {
		orderItemDtoList.add(orderItemDto);
	}
}
