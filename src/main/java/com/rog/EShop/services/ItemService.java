package com.rog.EShop.services;

import com.rog.EShop.dto.ItemDto;
import com.rog.EShop.entity.Item;
import com.rog.EShop.exceptions.NotFoundException;
import com.rog.EShop.mapper.ItemMapper;
import com.rog.EShop.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private  final Logger log = LoggerFactory.getLogger(ItemService.class);
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public ItemDto findById(Integer id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(
                        () -> {
                           log.error("Not found itemId: {}",
                                   id);
                            return new NotFoundException("Not found");
                        });
        return itemMapper.toDTO(item);
    }

    public List<ItemDto> findLast5By() {
        List<Item> items = itemRepository.findFirst5ByOrderByIdDesc();
        return itemMapper.toDTO(items);
    }
    public List<ItemDto> findAllByCategoryId(Integer id){
        log.debug("Getting all items in category {}", id);
        List<Item> items = itemRepository.findAllByCategoryId(id);
        return itemMapper.toDTO(items);
    }

    public ItemDto save(ItemDto itemDto) {
        Item item = itemMapper.toEntity(itemDto);
        Item itemSaved = itemRepository.save(item);
        return itemMapper.toDTO(itemSaved);


    }

    public ItemDto update(ItemDto itemDto) {
        Item item = itemMapper.toEntity(itemDto);
        Item itemUpdated = itemRepository.save(item);
        return itemMapper.toDTO(itemUpdated);
    }

    public void delete(Integer id) {
        itemRepository.deleteById(id);
    }
}
