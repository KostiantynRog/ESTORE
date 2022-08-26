package com.rog.EShop.mapper;

import com.rog.EShop.dto.ItemDto;
import com.rog.EShop.entity.Category;
import com.rog.EShop.entity.Item;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper( uses = Category.class)
public interface ItemMapper {
    ItemDto toDTO(Item item);
    List<ItemDto> toDTO(List<Item> items);
}
