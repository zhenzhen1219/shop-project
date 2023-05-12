package com.shop.repository;

import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long>{

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
    
    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);
    
}
