package com.rog.EShop.services;

import com.rog.EShop.dto.CategoryDto;
import com.rog.EShop.entity.Category;
import com.rog.EShop.exceptions.NotFoundException;
import com.rog.EShop.mapper.CategoryMapper;
import com.rog.EShop.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryService {
    private final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    public CategoryDto findById(Integer id) {
        log.debug("call findbyId with param: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("categoryId not found: {}", id);
                    return new NotFoundException("Not found");
                });
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

    public List<CategoryDto> findAll(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categoryMapper.toDto(categories);
    }

    public CategoryDto save(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        Category categorySaved = categoryRepository.save(category);
        return categoryMapper.toDTO(categorySaved);
    }

    public CategoryDto update(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        Category categoryUpdated = categoryRepository.save(category);
        return categoryMapper.toDTO(categoryUpdated);
    }

    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }
}
