package com.shop.dto;

import com.shop.entity.OrderItem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
	
	// 309 배송정보 여기에 같이 추가해보기 
	public OrderItemDto(OrderItem orderItem, String imgUrl) {
		this.itemNm = orderItem.getItem().getItemNm();
		this.count = orderItem.getCount();
		this.orderPrice = orderItem.getOrderPrice();
		this.imgUrl = imgUrl;
		
		
		
	}
	
	private String itemNm; // 상품명
	
	private int count; // 주문수량
	
	private int orderPrice; // 주문금액
	
	private String imgUrl; // 상품이미지 경로
	
	// 배송정보 여기도 추가요
	
	private String d_name;
	
	private String d_phone;
	
	private String d_memo;
	
	private String size;
	
	
}
