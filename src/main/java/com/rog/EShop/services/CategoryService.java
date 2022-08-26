package com.rog.EShop.services;

import com.rog.EShop.dto.CategoryDto;
import com.rog.EShop.entity.Category;
import com.rog.EShop.exceptions.NotFoundException;
import com.rog.EShop.mapper.CategoryMapper;
import com.rog.EShop.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    public CategoryDto findById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found"));
        return categoryMapper.toDTO(category);
    }
//    Second variant not to return Optional

//    public CategoryDto findById1(Integer id) {
//      Optional<Category> category = categoryRepository.findById(id);
//      if(category.isPresent()){
//          return categoryMapper.toDTO(category.get());
//      }else {
//          throw new NotFoundException("Not found");
//      }
//
//    }

    public List<CategoryDto> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDTO(categories);
    }

    public CategoryDto save(Category category) {
        Category categoryDTO = categoryRepository.save(category);
        return categoryMapper.toDTO(categoryDTO);
    }

    public CategoryDto update(Category category) {
        Category categoryDto = categoryRepository.save(category);
        return categoryMapper.toDTO(categoryDto);
    }

    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }
}
