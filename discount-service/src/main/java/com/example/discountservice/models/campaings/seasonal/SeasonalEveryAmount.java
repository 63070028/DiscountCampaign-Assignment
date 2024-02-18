package com.example.discountservice.models.campaings.seasonal;

import com.example.discountservice.util.CategoryCampaigns;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeasonalEveryAmount {
    private String campaign;
    private double discount;
    private CategoryCampaigns type;
    private int every;
    private int amount;
}
