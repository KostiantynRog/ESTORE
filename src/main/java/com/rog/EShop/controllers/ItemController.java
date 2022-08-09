package com.rog.EShop.controllers;

import com.rog.EShop.entity.Item;
import com.rog.EShop.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class ItemController {
    private final ItemService itemService;
    @GetMapping(path = "/items/{id}")
    public Optional<Item> getItemById(@PathVariable Integer id){
        return itemService.findById(id);
    }
}
