package com.rog.EShop.services;

import com.rog.EShop.dto.ItemDto;
import com.rog.EShop.entity.Item;
import com.rog.EShop.exceptions.NotFoundException;
import com.rog.EShop.mapper.ItemMapper;
import com.rog.EShop.repository.ItemRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Service
public class ItemService {
    private final Logger log = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public ItemDto findById(Integer id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Not found itemId: {}", id);
                    return new NotFoundException("Not found");
                });
        return itemMapper.toDTO(item);
    }

    public List<ItemDto> findAll(Pageable pageable) {
        Page<Item> items = itemRepository.findAll(pageable);
        return itemMapper.toEntity(items);
    }

    public List<ItemDto> findAllByCategoryId(Integer id, Pageable pageable) {
        log.debug("Getting all items in category {}", id);
        Page<Item> items = itemRepository.findAllByCategoryId(id, pageable);
        return itemMapper.toEntity(items);
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

    //    public List<ItemDto> findByName(String filter){
//        List<Item> items = itemRepository.findByName(filter);
//        return itemMapper.toEntity(items);
//    }
    public List<ItemDto> findByName(String filter) {
        List<Item> items = itemRepository.findByNameContainingIgnoreCase(filter);
        return itemMapper.toEntity(items);
    }
    public void exportCSV(Writer writer) {
        List<Item> items = itemRepository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.POSTGRESQL_CSV)) {
            csvPrinter.printRecord("Id", "Name", "Category Id", "Short description", "Price");
            for (Item item : items) {
                csvPrinter.printRecord(item.getId(), item.getName(), item.getCategory().getId(),
                        item.getShortDescription(), item.getPrice());
            }
        } catch (IOException e) {
            System.out.println("DB error");
        }
    }
}
