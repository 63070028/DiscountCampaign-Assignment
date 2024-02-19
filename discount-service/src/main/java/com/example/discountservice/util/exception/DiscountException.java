package com.example.discountservice.util.exception;

public class DiscountException extends BaseException {
    public DiscountException(String code) {
        super("request." + code);
    }

    public static DiscountException requestNull() {
        return new DiscountException("null");
    }

    public static DiscountException discountCampaignsNull() {
        return new DiscountException("discountCampaigns is null");
    }

    public static DiscountException couponNull() {
        return new DiscountException("discountCampaigns.coupon is null");
    }

    public static DiscountException onTopNull() {
        return new DiscountException("discountCampaigns.onTop is null");
    }

    public static DiscountException seasonalNull() {
        return new DiscountException("discountCampaigns.seasonal is null");
    }

    public static DiscountException couponApplySameCategory() {
        return new DiscountException("discountCampaigns.coupon apply same category");
    }

    public static DiscountException onTopApplySameCategory() {
        return new DiscountException("discountCampaigns.onTop apply same category");
    }


    public static DiscountException seasonalApplySameCategory() {
        return new DiscountException("discountCampaigns.seasonal apply same category");
    }

    public static DiscountException pointGreaterThanPercentageAmount(int maxPoint) {
        return new DiscountException("discountCampaigns.seasonal.pointDiscount.point greater than max point percentage amount " + maxPoint);
    }

    public static DiscountException categoryIsEmpty() {
        return new DiscountException("discountCampaigns.onTop.percentageCategory.category is empty");
    }

    public static DiscountException categoryDuplicate() {
        return new DiscountException("discountCampaigns.onTop.percentageCategory.category is duplicate");
    }

    public static DiscountException amountIsZero() {
        return new DiscountException("cart item amount is 0");
    }

    public static DiscountException everyIsZero() {
        return new DiscountException("discountCampaigns.seasonal.everyAmount.every is 0");
    }

}
