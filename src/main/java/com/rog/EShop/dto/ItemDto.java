package com.rog.EShop.dto;


public class ItemDto {
    private String name;
    private String shortDescription;
    private String fullDescription;

    public ItemDto() {
    }

    public ItemDto(String name, String shortDescription, String fullDescription) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
}
