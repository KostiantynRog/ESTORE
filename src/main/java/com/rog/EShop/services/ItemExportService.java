package com.rog.EShop.services;


import com.rog.EShop.entity.Item;
import com.rog.EShop.repository.ItemRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Service
public class ItemExportService {
    private final ItemRepository itemRepository;

    public ItemExportService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void exportCSV(Writer writer) {
        List<Item> items = itemRepository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.POSTGRESQL_CSV)) {
            csvPrinter.printRecord("Id", "Name", "Category Id", "Short description", "Price");
            for (Item item : items) {
                csvPrinter.printRecord(item.getId(), item.getName(), item.getCategory(),
                        item.getShortDescription(), item.getPrice());
            }
        } catch (IOException e) {
            System.out.println("DB error");
        }
    }
}
