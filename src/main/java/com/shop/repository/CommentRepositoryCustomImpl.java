package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.Role;
import com.shop.dto.CommentDto;
import com.shop.dto.MemberSearchDto;
import com.shop.entity.Comment;
import com.shop.entity.Member;
import com.shop.entity.QComment;
import com.shop.entity.QMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public CommentRepositoryCustomImpl(EntityManager em){
        this.queryFactory= new JPAQueryFactory(em);
    }




	@Override
	public Page<Comment> getCommentPage(Long itemId, Pageable pageable) {
		 QueryResults<Comment> results = queryFactory
	                .selectFrom(QComment.comment)
	                .where(QComment.comment.id.eq(itemId))
	                .orderBy(QComment.comment.regTime.desc())
	                .offset(pageable.getOffset())
	                .limit(pageable.getPageSize())
	                .fetchResults();

	        List<Comment> content = results.getResults();
	        for(int i=0; i<content.size(); i++) {
	        	System.out.println(content.get(i).getContent());
	        }
	        
	        
	        long total = results.getTotal();
	        System.out.println(total);
	        return new PageImpl<>(content,pageable,total);
	}
}
