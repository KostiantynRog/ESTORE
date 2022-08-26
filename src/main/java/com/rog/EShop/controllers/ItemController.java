package com.rog.EShop.controllers;

import com.rog.EShop.dto.ItemDto;
import com.rog.EShop.entity.Item;
import com.rog.EShop.exceptions.BadRequestException;
import com.rog.EShop.services.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping(path = "/api")
public class ItemController {
    private final ItemService itemService;


    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(path = "/items/{id}")
    public ItemDto getItemById(@PathVariable Integer id) {
        return itemService.findById(id);
    }

    @GetMapping(path = "/items/last")
    public List<ItemDto> getLast5By() {
        return itemService.findLast5By();
    }


    @PostMapping(path = "/items")
    public ResponseEntity<ItemDto> create(@RequestBody Item item) {
        if (item.getId() != null) {
            throw new BadRequestException("Id should be empty");
        }
        ItemDto itemDto = itemService.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemDto);
    }

    @PutMapping(path = "/items")
    public ItemDto update(@RequestBody Item item) {
        if (item.getId() == null) {
            throw new RuntimeException(" Id is not present in request body");
        }
        return itemService.update(item);

    }


    @DeleteMapping(path = "/items/{id}")
    public void deleteItem(@PathVariable Integer id) {
        itemService.delete(id);
    }
}
