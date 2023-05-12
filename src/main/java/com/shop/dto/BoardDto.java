package com.shop.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.modelmapper.ModelMapper;

import com.shop.entity.Board;
import com.shop.entity.Item;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BoardDto {
	
	private Long id;
	
	private String title;
	
	private String content;
	
	private String readCount;
	
	private LocalDateTime regDate;
	
	private LocalDateTime updateDate;

}
