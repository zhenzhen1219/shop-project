package com.shop.controller;

import com.shop.constant.ItemTypeStatus;
import com.shop.dto.CommentDto;
import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Comment;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.service.CommentService;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CommentService commentService;
    
    @GetMapping("/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
    }

    
    // 내가 작성하는 구간
    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult
                           bindingResult, Model model, @RequestParam("itemImgFile")List<MultipartFile>
                           itemImgFileList){
        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId()==null){
            model.addAttribute("errorMessage","첫번째 상픔 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }
        try{
            itemService.saveItem(itemFormDto,itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생하였습니다");
            System.out.print(e.getMessage());
            return "item/itemForm";
        }
        return "redirect:/";
    }
    

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){
    	
        try{
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto",itemFormDto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto",new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile")
                             List<MultipartFile> itemImgFileList, Model model){
        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty()&&itemFormDto.getId()==null){
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수 입력 값 입니다");
            return "item/itemForm";
        }
        try{
            itemService.updateItem(itemFormDto, itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }
    
    
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto,
    		@PathVariable("page") Optional<Integer> page, Model model) {
    	Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,3);
    	Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
    	model.addAttribute("items",items);
    	model.addAttribute("itemSearchDto",itemSearchDto);
    	model.addAttribute("maxPage",5);
    	return "item/itemMng";
    }
    


    
	
    @GetMapping(value = {"/item/{itemId}/{move}", "/item/{itemId}/{page}"})
	public String itemDtl1(Model model, @PathVariable("itemId") Long itemId, @PathVariable("page") Optional<Integer> page,ItemSearchDto itemSearchDto, @PathVariable("move") String move) {
		ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
		 
		Page<Comment> comments = commentService.getCommentListPage(itemId, pageable);
	
		
		Pageable pageable2 = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
		Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable2);
		
		model.addAttribute("items", items);
		model.addAttribute("move", move);
		model.addAttribute("item", itemFormDto);
		model.addAttribute("comment",comments);
		model.addAttribute("maxPage", 100);
		
		return "item/itemDtl";
	}
    
    
   
    
    
    
    
    // 여기서 다시 작업할꺼임;;;
    
    @PostMapping(value = {"/item/{itemId}", "/item/{itemId}/{page}"})
   	public String itemDtlPost(Model model, @PathVariable("itemId") Long itemId, @PathVariable("page") Optional<Integer> page,ItemSearchDto itemSearchDto ) {
   		ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
   		
   		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
   		 
   		Page<Comment> comments = commentService.getCommentListPage(itemId, pageable);
   		System.out.println("------------------------------");
   		System.out.println("aaaaaaaaaaaaaaaaaaaaaa "+itemId);
   		System.out.println("------------------------------");
   		//List<CommentDto> commentDtoList = commentService.getCommentList(itemId);
   		
   		// 여기에다 댓글 추가할꺼임
   		
   		
   		// List<CommentDto> commentDtoList = commentService.getCommentList(itemId);
   		
   		//if(commentDtoList == null) {
   			
   		//}else {
   			
   		//}
   		
   		Pageable pageable2 = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
   		Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable2);
   		
   		model.addAttribute("items", items);
   	
   		model.addAttribute("item", itemFormDto);
   		model.addAttribute("comment",comments);
   		model.addAttribute("maxPage", 100);
   		
   		return "item/itemDtl";
   	}
    
    
    
    
    
    
    
    
//    @GetMapping("/item/items")
//   	public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
//   		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6 );
//   		Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
//   									model.addAttribute("items", items);
//   									model.addAttribute("itemSearchDto", itemSearchDto);
//   									model.addAttribute("maxPage", 5);
//   		return "item/items";
//   	}
    
    
  
    // 여기 원본임
//    @GetMapping("/item/items")
//   	public String main(ItemSearchDto itemSearchDto, Model model) {
//   	
//   		List<MainItemDto> items = itemService.getMainItemPageEx(itemSearchDto);
//   									model.addAttribute("items", items);
//   									model.addAttribute("itemSearchDto", itemSearchDto);
//   								
//   		return "item/items";
//   	}
    

    @GetMapping(value={"/item/items", "/item/items/{itemType}"})
	public String main(ItemSearchDto itemSearchDto, Model model, @RequestParam(required = false, value = "itemType") String itemType) {
    	System.out.println("-------------------");
    	System.out.println("ItemType 값체크 " + itemType);
    	
    	if(itemType != null) {
    		ItemTypeStatus itemTypeStatus = ItemTypeStatus.valueOf(itemType);	
    		itemSearchDto.setItemTypeStatus(itemTypeStatus);
    	}
    	
    	
		List<MainItemDto> items = itemService.getMainItemPageEx(itemSearchDto);
									model.addAttribute("items", items);
									model.addAttribute("itemSearchDto", itemSearchDto);
								
		return "item/items";
	}
    
    
    
    
    
    
    
 // 카테고리 관련 
    // 상의
    @GetMapping("/item/cate/top")
   	public String itemTop(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
   		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6 );
   		Page<MainItemDto> items = itemService.getItemTopPage(itemSearchDto, pageable);
   									model.addAttribute("items", items);
   									model.addAttribute("itemSearchDto", itemSearchDto);
   									model.addAttribute("maxPage", 5);
   		return "item/itemsTop";
   	}
  
    // 하의
    @GetMapping("/item/cate/bottom")
   	public String itemBottom(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
   		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6 );
   		Page<MainItemDto> items = itemService.getItemBottomPage(itemSearchDto, pageable);
   									model.addAttribute("items", items);
   									model.addAttribute("itemSearchDto", itemSearchDto);
   									model.addAttribute("maxPage", 5);
   		return "item/itemsBottom";
   	}
    
    // 신발
    @GetMapping("/item/cate/shoes")
   	public String itemShoes(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
   		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6 );
   		Page<MainItemDto> items = itemService.getItemShoesPage(itemSearchDto, pageable);
   									model.addAttribute("items", items);
   									model.addAttribute("itemSearchDto", itemSearchDto);
   									model.addAttribute("maxPage", 5);
   		return "item/itemsShoes";
   	}
    
    // 가방
    @GetMapping("/item/cate/bag")
   	public String itemBag(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
   		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6 );
   		Page<MainItemDto> items = itemService.getItemBagPage(itemSearchDto, pageable);
   									model.addAttribute("items", items);
   									model.addAttribute("itemSearchDto", itemSearchDto);
   									model.addAttribute("maxPage", 5);
   		return "item/itemsBag";
   	}
    
    

      
}

