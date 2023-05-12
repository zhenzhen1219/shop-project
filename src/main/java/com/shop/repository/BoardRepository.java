package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shop.entity.Board;



// JpaRepository<첫번째 Entity요소 , 두번째 기본키 적어주기(id 타입 long)
// JpaRepository - 기본적인 CRUD 및 페이징 처리를 위한 메소드가 정의되어 있음
public interface BoardRepository extends JpaRepository<Board, Long>,
QuerydslPredicateExecutor<Board>, BoardRepositoryCustom {
		
	// 데이터 조회
	// find + (엔티티 이름) + By + 변수이름
	List<Board> findById(String id);

	public List<Board> findAllByOrderByIdDesc();
	
	
	@Modifying
	@Query("update Board p set p.readCount = p.readCount + 1 where p.id = :id")
	int updateCount(@Param("id") Long id);
}
