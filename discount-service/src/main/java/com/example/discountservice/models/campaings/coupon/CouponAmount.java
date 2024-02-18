package com.example.discountservice.models.campaings.coupon;

import com.example.discountservice.util.CategoryCampaigns;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponAmount {
    private int amount;
    private String campaign;
    private CategoryCampaigns type;
    private double discount;
}
