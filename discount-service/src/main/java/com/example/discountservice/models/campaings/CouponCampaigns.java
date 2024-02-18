package com.example.discountservice.models.campaings;

import com.example.discountservice.models.campaings.coupon.CouponAmount;
import com.example.discountservice.models.campaings.coupon.CouponPercentage;
import lombok.Data;

@Data
public class CouponCampaigns {
    private CouponAmount fixedAmount;
    private CouponPercentage percentageDiscount;
}
