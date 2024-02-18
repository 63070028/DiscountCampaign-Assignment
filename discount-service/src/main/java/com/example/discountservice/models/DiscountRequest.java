package com.example.discountservice.models;

import lombok.Data;

import java.util.List;

@Data
public class DiscountRequest {
    private List<Item> cart;
    private DiscountCampaigns discountCampaigns;
}
