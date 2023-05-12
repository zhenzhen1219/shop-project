package com.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartDetailDto {
	private Long cartItemId;
	
	private String itemNm;
	
	private int price;
	
	private int count;
	
	private String imgUrl;
	
	private String size;
	
//	private String deliveryStatus;
	
	public CartDetailDto(Long cartItemId, String itemNm, int price,
		int count, String imgUrl, String size) {
		this.cartItemId = cartItemId;
		this.itemNm = itemNm;
		this.price = price;
		this.count = count;
		this.imgUrl = imgUrl;
		this.size = size;
//		this.deliveryStatus = deliveryStatus;
	}
}
