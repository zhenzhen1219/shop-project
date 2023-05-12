package com.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shop.dto.CommentDto;
import com.shop.entity.Comment;
import com.shop.entity.Member;


public interface CommentRepository extends JpaRepository<Comment, Long>,
QuerydslPredicateExecutor<Comment>,CommentRepositoryCustom{
	@Query("select new com.shop.dto.CommentDto(c.id, c.content, c.writer) from Comment c where c.id = :id ")
	List<CommentDto> finByItemId(Long id);
	
	Comment findById(String id);
	
	
	@Modifying
	@Query("UPDATE Comment com set com.content = :content where com.idx = :id")
	void updateComment(Long id, String content);
	
	
	
}
