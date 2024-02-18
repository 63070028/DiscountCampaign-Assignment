package com.example.discountservice.models.campaings;

import com.example.discountservice.models.campaings.ontop.OnTopPercentageCategory;
import com.example.discountservice.models.campaings.ontop.OnTopPoint;
import lombok.Data;

@Data
public class OnTopCampaigns {
    private OnTopPercentageCategory percentageCategory;
    private OnTopPoint pointDiscount;
}
