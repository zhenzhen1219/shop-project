package com.shop.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.BoardDto;
import com.shop.dto.BoardFormDto;
import com.shop.dto.BoardSearchDto;
import com.shop.dto.ItemFormDto;
import com.shop.entity.Board;
import com.shop.entity.BoardImg;
import com.shop.service.BoardImgService;
import com.shop.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;
	private final BoardImgService boardImgService;
	
	//글 목록 	
	@GetMapping(value = {"/board/boardList", "board/boardList/{page}"}) // 페이지가 없는 경우 / 페이지가 있는 경우
    public String boardList(BoardSearchDto boardSearchDto,
    		@PathVariable("page") Optional<Integer> page, Model model) {
    	Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,5);
    	Page<Board> boards = boardService.getBoardListPage(boardSearchDto, pageable);
    	model.addAttribute("boards",boards);
    	model.addAttribute("boardSearchDto",boardSearchDto);
    	model.addAttribute("maxPage",5);
    	return "board/boardList";
    }
	
	//글 작성
	@GetMapping("/board/boardWriteForm")
	public String boardForm(Model model) {
		model.addAttribute("boardFormDto", new BoardFormDto());
		return "board/boardWriteForm";
	}
	
	@PostMapping(value = "/board/boardWriteForm")
	 public String boardNew(@Valid BoardFormDto boardFormDto, BindingResult
                           bindingResult, Model model, @RequestParam("boardImgFile")MultipartFile
                           boardImgFile){
        if (bindingResult.hasErrors()){
            return "board/boardWriteForm";
        }
        if (boardImgFile.isEmpty() && boardFormDto.getId()==null){
            model.addAttribute("errorMessage","이미지는 필수 입력 값 입니다.");
            return "board/boardWriteForm";
        }
        try{
            boardService.saveBoard(boardFormDto,boardImgFile);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생하였습니다");
            System.out.print(e.getMessage());
            return "board/boardWriteForm";
        }
        return "redirect:/board/boardList";
    }
	
	
	//글 상세보기
	@GetMapping("/board/boardView/{boardId}")
	public String boardView(Model model, @PathVariable("boardId") Long boardId) {
		BoardFormDto boardFormDto = boardService.getBoardView(boardId);
		boardService.updateView(boardId);
		model.addAttribute("board", boardFormDto);
		
		return "board/boardView";
	}
	
	// 글 수정
	@GetMapping(value = "/board/boardUpdateForm/{boardId}")
    public String boardUpdate(@PathVariable("boardId") Long boardId, Model model){
    	
        try{
            BoardFormDto boardFormDto = boardService.getBoardUpdate(boardId);
            model.addAttribute("boardFormDto", boardFormDto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 상품 입니다.");
            model.addAttribute("boardFormDto",new BoardFormDto());
            return "board/boardUpdateForm";
        }
        return "board/boardUpdateForm";
    }
	
	@PostMapping(value = "/board/boardUpdateForm/{boardId}")
    public String boardUpdate(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, @RequestParam("boardImgFile")
                             	MultipartFile boardImgFile, Model model,
                             	@PathVariable("boardId") Long boardId){
        if (bindingResult.hasErrors()){
        	return "redirect:/board/boardUpdateForm/" + boardId;
        }
        if (boardImgFile.isEmpty()&&boardFormDto.getId()==null){
            model.addAttribute("errorMessage","이미지는 필수 입력 값 입니다");
            return "redirect:/board/boardUpdateForm/" + boardId;
        }
        try{
            boardService.updateBoard(boardFormDto, boardImgFile);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 수정 중 에러가 발생하였습니다.");
            return "redirect:/board/boardUpdateForm/" + boardId;
        }
        return "redirect:/board/boardList";
    }
	 
	 @Transactional
	 @PostMapping("/board/delete")
		public String deletePost(@RequestParam final Long id) {
		 	boardImgService.boardImgDelete(id);
		 	boardService.boardDelete(id);
			return "redirect:/board/boardList";
		}

}
