package com.rog.EShop.controllers;

import com.rog.EShop.entity.Item;
import com.rog.EShop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/")
public class ItemController {
    private final ItemRepository itemRepository;
    @GetMapping(path = "/items/{id}")
    public Optional<Item> getItemById(@PathVariable Integer id){
        return itemRepository.findById(id);
    }
}
