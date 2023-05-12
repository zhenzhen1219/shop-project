package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.constant.ItemTypeStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {

    private String searchDateType;

    private ItemSellStatus searchSellStatus;
    
    private ItemTypeStatus itemTypeStatus;
    
    private String searchBy;

    private String searchQuery="";
}
