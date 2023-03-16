package com.rog.EShop.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

@Service
public class ItemExportService {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;

    public void exportCSV() {

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT * FROM items";

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(sql);

            BufferedWriter fileWriter = new BufferedWriter(new FileWriter("output/items.csv"));

            // write header line containing column names
            fileWriter.write("id,name,category_id,short_description,price");

            while (result.next()) {
                String id = result.getString("id");
                String itemName = result.getString("name");
                String categoryId = result.getString("category_id");
                String shortDescription = result.getString("short_description");
                BigDecimal price = result.getBigDecimal("price");


                String line = String.format("%s,\"%s\",%s,\"%s\",%.1f",
                        id, itemName, categoryId, shortDescription, price);

                fileWriter.newLine();
                fileWriter.write(line);
            }

            statement.close();
            fileWriter.close();

        } catch (SQLException e) {
            System.out.println("Database error:");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File IO error:");
            e.printStackTrace();
        }

    }
//    private static final String OBJECT_LIST = "output/items.csv";
//    public void exportCSVWriter() throws IOException, SQLException{
//        try(Connection connection = DriverManager.getConnection(url, username, password)){
//            String sql = "SELECT * FROM items";
//            Statement statement = connection.createStatement();
//            ResultSet result = statement.executeQuery(sql);
//
//        }
//        Writer writer = Files.newBufferedWriter(Paths.get(OBJECT_LIST));
//        StatefulBeanToCsv<Item> beanToCsv = new StatefulBeanToCsvBuilder(writer)
//                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
//                .build();
//    }
}
