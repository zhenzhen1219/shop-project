package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartOrderPageDto;
import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.dto.OrderItemDto;
import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	private final OrderRepository orderRepository;
	private final ItemImgRepository itemImgRepository;

	
	public Long order(OrderDto orderDto, String email) {
		Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
		Member member = memberRepository.findByEmail(email);

		List<OrderItem> orderItemList = new ArrayList<>();
		OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount(), orderDto.getSize(),orderDto.getD_name(),
				orderDto.getD_address(), orderDto.getD_phone(), orderDto.getD_memo());
		orderItemList.add(orderItem);

		Order order = Order.createOrder(member, orderItemList);
		orderRepository.save(order);

		return order.getId();

	}
	

	
		
	public Long orders(List<OrderDto> orderDtoList, String email) {

		// 일단 위에 order 에도 배송지 받는거 추가해줬음
		Member member = memberRepository.findByEmail(email);
		List<OrderItem> orderItemList = new ArrayList<>();

		for (OrderDto orderDto : orderDtoList) {
			Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);

			OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount(),orderDto.getSize(), orderDto.getD_name(),
					orderDto.getD_address(), orderDto.getD_phone(), orderDto.getD_memo());
			orderItemList.add(orderItem);
		}

		Order order = Order.createOrder(member, orderItemList);
		orderRepository.save(order);

		return order.getId();
	}

	// orderController에서 호출
	@Transactional(readOnly = true)
	public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

		List<Order> orders = orderRepository.findOrders(email, pageable);
		Long totalCount = orderRepository.countOroder(email);

		List<OrderHistDto> orderHistDtos = new ArrayList<>();

		for (Order order : orders) {
			OrderHistDto orderHistDto = new OrderHistDto(order);
			List<OrderItem> orderItems = order.getOrderItems();

			for (OrderItem orderItem : orderItems) {
				ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
				OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
				orderHistDto.addOrderItemDto(orderItemDto);
			}

			orderHistDtos.add(orderHistDto);
		}

		return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
	}

	@Transactional(readOnly = true)
	public boolean validateOrder(Long orderId, String email) {
		Member curMember = memberRepository.findByEmail(email);
		Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
		Member savedMember = order.getMember();

		if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
			return false;
		}

		return true;
	}

	public void cancelOrder(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
		order.cancelOrder();
	}
	

}
