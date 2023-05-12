package com.shop.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name="order_item_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="item_id")
	private Item item;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_id")
	private Order order;
	
	private int orderPrice;
	
	private int count;
	
	private String size;
	
	// 배송지 추가해보자
	
	private String d_name;
	
	private String d_address;
	
	private String d_phone;
	
	private String d_memo;
 
// 일단 여기에 필드 추가해 주고 배송지 정보도 같이 받는걸로 설정,,,?	
	public static OrderItem createOrderItem(Item item, int count, String size,String d_name, String d_address, String d_phone, String d_memo) {
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		orderItem.setCount(count);
		orderItem.setSize(size);
		orderItem.setOrderPrice(item.getPrice());
		orderItem.setD_name(d_name);
		orderItem.setD_address(d_address);
		orderItem.setD_phone(d_phone);
		orderItem.setD_memo(d_memo);
		
		
		item.removeStock(count);
		return orderItem;
	}

	public int getTotalPrice() {
		return orderPrice*count;
	}
	
	public void cancel() {
        this.getItem().addStock(count);
    }

	
}
