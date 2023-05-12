package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.constant.ItemTypeStatus;
import com.shop.dto.CommentDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.MainItemDto2;
import com.shop.dto.QMainItemDto;
import com.shop.dto.QMainItemDto2;
import com.shop.entity.Comment;
import com.shop.entity.Item;
import com.shop.entity.QComment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
	
    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

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
        return  QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if (StringUtils.equals("itemNm",searchBy)){
            return QItem.item.itemNm.like("%"+searchQuery+"%");
        }else if(StringUtils.equals("createdBy",searchBy)){
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }
    
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QueryResults<Item>results=queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
        itemSearchDto.getSearchQuery()))
                
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Item> content=results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }
    
    // 선영
    
    private BooleanExpression itemNmLike(String searchQuery) {
		
		return StringUtils.isEmpty(searchQuery) ?
				null : QItem.item.itemNm.like("%" + searchQuery + "%");
	
    }
    
    
    
    // 수진
    private BooleanExpression itemTypeStatusEq(ItemTypeStatus itemTypeStatus){
        return itemTypeStatus == null ? null : QItem.item.itemTypeStatus.eq(itemTypeStatus);
    }
    
   
	@Override
	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		QItem item = QItem.item;
		QItemImg itemImg = QItemImg.itemImg;
		
		
//		JPAQueryFactory queryFactory = new JPAQueryFactory();
		
		QueryResults<MainItemDto> results = queryFactory
				.select(new QMainItemDto(
						item.id,
						item.itemNm,
						item.itemDetail,
						itemImg.imgUrl,
						item.price
						
			
						)
				)
				.from(itemImg)
				.join(itemImg.item, item)
				.where(itemImg.repimgYn.eq("Y"))
				.where(itemNmLike(itemSearchDto.getSearchQuery()))
				.orderBy(item.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		
		List<MainItemDto> content = results.getResults();
		long total = results.getTotal();
		return new PageImpl<>(content, pageable, total);
	}

	
	// 여기가 작업하는 곳이얌
	@Override
	public List<MainItemDto> getMainItemPageEx(ItemSearchDto itemSearchDto) {
		System.out.println(itemSearchDto.getItemTypeStatus());
		
		QItem item = QItem.item;
		QItemImg itemImg = QItemImg.itemImg;
		
		
//		JPAQueryFactory queryFactory = new JPAQueryFactory();
		
		QueryResults<MainItemDto> results = queryFactory
				.select(new QMainItemDto(
						item.id,
						item.itemNm,
						item.itemDetail,
						itemImg.imgUrl,
						item.price
						
			
						)
				)
				.from(itemImg)
				.join(itemImg.item, item)
				.where(itemImg.repimgYn.eq("Y"))
				//.where(itemTypeStatusEq(ItemTypeStatus.BAG))
				.where(itemTypeStatusEq(itemSearchDto.getItemTypeStatus()))
				.where(itemNmLike(itemSearchDto.getSearchQuery()))
				
				
				
				.orderBy(item.id.desc())
				.fetchResults();
		
		List<MainItemDto> content = results.getResults();
		// long total = results.getTotal();
		return content;
	}
	
	
	// 카테고리 관련
		// 상의
		@Override
		public Page<MainItemDto> getItemTopPage(ItemSearchDto itemSearchDto, Pageable pageable) {
			QItem item = QItem.item;
			QItemImg itemImg = QItemImg.itemImg;
			
//			JPAQueryFactory queryFactory = new JPAQueryFactory();
			
			QueryResults<MainItemDto> results = queryFactory
					.select(new QMainItemDto(
							item.id,
							item.itemNm,
							item.itemDetail,
							itemImg.imgUrl,
							item.price)
					)
					.from(itemImg)
					.join(itemImg.item, item)
					.where(itemImg.repimgYn.eq("Y"))
					.where(item.itemTypeStatus.eq(ItemTypeStatus.TOP))
					.where(itemNmLike(itemSearchDto.getSearchQuery()))
					.orderBy(item.id.desc())
					.offset(pageable.getOffset())
					.limit(pageable.getPageSize())
					.fetchResults();
			
			List<MainItemDto> content = results.getResults();
			long total = results.getTotal();
			return new PageImpl<>(content, pageable, total);
		}
		
		// 하의
		@Override
		public Page<MainItemDto> getItemBottomPage(ItemSearchDto itemSearchDto, Pageable pageable) {
			QItem item = QItem.item;
			QItemImg itemImg = QItemImg.itemImg;
			
//				JPAQueryFactory queryFactory = new JPAQueryFactory();
			
			QueryResults<MainItemDto> results = queryFactory
					.select(new QMainItemDto(
							item.id,
							item.itemNm,
							item.itemDetail,
							itemImg.imgUrl,
							item.price)
					)
					.from(itemImg)
					.join(itemImg.item, item)
					.where(itemImg.repimgYn.eq("Y"))
					.where(item.itemTypeStatus.eq(ItemTypeStatus.BOTTOMS))
					.where(itemNmLike(itemSearchDto.getSearchQuery()))
					.orderBy(item.id.desc())
					.offset(pageable.getOffset())
					.limit(pageable.getPageSize())
					.fetchResults();
			
			List<MainItemDto> content = results.getResults();
			long total = results.getTotal();
			return new PageImpl<>(content, pageable, total);
		}
		
		// 신발
		@Override
		public Page<MainItemDto> getItemShoesPage(ItemSearchDto itemSearchDto, Pageable pageable) {
			QItem item = QItem.item;
			QItemImg itemImg = QItemImg.itemImg;
			
//				JPAQueryFactory queryFactory = new JPAQueryFactory();
			
			QueryResults<MainItemDto> results = queryFactory
					.select(new QMainItemDto(
							item.id,
							item.itemNm,
							item.itemDetail,
							itemImg.imgUrl,
							item.price)
					)
					.from(itemImg)
					.join(itemImg.item, item)
					.where(itemImg.repimgYn.eq("Y"))
					.where(item.itemTypeStatus.eq(ItemTypeStatus.SHOES))
					.where(itemNmLike(itemSearchDto.getSearchQuery()))
					.orderBy(item.id.desc())
					.offset(pageable.getOffset())
					.limit(pageable.getPageSize())
					.fetchResults();
			
			List<MainItemDto> content = results.getResults();
			long total = results.getTotal();
			return new PageImpl<>(content, pageable, total);
		}
		
		// 가방
		@Override
		public Page<MainItemDto> getItemBagPage(ItemSearchDto itemSearchDto, Pageable pageable) {
			QItem item = QItem.item;
			QItemImg itemImg = QItemImg.itemImg;
			
//					JPAQueryFactory queryFactory = new JPAQueryFactory();
			
			QueryResults<MainItemDto> results = queryFactory
					.select(new QMainItemDto(
							item.id,
							item.itemNm,
							item.itemDetail,
							itemImg.imgUrl,
							item.price)
					)
					.from(itemImg)
					.join(itemImg.item, item)
					.where(itemImg.repimgYn.eq("Y"))
					.where(item.itemTypeStatus.eq(ItemTypeStatus.BAG))
					.where(itemNmLike(itemSearchDto.getSearchQuery()))
					.orderBy(item.id.desc())
					.offset(pageable.getOffset())
					.limit(pageable.getPageSize())
					.fetchResults();
			
			List<MainItemDto> content = results.getResults();
			long total = results.getTotal();
			return new PageImpl<>(content, pageable, total);
		}

//		@Override
//		public List<MainItemDto> getMainItemPageEx(ItemSearchDto itemSearchDto, String itemType) {
//			// TODO Auto-generated method stub
//			return null;
//		}
	
	


	
}
