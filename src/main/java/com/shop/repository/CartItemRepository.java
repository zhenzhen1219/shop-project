package com.shop.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shop.dto.CartDetailDto;
import com.shop.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	CartItem findByCartIdAndItemIdAndSize(Long cartId, Long itemId, String size);
	// CartItem findByCartIdAndCartItemId(Long cartId, Long cartItemId);
	  
	// CartItem findCartItemById(Long cartItemId);
	
	  @Query("SELECT new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl, ci.size) " + 
			  "from CartItem ci, ItemImg im " + 
			  "join ci.item i " +
			  "where ci.cart.id = :cartId " + 
			  "and im.item.id = ci.item.id " +
			  "and im.repimgYn = 'Y' " + 
			  "order by ci.regTime desc") 
	  List<CartDetailDto> findCartDetailDtoList(Long cartId);
	  
	  
	  @Query("SELECT new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl, ci.size) " + 
			  "from CartItem ci, ItemImg im " + 
			  "join ci.item i " +
			  "where ci.cart.id = :cartId " + 
			  "and ci.id = :cartItemId " +
			  "and im.item.id = ci.item.id " +
			  "and im.repimgYn = 'Y' " + 
			  "order by ci.regTime desc") 
	  CartDetailDto findCartDetailDtoListt(Long cartId, Long cartItemId);
	  
	  
	  
	  @Query("SELECT new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl, ci.size) " + 
			  "from CartItem ci, ItemImg im " + 
			  "join ci.item i " +
			  "where ci.cart.id = :cartId " + 
			  "and ci.id = :cartItemId " +
			  "and im.item.id = ci.item.id " +
			  "and im.repimgYn = 'Y' " + 
			  "order by ci.regTime desc") 
	 List<CartDetailDto> findCartDetailDtoListtt(Long cartId, Long cartItemId);
	  
	  
	  
	
	
			
}
