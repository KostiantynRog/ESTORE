package com.rog.EShop.services;

import com.rog.EShop.entity.Item;
import com.rog.EShop.repository.ItemRepository;

import java.util.Optional;

public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    public Optional<Item> findById(Integer id){
        return itemRepository.findById(id);
    }
}
