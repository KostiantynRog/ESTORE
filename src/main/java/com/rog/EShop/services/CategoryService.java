package com.rog.EShop.services;

import com.rog.EShop.entity.Category;
import com.rog.EShop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;


    public Optional<Category> findById(Integer id){
        return categoryRepository.findById(id);
    }
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }
}
