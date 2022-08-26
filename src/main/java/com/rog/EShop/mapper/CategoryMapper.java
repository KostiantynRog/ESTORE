package com.rog.EShop.mapper;

import com.rog.EShop.dto.CategoryDto;
import com.rog.EShop.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {


    CategoryDto toDTO(Category category);

    List<CategoryDto> toDTO(List<Category> categories);

}
