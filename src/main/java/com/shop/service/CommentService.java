package com.shop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CommentDto;
import com.shop.dto.MemberSearchDto;
import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Comment;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.CommentRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService{

	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;

	public void CommentInsert(Comment comment) {

		commentRepository.save(comment);

	}

	@Transactional(readOnly = true)
	public List<CommentDto> getCommentList(Long id) {

		List<CommentDto> commentDtoList = new ArrayList<>();

		commentDtoList = commentRepository.finByItemId(id);

		return commentDtoList;
	}

	@Transactional(readOnly = true)
	public Page<Comment> getCommentListPage(Long itemId, Pageable pageable) {
		return commentRepository.getCommentPage(itemId, pageable);
	}

	// 댓글 삭제
	public void deleteById(Long id) {
		commentRepository.deleteById(id);
	}
	
	// 조회
	public Comment findById(Long id) {
		Optional<Comment> optionalComment = commentRepository.findById(id);
		if (optionalComment.isPresent()) {
			return optionalComment.get();
		}
		throw new NoSuchElementException("해당 정보를 찾을 수 없습니다.");
	}
	
	
	// 업데이트
	public void updateComment(Long id, String content) {
		commentRepository.updateComment(id, content);
	}

	

}
