package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.BoardDto;
import com.shop.dto.BoardFormDto;
import com.shop.dto.BoardImgDto;
import com.shop.dto.BoardSearchDto;
import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Board;
import com.shop.entity.BoardImg;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.entity.Member;
import com.shop.repository.BoardImgRepository;
import com.shop.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardImgService boardImgService;
    private final BoardImgRepository boardImgRepository;

    // 글 리스트 페이지에서 사용
    @Transactional( readOnly = true)
	public Page<Board> getBoardListPage(BoardSearchDto boardSearchDto, Pageable pageable){
		return boardRepository.getBoardListPage(boardSearchDto, pageable);
	}
    
    // 글 등록 페이지에서 사용 
    public Long saveBoard(BoardFormDto boardFormDto, MultipartFile boardImgFile) throws Exception{

        //상품 등록
    	Board board = boardFormDto.createBoard();
        Long id = boardRepository.save(board).getId();

        //이미지 등록
        BoardImg boardImg = new BoardImg();
        
        board.setId(id);
        boardImg.setBoard(board);
        boardImgService.saveBoardImg(boardImg, boardImgFile);
        
        return board.getId();

    }
    
	// 정보 조회
	@Transactional(readOnly = true)
	public BoardFormDto getBoardDtl(Long boardId){
		Board board = boardRepository.findById(boardId)
				.orElseThrow(EntityNotFoundException::new);
		BoardFormDto boardFormDto = BoardFormDto.of(board);
		return boardFormDto;
	}
	
	// 조회수
	@Transactional
	public int updateView(Long id) {
		return boardRepository.updateCount(id);
	}
	
    
    // 글 상세보기 페이지에서 사용 (257p 참고)
   	@Transactional(readOnly = true)
   	public BoardFormDto getBoardView(Long boardId) {
		
		Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);
		BoardFormDto boardFormDto = BoardFormDto.of(board);
   		
		BoardImg boardImg = boardImgRepository.findByBoardIdOrderByIdAsc(boardId);
		BoardImgDto boardImgDto = BoardImgDto.of(boardImg);
		boardFormDto.setBoardImgDto(boardImgDto);
				
   		return boardFormDto;
	}
   	
 	// 글 수정할 때
   	@Transactional(readOnly = true)
	public BoardFormDto getBoardUpdate(Long boardId) {
		BoardImg boardImg = boardImgRepository.findByBoardIdOrderByIdAsc(boardId);
			BoardImgDto boardImgDto = BoardImgDto.of(boardImg);		
		
		Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);
		BoardFormDto boardFormDto = BoardFormDto.of(board);
		boardFormDto.setBoardImgDto(boardImgDto);
		return boardFormDto;
	}
   	
   	public Long updateBoard(BoardFormDto boardFormDto, MultipartFile boardImgFile) throws Exception{

        //상품 수정
        Board board = boardRepository.findById(boardFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        board.updateBoard(boardFormDto);

        Long boardImgId = boardFormDto.getBoardImgId();

        //이미지 등록
        boardImgService.updateBoardImg(boardImgId, boardImgFile);
        
        return board.getId();
    }
   	
 // 특정 게시글 삭제
 	public void boardDelete(Long id) {
 		boardRepository.deleteById(id);
 	}
	
	

	
	
}
