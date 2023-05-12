package com.shop.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.shop.constant.OrderStatus;
import com.shop.dto.OrderItemDto;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="orders")
@Getter
@Setter
public class Order extends BaseEntity {
	
	@Id
	@GeneratedValue
	@Column(name="order_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id")
	private Member member;
	
	private LocalDateTime orderDate;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	// CascadeTypeAll : 부모 엔티티의 영속성 상태변화를 자식 엔티티에 모두 전이하는 것
	// orphanRemoval : 주문 엔티티(부모 엔티티)에서 주문 상품(자식엔티티)를 삭제했을 때 orderItem 엔티티가 삭제
	
	
	private List<OrderItem> orderItems = new ArrayList<>();
	

	private String size;

	// 배송지 추가해보자
	private String d_name;
	private String d_address;
	private String d_phone;
	private String d_memo;
	private int totalprice;
	
	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		// orderItem.setOrder(this);
		orderItem.setOrder(this);
	}
	
	public static Order createOrder(Member member, List<OrderItem> orderItemList) {
		Order order = new Order();
		order.setMember(member);
		for(OrderItem orderItem : orderItemList) {
			order.addOrderItem(orderItem);
		}
		order.setSize(orderItemList.get(0).getSize());
		order.setD_name(orderItemList.get(0).getD_name());
		order.setD_address(orderItemList.get(0).getD_address());
		order.setD_phone(orderItemList.get(0).getD_phone());
		order.setD_memo(orderItemList.get(0).getD_memo());
		order.setOrderStatus(OrderStatus.ORDER);
		order.setOrderDate(LocalDateTime.now());
		order.setTotalprice(order.getTotalPrice());
		
		// 여기 추가아직 안했음
		
		return order;
	}
	
	public int getTotalPrice() {
		int totalPrice = 0;
		for(OrderItem orderItem : orderItems) {
			totalPrice += orderItem.getTotalPrice();
		}
	
		return totalPrice;
	}
	
	public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEl;

        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }
	
	
	
}
