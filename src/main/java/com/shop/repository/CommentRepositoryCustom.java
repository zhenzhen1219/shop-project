package com.shop.repository;

import com.shop.dto.CommentDto;
import com.shop.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    Page<Comment> getCommentPage(Long itemId, Pageable pageable);
}
