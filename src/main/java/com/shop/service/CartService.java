package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.util.StringUtils;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.dto.CartOrderDto;
import com.shop.dto.CartOrderPageDto;
import com.shop.dto.OrderDto;
import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final OrderService orderService;


	public Long addCart(CartItemDto cartItemDto, String email) {
		// 현재 로그인한 회원 엔티티를 조회하는 과정
		Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
		Member member = memberRepository.findByEmail(email);

		Cart cart = cartRepository.findByMemberId(member.getId());
		// 상품을 처음으로 장바구니에 담을 경우 해당 회원의 장바구니 엔티티를 생성한다.
		if (cart == null) {
			cart = Cart.createCart(member);
			cartRepository.save(cart);
		}

		// 현재 상품이 장바구니에 이미 들어가있는지 조회합니다.
		CartItem savedCartItem = cartItemRepository.findByCartIdAndItemIdAndSize(cart.getId(), item.getId(), cartItemDto.getSize());
		

		// 장바구니에 이미 있던 상품일 경우 기존 수량에 현재 장바구니에 담을 수량만큼 더해준다.
		if (savedCartItem != null) {
			savedCartItem.addCount(cartItemDto.getCount());
			return savedCartItem.getId();

			// 장바구니 엔티티, 상품엔티티, 장바구니에 담을 수량을 이용하여 cartItem엔티티를 생성
			// 장바구니에 들어갈 상품을 저장
		} else {
			CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount(), cartItemDto.getSize());
			cartItemRepository.save(cartItem);
			return cartItem.getId();
		}
	}

	@Transactional(readOnly = true)
	public List<CartDetailDto> getCartList(String email) {

		List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

		Member member = memberRepository.findByEmail(email);
		System.out.println("~~~~~~~~~~~~~~");
		System.out.println(member.getEmail());
		System.out.println("~~~~~~~~~~~~~~");
		Cart cart = cartRepository.findByMemberId(member.getId());
		
		
		
		if (cart == null) {
			return cartDetailDtoList;
		}

		cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());

		return cartDetailDtoList;
	}
	

	

	
	

	@Transactional(readOnly = true)
	public List<CartDetailDto> orderCartPageItem(List<CartOrderPageDto> cartOrderPageDtoList, String email) {

		List<CartDetailDto> cartDetailDtoList = new ArrayList<>();
		
		Member member = memberRepository.findByEmail(email);
		Cart cart = cartRepository.findByMemberId(member.getId());
		 
		for (CartOrderPageDto cartOrderPageDto : cartOrderPageDtoList) {
			
		CartDetailDto cartDetailDto = cartItemRepository.findCartDetailDtoListt(cart.getId(), cartOrderPageDto.getCartItemId());
		
		
//		cartDetailDtoList= cartItemRepository.findCartDetailDtoListtt(cart.getId(), cartOrderPageDto.getCartItemId());
		cartDetailDtoList.add(cartItemRepository.findCartDetailDtoListt(cart.getId(), cartOrderPageDto.getCartItemId()));
		}
		// CartItem cartItem = cartItemRepository.findCartItemById(cartOrderPageDtoList.get(0).getCartItemId());
		
		return cartDetailDtoList;
	}

	// 352
	@Transactional(readOnly = true)
	public boolean validateCartItem(Long cartItemId, String email) {
		Member curMember = memberRepository.findByEmail(email);
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
		Member savedMember = cartItem.getCart().getMember();

		if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
			return false;
		}

		return true;
	}

	public void updateCartItemCount(Long cartItemId, int count) {
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

		cartItem.updateCount(count);
	}

	public void deleteCartItem(Long cartItemId) {
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
		cartItemRepository.delete(cartItem);
	}

	// cartController 에서 @PostMapping(value = "/cart/orders") 받고 데이터들 다시 여기로 보냄
	public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {
		List<OrderDto> orderDtoList = new ArrayList<>();

		for (CartOrderDto cartOrderDto : cartOrderDtoList) {
			CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
					.orElseThrow(EntityNotFoundException::new);

			OrderDto orderDto = new OrderDto();

			// 일단 여기서 객체 만들어서 넣어주는거 같으니 orderDto에 배송지 필드 추가해보겠음
			orderDto.setItemId(cartItem.getItem().getId());
			orderDto.setCount(cartItem.getCount());
			orderDto.setD_name(cartOrderDto.getD_name());
			orderDto.setD_address(cartOrderDto.getD_address());
			orderDto.setD_phone(cartOrderDto.getD_phone());
			orderDto.setD_memo(cartOrderDto.getD_memo());
			orderDto.setSize(cartItem.getSize()); 
			orderDtoList.add(orderDto);
		}

		Long orderId = orderService.orders(orderDtoList, email);

		for (CartOrderDto cartOrderDto : cartOrderDtoList) {
			CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
					.orElseThrow(EntityNotFoundException::new);
			cartItemRepository.delete(cartItem);
		}
		return orderId;
	}
	
	
	
	
	
	public Long orderPageItem(List<CartOrderDto> cartOrderDtoList, String email) {
		List<OrderDto> orderDtoList = new ArrayList<>();

		for (CartOrderDto cartOrderDto : cartOrderDtoList) {
			CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
					.orElseThrow(EntityNotFoundException::new);

			OrderDto orderDto = new OrderDto();

			// 일단 여기서 객체 만들어서 넣어주는거 같으니 orderDto에 배송지 필드 추가해보겠음
			orderDto.setItemId(cartItem.getItem().getId());
			orderDto.setCount(cartItem.getCount());
			orderDto.setD_name(cartOrderDto.getD_name());
			orderDto.setD_address(cartOrderDto.getD_address());
			orderDto.setD_phone(cartOrderDto.getD_phone());
			orderDto.setD_memo(cartOrderDto.getD_memo());
			orderDto.setSize(cartItem.getSize()); 
			orderDtoList.add(orderDto);
		}

		Long orderId = orderService.orders(orderDtoList, email);

		for (CartOrderDto cartOrderDto : cartOrderDtoList) {
			CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
					.orElseThrow(EntityNotFoundException::new);
			cartItemRepository.delete(cartItem);
		}
		return orderId;
	}


}
