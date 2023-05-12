package com.shop.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.Role;
import com.shop.dto.BoardSearchDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.Board;
import com.shop.entity.Item;
import com.shop.entity.QBoard;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;

//  BoardRepositoryCustom를 상속받음
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{
	
	// 동적으로 쿼리를 생성하기 위함
    private JPAQueryFactory queryFactory;

    // JPAQueryFactory의 생성자로 EntityManager 객체를 넣어줌
    public BoardRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    // searchDateType에 따라서 datetime의 값을 이전 시간의 값으로 세팅 후 해당 시간 이후로 등록된 글만 조회 
    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if (StringUtils.equals("1d",searchDateType)) {
            dateTime=dateTime.minusDays(1);
        }else if (StringUtils.equals("1w",searchDateType)) {
            dateTime=dateTime.minusWeeks(1);
        }else if (StringUtils.equals("1m",searchDateType)) {
            dateTime=dateTime.minusMonths(1);
        }else if (StringUtils.equals("6m",searchDateType)) {
            dateTime=dateTime.minusMonths(6);
        }
        return  QBoard.board.regTime.after(dateTime);
    }

    // searchBy의 값에 따라서 검색어를 포함하고 있는 작성자, 제목을 조회
    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if (StringUtils.equals("title",searchBy)){
            return QBoard.board.title.like("%"+searchQuery+"%");
        }else if(StringUtils.equals("createdBy",searchBy)){
            return QBoard.board.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }
    
    // queryfactory를 통해 쿼리 생성 
    @Override
    public Page<Board> getBoardListPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        QueryResults<Board>results=queryFactory
                .selectFrom(QBoard.board)
                .where(regDtsAfter(boardSearchDto.getSearchDateType()),
                        searchByLike(boardSearchDto.getSearchBy(),
        boardSearchDto.getSearchQuery()))
                .orderBy(QBoard.board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Board> content=results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }
    
}
