package com.shop.service;

import com.shop.constant.ItemTypeStatus;
import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.ItemRepositoryCustom;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	private final ItemImgService itemImgService;
	private final ItemImgRepository itemImgRepository;

	public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

		// 상품 등록
		Item item = itemFormDto.createItem();
	
		itemRepository.save(item);
		

		// 이미지 등록
		for (int i = 0; i < itemImgFileList.size(); i++) {
			ItemImg itemImg = new ItemImg();
			
			
			itemImg.setItem(item);
			if (i == 0) {
				itemImg.setRepimgYn("Y");

			} else {
				itemImg.setRepimgYn("N");

			}
			itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
		}
		return item.getId();

	}

	@Transactional(readOnly = true)
	public ItemFormDto getItemDto(Long itemId) {

		List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
		List<ItemImgDto> itemImgDtoList = new ArrayList<>();
		for (ItemImg itemImg : itemImgList) {
			ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
			itemImgDtoList.add(itemImgDto);
		}
		Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
		ItemFormDto itemFormDto = ItemFormDto.of(item);
		itemFormDto.setItemImgDtoList(itemImgDtoList);
		return itemFormDto;
	}

	public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

		// 상픔 수정
		Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
		item.updateItem(itemFormDto);

		List<Long> itemImgIds = itemFormDto.getItemImgIds();

		// 이미지 등록
		for (int i = 0; i < itemImgFileList.size(); i++) {
			itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
		}
		return item.getId();
	}

	@Transactional(readOnly = true)
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		return itemRepository.getAdminItemPage(itemSearchDto, pageable);
	}

	// 선영님
	@Transactional(readOnly = true)
	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		return itemRepository.getMainItemPage(itemSearchDto, pageable);
	}
	
	
	
	// 여기 수정입니다 아이템들 페이지 없애기
	@Transactional(readOnly = true)
	public List<MainItemDto> getMainItemPageEx(ItemSearchDto itemSearchDto) {
		System.out.println("-----------------------------");
		System.out.println(itemSearchDto.getItemTypeStatus());
		return itemRepository.getMainItemPageEx(itemSearchDto);
	}
	
	
	// 이미지 총 불러오게 하는법
	
	

	// 257 페이지
	@Transactional(readOnly = true)
	public ItemFormDto getItemDtl(Long itemId) {
		List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
		List<ItemImgDto> itemImgDtoList = new ArrayList<>();
		for (ItemImg itemImg : itemImgList) {
			ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
			itemImgDtoList.add(itemImgDto);
		}

		Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
		ItemFormDto itemFormDto = ItemFormDto.of(item);
		itemFormDto.setItemImgDtoList(itemImgDtoList);
		return itemFormDto;
	}
	
	// 카테고리 관련 
		// 상의 
		@Transactional(readOnly = true)
		public Page<MainItemDto> getItemTopPage(ItemSearchDto itemSearchDto, Pageable pageable){
			return itemRepository.getItemTopPage(itemSearchDto, pageable);
		}
		
		// 하의 
		@Transactional(readOnly = true)
		public Page<MainItemDto> getItemBottomPage(ItemSearchDto itemSearchDto, Pageable pageable){
			return itemRepository.getItemBottomPage(itemSearchDto, pageable);
		}
		
		// 신발 
		@Transactional(readOnly = true)
		public Page<MainItemDto> getItemShoesPage(ItemSearchDto itemSearchDto, Pageable pageable){
			return itemRepository.getItemShoesPage(itemSearchDto, pageable);
		}
		
		// 가방 
		@Transactional(readOnly = true)
		public Page<MainItemDto> getItemBagPage(ItemSearchDto itemSearchDto, Pageable pageable){
			return itemRepository.getItemBagPage(itemSearchDto, pageable);
		}

}
