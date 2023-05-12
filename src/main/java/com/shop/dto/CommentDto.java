package com.shop.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.shop.entity.Item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDto {
	
	private int idx;
	
	private Long id;
	
	
	private String content;


	private String writer;
	private LocalDateTime regTime;
	
	private LocalDateTime updateTime;

	
	private List<CommentDto> commentDtoList;
	
	
	public CommentDto(Long id, String content, String writer) {
			this.id = id;
			this.content = content;
			this.writer = writer;
		
//			this.deliveryStatus = deliveryStatus;
		}
}
