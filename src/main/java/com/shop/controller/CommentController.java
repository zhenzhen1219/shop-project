package com.shop.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CommentDto;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Comment;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.service.CommentService;
import com.shop.service.ItemService;
import com.shop.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;
	
	// 댓글 삭제기능
	/*
	 * @GetMapping("/comment/delete/{id}") public String
	 * deleteMember(@PathVariable("id") Long id) {
	 * 
	 * commentService.deleteById(id);
	 * 
	 * return "redirect:/"; }
	 */
	
	
	
	/*
	 * @PostMapping("/comment/delete") public ResponseEntity<String>
	 * deleteMemberPost(@RequestParam("idx") Long id) {
	 * System.out.println("댓글 삭제입니다."); commentService.deleteById(id); return new
	 * ResponseEntity<String>(HttpStatus.OK); }
	 */
	
	
	
	
// 댓글 작성 후 DB저장되는 과정 ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

	
	Long ItemId1;
	
	@PostMapping("/board/comment/write")
	public ResponseEntity<String> insertComment(@RequestParam("content") String content,@RequestParam("itemId") Long ItemId, @RequestParam("star") int star, Principal principal, Model model) throws Exception{
		System.out.println("---------------------------");
		System.out.println(content);
		System.out.println(ItemId);
		System.out.println(star);
		
		
		
		System.out.println("---------------------------");
		
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setWriter(principal.getName());
		comment.setId(ItemId);
		comment.setStar(star);
		
		String email = principal.getName();
		commentService.CommentInsert(comment);
		
		ItemId1 = ItemId;
		return new ResponseEntity<String>(HttpStatus.OK);
		
	}
	

	@PostMapping("/comment/delete")
	public ResponseEntity<String> deleteMemberPost(@RequestParam("idx") Long id) {
		System.out.println("댓글 삭제입니다.");
		commentService.deleteById(id);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	
	
	@PostMapping("/comment/update")
	public ResponseEntity<String> updateComment(@RequestParam("idx") Long id,@RequestParam("content") String content ) {
		System.out.println("댓글 업데이트입니다.");
		commentService.updateComment(id, content);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	
	
	
	// 댓글 업데이트 기능
	/*
	 * @GetMapping("/comment/update/{id}/{content}") public String
	 * updateComment(@PathVariable("id") Long id, @PathVariable("content") String
	 * content) { System.out.println("댓글 업데이트입니다.");
	 * 
	 * // 변경된 회원 정보 저장 commentService.updateComment(id, content);
	 * 
	 * return "redirect:/"; }
	 * 
	 */

	
}
