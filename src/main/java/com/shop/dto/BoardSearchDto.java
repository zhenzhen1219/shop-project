package com.shop.dto;

import com.shop.constant.Role;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardSearchDto {
	// 현재 시간과 작성일을 비교해서 상품 조회 
    private String searchDateType;

    // 관리자 / 일반회원 기준으로 조회
    private Role searchRole;
    
    // 상품을 조회할 떄의 유형 ( 작성자, 글제목)
    private String searchBy;

    // 조회할 검색어 저장 변수 
    private String searchQuery="";
    
}
