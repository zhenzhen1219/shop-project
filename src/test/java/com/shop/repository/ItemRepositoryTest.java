package com.shop.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
public class ItemRepositoryTest {
	
	@Autowired
	ItemRepository itemRepository;
	
	//  연속성context
	@PersistenceContext
	EntityManager em;
	
//	@Test
//	@DisplayName("상품 저장 테스트")
//	public void createItemTest() {
//		Item item = new Item();
//		item.setItemNm("테스트 상품");
//		item.setPrice(1000);
//		item.setItemDetail("테스트 상품 상세 설명");
//		item.setItemSellStatus(ItemSellStatus.SELL);
//		item.setStockNumber(100);
//		item.setRegTime(LocalDateTime.now());
//		item.setUpdateTime(LocalDateTime.now());
//		Item savedItem = itemRepository.save(item);
//		System.out.println(savedItem.toString());
//	}
	
	public void createItemList() {
		for(int i=0; i<10; i++) {
			Item item = new Item();
			item.setItemNm("테스트 상품" + i);
			item.setPrice(1000 + i);
			item.setItemDetail("테스트 상품 상세 설명" + i);
			item.setItemSellStatus(ItemSellStatus.SELL);
			item.setStockNumber(100);
			item.setRegTime(LocalDateTime.now());
			item.setUpdateTime(LocalDateTime.now());
			Item savedItem = itemRepository.save(item);
		}
	}
	
//	@Test
//	@DisplayName("상품명 조회 테스트")
//	public void findByItemNmTest() {
//		this.createItemList();
//		
//		List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
////		List<Item> itemList = itemRepository.findAll();
//		for(Item item : itemList) {
//			System.out.println(item.toString());
//		}
//		
//	}
//	
//	@Test
//	@DisplayName("상품명, 상품상세설명 or 테스트")
//	public void findByItemNmOrItemDetail() {
//		this.createItemList();
//		
//		List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트상품1", "테스트 상품 상세 설명5");
//		for(Item item : itemList) {
//			System.out.println(item.toString());
//		}
//		
//	}
	
	
//	@Test
//	@DisplayName("가격 GreaterThan 테스트")
//	public void findByPriceGreaterThanTest() {
//		this.createItemList();
//		
//		List<Item> itemList = itemRepository.findByPriceGreaterThan(1005);
//		for(Item item : itemList) {
//			System.out.println(item.toString());
//		}
//		
//	}
	
	
//	@Test
//	@DisplayName("가격 내림차순 조회 테스트")
//	public void findByPriceGreaterThanByPriceDescTest() {
//		
//		this.createItemList();
//		List<Item> itemList = itemRepository.findByPriceGreaterThanOrderByPriceDesc(1005);
//		for(Item item : itemList) {
//			System.out.println(item.toString());
//		}
//		
//	}
	
	
//	@Test
//	@DisplayName("id가 3이하인 데이터 조회")
//	public void findByIdLessThanEqualTest() {
//		
//		this.createItemList();
//		List<Item> itemList = itemRepository.findByIdLessThanEqual(3L);
//		for(Item item : itemList) {
//			System.out.println(item.toString());
//		}
//		
//	}
	
	
	
//	
//	@Test
//	@DisplayName("상품 상세 설명 중 7이 포함된 데이터만 조회")
//	public void findByItemDetailLikeTest() {
//		
//		this.createItemList();
//		List<Item> itemList = itemRepository.findByItemDetailLike("%7");
//		for(Item item : itemList) {
//			System.out.println(item.toString());
//		}
//		
//	}
	
	
	
	
//	@Test
//	@DisplayName("id가 5~7 사이인 데이터를 id를 오름차순 정렬")
//	public void findByIdLessThanAndIdGreaterThan() {
//		this.createItemList();
//		List<Item> itemList = itemRepository.findByIdLessThanAndIdGreaterThan(7, 5);
//		for(Item item : itemList) {
//			System.out.println(item.toString());
//		}
//	}
	
	
//	@Test
//	@DisplayName("id가 5~7 사이인 데이터를 id를 오름차순 정렬")
//	public void findByIdBetweenOrderByItemIdAscTest() {
//		this.createItemList();
//		List<Item> itemList = itemRepository.findByIdBetweenOrderByIdAsc(5L,7L);
//		for(Item item : itemList) {
//			System.out.println(item.toString());
//		}
//	}
	
	
//	@Test
//	@DisplayName("@Query를 이용한 상품 조회 테스트")
//	public void findByItemDetailTest() {
//		this.createItemList();
//		List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
//		for(Item item : itemList) {
//			System.out.println(item.toString());
//		}
//	}
	
	
	@Test
	@DisplayName("NativeQuery 속성을 이용한 상품 조회 테스트")
	public void findByItemDetailByNativeTest() {
		this.createItemList();
		
		List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	
//	@Test
//	@DisplayName("Querydsl 조회 테스트1")
//	public void queryDslTest() {
//		this.createItemList();
//		
//		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
//		QItem qItem = QItem.item;
//		JPAQuery<Item> query = queryFactory.selectFrom(qItem)
//		.where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
//		.where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명"+ "%"))
//		.orderBy(qItem.price.desc());
//		
//		// fetch() - 조회 결과 리스트 반환
//		List<Item> itemList = query.fetch();
//		
//		for(Item item : itemList) {
//			System.out.println(item.toString());
//		}
//		
//	}
	
	public void createItemList2() {
		for(int i=0; i<=5; i++) {
			Item item = new Item();
			item.setItemNm("테스트 상품" + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세 설명" + i);
			item.setItemSellStatus(ItemSellStatus.SELL);
			item.setStockNumber(100);
			item.setRegTime(LocalDateTime.now());
			item.setUpdateTime(LocalDateTime.now());
			itemRepository.save(item);
		}
		
		for(int i=6; i<=10; i++) {
			Item item = new Item();
			item.setItemNm("테스트 상품" + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세 설명" + i);
			item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
			item.setStockNumber(0);
			item.setRegTime(LocalDateTime.now());
			item.setUpdateTime(LocalDateTime.now());
			itemRepository.save(item);
		}
	}
	
	@Test
	@DisplayName("상품 Querydsl 조회 테스트 2")
	public void queryDslTest2() {
		this.createItemList2();
		
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		QItem item = QItem.item;
		
		String itemDetail = "테스트 상품 상세 설명";
		int price = 10003;
		String itemSellStat = "SELL";
		
		booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
		booleanBuilder.and(item.price.gt(price));
		
		if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
			booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
		}
		
		// 보여줄 행의 갯수 제한 = limit
		Pageable pageable = PageRequest.of(0, 5);
		Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
		
		System.out.println("total elements: " + itemPagingResult.getTotalElements());
		
		List<Item> resultItemList = itemPagingResult.getContent();
		for(Item resultItem : resultItemList) {
			System.out.println(resultItem.toString());
		}
		
	
		
	}
	
}
