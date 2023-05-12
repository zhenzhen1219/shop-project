package com.shop.repository;

import com.shop.constant.ItemTypeStatus;
import com.shop.dto.CommentDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Comment;
import com.shop.entity.Item;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

	Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

	Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

	List<MainItemDto> getMainItemPageEx(ItemSearchDto itemSearchDto);

	// 카테고리 관련
	Page<MainItemDto> getItemTopPage(ItemSearchDto itemSearchDto, Pageable pageable);

	Page<MainItemDto> getItemBottomPage(ItemSearchDto itemSearchDto, Pageable pageable);

	Page<MainItemDto> getItemShoesPage(ItemSearchDto itemSearchDto, Pageable pageable);

	Page<MainItemDto> getItemBagPage(ItemSearchDto itemSearchDto, Pageable pageable);

}
