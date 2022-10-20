package com.rog.EShop.mapper;

import com.rog.EShop.dto.CategoryDto;
import com.rog.EShop.entity.Category;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface CategoryMapper {

    CategoryDto toDTO(Category category);

    List<CategoryDto> toDto(Page<Category> categories);

    Category toEntity(CategoryDto categoryDto);

}
