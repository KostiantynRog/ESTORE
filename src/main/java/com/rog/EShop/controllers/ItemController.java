package com.rog.EShop.controllers;

import com.rog.EShop.dto.ItemCreateDto;
import com.rog.EShop.entity.Item;
import com.rog.EShop.mapper.ItemMapper;
import com.rog.EShop.services.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping(path = "/api")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @GetMapping(path = "/items/{id}")
    public Optional<ItemCreateDto> getItemById(@PathVariable Integer id) {
        Optional<Item> item = itemService.findById(id);
        return Optional.ofNullable(itemMapper.toDTO(item));
    }

    @GetMapping(path = "/items/last")
    public List<ItemCreateDto> getFirst5By() {
        List<Item> itemCreateDtoList = itemService.findFirst5By();
        return itemMapper.toDTO(itemCreateDtoList);
    }


    @PostMapping(path = "/items")
    public ResponseEntity<ItemCreateDto> create(@RequestBody Item item) {
        if(item.getId() != null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Item itemDTO = itemService.save(item);
        ItemCreateDto newItem = itemMapper.toDTO(itemDTO);
        return new ResponseEntity<>(newItem, HttpStatus.CREATED);
    }

    @PutMapping(path = "/items")
    public ItemCreateDto update(@RequestBody Item item) {
        if (item.getId() == null) {
            throw new RuntimeException(" Id is not present in request body");
        }
        Item itemDTO = itemService.update(item);
        return itemMapper.toDTO(itemDTO);
    }


    @DeleteMapping(path = "/items/{id}")
    public void deleteItem(@PathVariable Integer id) {
        itemService.delete(id);
    }
}
