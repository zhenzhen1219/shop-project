package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto2 {
	private Long cartItemId;
	
	private String itemNm;
	
	private int price;
	
	private int count;
	
	private String imgUrl;
	
//	private String deliveryStatus;
	
	public CartDetailDto2(Long cartItemId, String itemNm, int price,
			int count, String imgUrl) {
		this.cartItemId = cartItemId;
		this.itemNm = itemNm;
		this.price = price;
		this.count = count;
		this.imgUrl = imgUrl;
//		this.deliveryStatus = deliveryStatus;
	}
}
