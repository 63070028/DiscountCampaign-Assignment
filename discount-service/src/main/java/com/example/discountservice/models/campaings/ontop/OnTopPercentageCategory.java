package com.example.discountservice.models.campaings.ontop;

import com.example.discountservice.util.CategoryCampaigns;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OnTopPercentageCategory {
    private String campaign;
    private double discount;
    private List<String> category;
    private CategoryCampaigns type;
    private int percentage;
}
