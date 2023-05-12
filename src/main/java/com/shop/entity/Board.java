package com.shop.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.shop.dto.BoardFormDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="board")
@Getter
@Setter
@ToString
public class Board extends BaseEntity {
	
	@Id
	@Column(name="board_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;			// 게시글 번호 
	
	private String title;		// 글 제목
	
	@Column(nullable = false, length = 2000)
	private String content;		// 글 내용
	
	private int readCount;		// 조회수
	
	@CreationTimestamp
	private LocalDateTime regDate; // 등록시간
	
	@UpdateTimestamp
	private LocalDateTime updateDate; // 수정시간
	
	public void updateBoard(BoardFormDto boardFormDto){
		this.title = boardFormDto.getTitle();
		this.content = boardFormDto.getContent();
	}
	
}
