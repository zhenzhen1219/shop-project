package com.shop.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;

import com.shop.entity.Board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardFormDto {

    private Long id;
    
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;
    
    @NotNull(message = "내용은 필수 입력 값입니다.")
    private String content;

    private String readCount;
    
    private LocalDateTime regDate;
	
	private LocalDateTime updateDate;
	
	private String createdBy;
	
	private BoardImgDto boardImgDto = new BoardImgDto();

	private Long boardImgId;
	
	 public Long getBoardImgId() {
	        return boardImgId;
	    }
    private static ModelMapper modelMapper = new ModelMapper();

    public Board createBoard(){
        return modelMapper.map(this,Board.class);
    }

    
    public static BoardFormDto of(Board board){
        return modelMapper.map(board, BoardFormDto.class);
    }
}
