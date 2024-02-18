package com.example.discountservice.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {
    private String name;
    private String category;
    private double price;
    private int quantity;
}
