package com.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shop.dto.BoardSearchDto;
import com.shop.entity.Board;

public interface BoardRepositoryCustom {
	
	// 조회 조건을 담고있는 boardSearchDto 객체와 페이징 정보를 담고있는 pageable객체를 파라미터로 받는 메소드 
	Page<Board> getBoardListPage(BoardSearchDto boardSearchDto, Pageable pageable);
}
