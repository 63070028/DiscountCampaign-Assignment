package com.example.discountservice.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscountDetail {
    private String amount;
    private DiscountCampaigns useDiscountCampaign;
    private String totalPrice;
}
