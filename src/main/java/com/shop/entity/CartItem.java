package com.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="cart_item")
public class CartItem extends BaseEntity {
		
	@Id
	@GeneratedValue
	@Column(name="cart_item_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cart_id")
	private Cart cart;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="item_id")
	private Item item;
	
	private int count;
	
	private String size;
	
	public static CartItem createCartItem(Cart cart, Item item, int count,String size) {
		CartItem cartItem = new CartItem();
		cartItem.setCart(cart);
		cartItem.setItem(item);
		cartItem.setCount(count);
		cartItem.setSize(size);	
		return cartItem;
	}
	
	public void addCount(int count) {
		this.count += count;
	}
	
	// 351 장바구니 수량변경시
	public void updateCount(int count) {
		this.count = count;
	}
	
	
}
