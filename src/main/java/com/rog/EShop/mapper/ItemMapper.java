package com.rog.EShop.mapper;

import com.rog.EShop.dto.ItemDto;
import com.rog.EShop.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ItemMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ItemDto toDTO(Item item);

    @Mapping(target = "category.id", source = "categoryId")
    Item toEntity(ItemDto itemDto);

    List<ItemDto> toDTO(List<Item> items);
}