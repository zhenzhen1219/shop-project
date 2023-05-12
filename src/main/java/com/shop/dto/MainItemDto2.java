package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto2 {
	// 메인 페이지에서 상품을 보여줄 때 사용할 Dto 클래스

	/*
	 * private Long id1; private Long id2; private Long id3; private Long id4;
	 * private Long id5;
	 * 
	 * private String itemNm1; private String itemNm2; private String itemNm3;
	 * private String itemNm4; private String itemNm5;
	 * 
	 * private String itemDetail1; private String itemDetail2; private String
	 * itemDetail3; private String itemDetail4; private String itemDetail5;
	 * 
	 * private String imgUrl1; private String imgUrl2; private String imgUrl3;
	 * private String imgUrl4; private String imgUrl5;
	 * 
	 * MainItemImgDto mainItemImgDto;
	 * 
	 * private Integer price1; private Integer price2; private Integer price3;
	 * private Integer price4; private Integer price5;
	 */
	

	private String imgUrl1;
	private String imgUrl2;
	private String imgUrl3;
	private String imgUrl4;
	private String imgUrl5;

	
	
	
	
	@QueryProjection
	public MainItemDto2(String imgUrl1, String imgUrl2, String imgUrl3, String imgUrl4, String imgUrl5) {
	
		this.imgUrl1 = imgUrl1;
		this.imgUrl2 = imgUrl2;
		this.imgUrl3 = imgUrl3;
		this.imgUrl4 = imgUrl4;
		this.imgUrl5 = imgUrl5;
		
	

	}
	
	
	
	
	
	

//	@QueryProjection
//	public MainItemDto(Long id1, Long id2, Long id3, Long id4, Long id5, String itemNm1, String itemNm2, String itemNm3,
//			String itemNm4, String itemNm5, String itemDetail1, String itemDetail2, String itemDetail3,
//			String itemDetail4, String itemDetail5, String imgUrl1, String imgUrl2, String imgUrl3, String imgUrl4,
//			String imgUrl5, Integer price1, Integer price2, Integer price3, Integer price4, Integer price5) {
//		this.id1 = id1;
//		this.id2 = id2;
//		this.id3 = id3;
//		this.id4 = id4;
//		this.id5 = id5;
//
//		this.itemNm1 = itemNm1;
//		this.itemNm2 = itemNm2;
//		this.itemNm3 = itemNm3;
//		this.itemNm4 = itemNm4;
//		this.itemNm5 = itemNm5;
//
//		this.itemDetail1 = itemDetail1;
//		this.itemDetail2 = itemDetail2;
//		this.itemDetail3 = itemDetail3;
//		this.itemDetail4 = itemDetail4;
//		this.itemDetail5 = itemDetail5;
//
//		this.imgUrl1 = imgUrl1;
//		this.imgUrl2 = imgUrl2;
//		this.imgUrl3 = imgUrl3;
//		this.imgUrl4 = imgUrl4;
//		this.imgUrl5 = imgUrl5;
//
//		this.price1 = price1;
//		this.price2 = price2;
//		this.price3 = price3;
//		this.price4 = price4;
//		this.price5 = price5;
//
//	}
}
