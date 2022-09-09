package com.rog.EShop.mapper;

import com.rog.EShop.dto.ItemDto;
import com.rog.EShop.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface ItemMapper {
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ItemDto toDTO(Item item);

    @Mapping(target = "category.id", source = "categoryId")
    Item toEntity(ItemDto itemDto);

    List<ItemDto> toEntity(Page<Item> items);
    List<ItemDto> toEntity(List<Item> items);
    
}
