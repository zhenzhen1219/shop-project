package com.shop.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shop.dto.CommentDto;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Item;
import com.shop.entity.Member;

// JpaRepository<첫번째 Entity요소 , 두번째 기본키 적어주기(id 타입 long)
// JpaRepository - 기본적인 CRUD 및 페이징 처리를 위한 메소드가 정의되어 있음
public interface MemberRepository
		extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member>, MemberRepositoryCustom {

// 중복회원 검색 위해 email로 회원 정보 조회
	Member findByEmail(String email);

	Member findByName(String name);

	Member findById(String id);

	MemberFormDto getMemberByEmail(String email);
	
	
	@Query("select m.email from Member m where m.name = :name ")
	String getMemberId(String name);
	
	
	@Query("select m.email from Member m where m.name = :name and m.email = :email ")
	String getMemberIdName(String name, String email);
	
	
	@Transactional
	@Modifying
	@Query("update Member m set m.password = :password where m.email = :email ")
	void updatePw(String email, String password);
	

}