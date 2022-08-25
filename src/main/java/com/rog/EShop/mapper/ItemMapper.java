package com.rog.EShop.mapper;

import com.rog.EShop.dto.CategoryCreateDto;
import com.rog.EShop.dto.ItemCreateDto;
import com.rog.EShop.entity.Category;
import com.rog.EShop.entity.Item;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper( uses = Category.class)
public interface ItemMapper {
    ItemCreateDto toDTO(Item item);
    ItemCreateDto toDTO(Optional<Item> item);
    List<ItemCreateDto> toDTO(List<Item> categories);
}
