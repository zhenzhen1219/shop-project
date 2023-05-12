package com.shop.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CartOrderDto {

	private Long cartItemId;

	private String size;
	private String d_name;
	private String d_address;
	private String d_phone;
	private String d_memo;
	
	private List<CartOrderDto> cartOrderDtoList;


}
