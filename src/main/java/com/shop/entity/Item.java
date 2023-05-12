package com.shop.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.web.multipart.MultipartFile;

import com.shop.constant.ItemSellStatus;
import com.shop.constant.ItemTypeStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {
	
	@Id
	@Column(name="item_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;			// 상품 코드

	@Column(nullable = false, length = 50)
	private String itemNm;		// 상품명
	
	@Column(name="price", nullable = false)
	private int price;			// 가격
	
	@Column(nullable = false)
	private int stockNumber;	// 재고수량
	
	@Lob
	@Column(nullable = false)
	private String itemDetail;	// 상품 상세 설명
	
	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemSellStatus;	// 상품 판매 상태
	
	@Enumerated(EnumType.STRING)
	private ItemTypeStatus itemTypeStatus;  // 상품 종류 / DB에선 top(상의), bottoms(하의), 신발(shoes), 가방(bag)이 출력.
	
	private LocalDateTime regTime;			// 등록시간
	
	private LocalDateTime updateTime;		// 수정 시간
	
	
	
	

	public void updateItem(ItemFormDto itemFormDto){
		this.itemNm=itemFormDto.getItemNm();
		this.price=itemFormDto.getPrice();
		this.stockNumber=itemFormDto.getStockNumber();
		this.itemDetail= itemFormDto.getItemDetail();
		this.itemSellStatus=itemFormDto.getItemSellStatus();
		this.itemTypeStatus=itemFormDto.getItemTypeStatus();
	}
	
	
	public void addStock(int stockNumber) {
		this.stockNumber += stockNumber;
	}
	
	public void removeStock(int stockNumber) {
		
		int restStock = this.stockNumber - stockNumber;
		if(restStock<0) {
			throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량 : "+ this.stockNumber + ")");
		}
		this.stockNumber = restStock;
	}
}
