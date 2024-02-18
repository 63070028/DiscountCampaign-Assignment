package com.example.discountservice.models.campaings.coupon;

import com.example.discountservice.util.CategoryCampaigns;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponPercentage {
    private String campaign;
    private double discount;
    private CategoryCampaigns type;
    private int percentage;

}
