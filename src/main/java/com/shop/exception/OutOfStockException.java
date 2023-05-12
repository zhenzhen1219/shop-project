package com.shop.exception;

public class OutOfStockException extends RuntimeException {
	
	// 주문수량보다 재고의 수가 적을때 발생시킬 exception
	public OutOfStockException(String message) {
		super(message);
	}
}
