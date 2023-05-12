package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shop.entity.Item;



// JpaRepository<첫번째 Entity요소 , 두번째 기본키 적어주기(id 타입 long)
// JpaRepository - 기본적인 CRUD 및 페이징 처리를 위한 메소드가 정의되어 있음
public interface ItemRepository extends JpaRepository<Item, Long>,
QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
		
	// 데이터 조회
	// find + (엔티티 이름) + By + 변수이름
	List<Item> findByItemNm(String itemNm);
	
	List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
	
	
	
	// >=
	List<Item> findByPriceGreaterThan(int price);
	
	List<Item> findByPriceGreaterThanOrderByPriceDesc(int price);
	
	List<Item> findByIdLessThanEqual(long id);
	
	List<Item> findByItemDetailLike(String itemDetail);
	
//	List<Item> findByIdLessThanAndIdGreaterThan(long id, long id1);
	
	List<Item> findByIdBetweenOrderByIdAsc(Long start_id1, Long End_id2);
	
	// @Query - JPQL(Java Persistence Query Language)
//	@Query("SELECT i FROM Item i WHERE i.itemDetail LIKE "
//		   + "%:itemDetail% ORDER BY i.price DESC" )
//	List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
//	
	
	//Native Query
	@Query(value="SELECT * FROM item i WHERE i.item_detail LIKE "
			   + "%:itemDetail% ORDER BY i.price DESC", nativeQuery = true)
		List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
	
	
}
