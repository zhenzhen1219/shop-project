package com.shop.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.dto.CartOrderDto;
import com.shop.dto.CartOrderPageDto;
import com.shop.dto.ItemFormDto;
import com.shop.dto.OrderDto;
import com.shop.service.CartService;
import com.shop.service.ItemService;
import com.shop.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {

	private final ItemService itemService;
	private final CartService cartService;
	private final OrderService orderService;
	List<CartDetailDto> cartDetailList;
	List<CartOrderPageDto> cartOrderPageDtolist;

	@PostMapping("/cart")
	// 장바구니에 담을 상품 정보를 받는 cartItemDto 객체에 데이터 바인딩 시 에리가 있는지 검사합니다.
	public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult,
			Principal principal) {

		if (bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();

			for (FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage());
			}
			return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
		}
		String email = principal.getName();
		Long cartItemId;

		try {
			cartItemId = cartService.addCart(cartItemDto, email);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
	}

	@GetMapping("/cart")
	public String orderHist(Principal principal, Model model) {
		System.out.println("-------------------------");
		System.out.println("cartController부분에서 cart 눌렀을때 이동하는 구간입니다.");
		System.out.println(principal.getName());
		System.out.println("-------------------------");
		List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
		
		int count = cartDetailList.size();
		System.out.println("-------몇개인지 확인해주세요-0000");
		System.out.println(count);
		
		model.addAttribute("cartItems", cartDetailList);
		return "cart/cartList";
	}
	
	
	

	// 장바구니에서 페이지이동
	@ResponseBody
	@PostMapping("/cart/orderPageTest")
	public String orderPagezi(@RequestBody List<CartOrderPageDto> cartOrderPageDtolist, Model model,
			Principal principal) throws Exception {
		cartDetailList = cartService.orderCartPageItem(cartOrderPageDtolist, principal.getName());
		System.out.println(cartOrderPageDtolist.get(0).getCartItemId());
		model.addAttribute("cartItems", cartDetailList);
		String test = "하위";
		// redirect.addFlashAttribute("cartItems",test);
		return "order/orderPage";
		// return "order/orderPageTest";
	}

	// 주문페이지에 데이터 전달
	@GetMapping("/cart/orderPageTest")
	public String cartOrderPage(Model model, Principal principal) {
		model.addAttribute("cartItems", cartDetailList);
		return "order/orderPage";

	}

	@PostMapping(value = { "/order/orderPage1" })
	public String itemDtl(Model model, @RequestParam(required = false, value = "itemId") String itemId,
			@RequestParam(required = false, value = "count") int count, @RequestParam(required = false, value = "size") String size) {
		System.out.println("----------------------------------------");
		System.out.println("----------------------------------------");
		System.out.println("----------------------------------------");
		System.out.println("----------------------------------------");
		System.out.println(itemId);
		System.out.println(count);
		System.out.println(size);

		ItemFormDto itemFormDto = itemService.getItemDtl(Long.parseLong(itemId));
		model.addAttribute("item", itemFormDto);
		model.addAttribute("count", count);
		model.addAttribute("size", size);
		return "order/itemDtlOrderPage";
	}


	@PatchMapping(value = "/cartItem/{cartItemId}")
	public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count,
			Principal principal) {

		if (count <= 0) {
			return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
		} else if (!cartService.validateCartItem(cartItemId, principal.getName())) {
			return new ResponseEntity<String>("수정할 권한이 없습니다.", HttpStatus.FORBIDDEN);
		}

		cartService.updateCartItemCount(cartItemId, count);
		return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
	}

	// 357 삭제 기능
	@DeleteMapping(value = "/cartItem/{cartItemId}")
	public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId,
			Principal principal) {
		
		System.out.println(cartItemId);

		if (!cartService.validateCartItem(cartItemId, principal.getName())) {
			return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
		}

		cartService.deleteCartItem(cartItemId);
		return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
	}

	// 장바구니 -> 주문페이지 통해서 주문버튼 누른순간
	@PostMapping(value = "/cart/orders")
	public @ResponseBody ResponseEntity cartOrder(@RequestBody CartOrderDto cartOrderDto, Principal principal) {

		List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();
		System.out.println("------------------------------------------------------");
		System.out.println(cartOrderDto.getCartOrderDtoList().get(0));
		System.out.println("------------------------------------------------------");

		if (cartOrderDtoList == null || cartOrderDtoList.size() == 0) {
			return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);
		}

		for (CartOrderDto cartOrder : cartOrderDtoList) {
			if (!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())) {
				return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
			}
		}

		Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
		return new ResponseEntity<Long>(orderId, HttpStatus.OK);
	}

	// 상품디테일페이지에서 주문하기 눌렀을때
	@PostMapping(value = "/itemDetail/orders")
	public @ResponseBody ResponseEntity itemDetailOrder(@RequestBody OrderDto orderDto, Principal principal) {


		System.out.println("------------------------------------------------------");
		System.out.println(orderDto.getD_address());
		System.out.println("------------------------------------------------------");

		

		Long orderId = orderService.order(orderDto, principal.getName());
		return new ResponseEntity<Long>(orderId, HttpStatus.OK);
	}
	
	// 장바구니 갯수 구해주기
	@PostMapping("/cartCount")
	public @ResponseBody ResponseEntity cartCount(Principal principal, Model model) {
		System.out.println("-------------------------");
		System.out.println("cartController부분에서 cart 눌렀을때 이동하는 구간입니다.");
		System.out.println(principal.getName());
		System.out.println("-------------------------");
		List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
		
		Long cartCount = (long)cartDetailList.size();
		
		return new ResponseEntity<Long>(cartCount, HttpStatus.OK);
		
	}

}
