package com.example.discountservice.models;

import com.example.discountservice.models.campaings.CouponCampaigns;
import com.example.discountservice.models.campaings.OnTopCampaigns;
import com.example.discountservice.models.campaings.SeasonalCampaigns;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscountCampaigns {
    private CouponCampaigns coupon;
    private OnTopCampaigns onTop;
    private SeasonalCampaigns seasonal;
}
