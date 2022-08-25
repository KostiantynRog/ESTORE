package com.rog.EShop.mapper;

import com.rog.EShop.dto.CategoryCreateDto;
import com.rog.EShop.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface CategoryMapper {


    CategoryCreateDto toDTO(Category category);

    List<CategoryCreateDto> toDTO(List<Category> categories);

    CategoryCreateDto toDTO(Optional<Category> category);

}
