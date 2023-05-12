package com.shop.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.entity.BoardImg;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long>{

    BoardImg findByBoardIdOrderByIdAsc(Long boardId);

	void deleteByBoardId(Long id);
    
}
