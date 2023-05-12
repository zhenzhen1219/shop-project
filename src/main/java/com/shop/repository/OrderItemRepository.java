package com.shop.repository;


import com.shop.entity.Order;
import com.shop.entity.OrderItem;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
	
	@Query("select o from Order o " +
			"where o.member.email = :email " +
			"order by o.orderDate desc "
	)
	List<Order> findOrders(@Param("email") String email, Pageable pageable);
	
	@Query("select count(o) from Order o " +
			"where o.member.email = :email"
			)
	Long countOrder(@Param("email") String email);
}
