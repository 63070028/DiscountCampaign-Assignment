package com.example.discountservice.services;

import com.example.discountservice.models.*;
import com.example.discountservice.util.DiscountCalculate;
import com.example.discountservice.util.exception.BaseException;
import com.example.discountservice.util.exception.DiscountException;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class DiscountService {

    private final DecimalFormat df = new DecimalFormat("#.##");

    public DiscountService() {
        df.setRoundingMode(RoundingMode.FLOOR);
    }

    public DiscountResponse calculateTotalPrice(DiscountRequest request) throws BaseException {

        if (Objects.isNull(request)) {
            throw DiscountException.requestNull();
        }

        double amount = 0;
        List<Item> cart = request.getCart();
        for (Item item : cart) {
            amount += item.getPrice() * item.getQuantity();
        }
        double totalPrice = amount;

        DiscountCampaigns discountCampaigns = request.getDiscountCampaigns();
        validateDiscountCampaigns(discountCampaigns, amount);


        if (Objects.nonNull(discountCampaigns.getCoupon().getFixedAmount())) {
            double discount = discountCampaigns.getCoupon().getFixedAmount().getAmount();
            double result = totalPrice - discount;
            totalPrice = result >= 0 ? result : 0;
            discountCampaigns.getCoupon().getFixedAmount().setDiscount(discount);
        }

        if (Objects.nonNull(discountCampaigns.getCoupon().getPercentageDiscount())) {
            double discount = DiscountCalculate.percentage(totalPrice, discountCampaigns.getCoupon().getPercentageDiscount().getPercentage());
            double result = totalPrice - discount;
            totalPrice = result >= 0 ? result : 0;
            discountCampaigns.getCoupon().getPercentageDiscount().setDiscount(discount);
        }

        if (Objects.nonNull(discountCampaigns.getOnTop().getPercentageCategory())) {
            double discount = DiscountCalculate.percentageCategory(cart, discountCampaigns.getOnTop().getPercentageCategory().getCategory(), discountCampaigns.getOnTop().getPercentageCategory().getPercentage());
            double result = totalPrice - discount;
            totalPrice = result >= 0 ? result : 0;
            discountCampaigns.getOnTop().getPercentageCategory().setDiscount(discount);
        }

        if (Objects.nonNull(discountCampaigns.getOnTop().getPointDiscount())) {
            int point = discountCampaigns.getOnTop().getPointDiscount().getPoint();
            int maxPoint = DiscountCalculate.maxPoint(totalPrice);
            if (point > maxPoint) {
                throw DiscountException.pointGreaterThanPercentageAmount(maxPoint);
            }
            double discount = point;
            double result = totalPrice - discount;
            totalPrice = result >= 0 ? result : 0;
            discountCampaigns.getOnTop().getPointDiscount().setDiscount(discount);
        }

        if (Objects.nonNull(discountCampaigns.getSeasonal().getEveryAmount())) {
            double discount = DiscountCalculate.everyAmount(totalPrice, discountCampaigns.getSeasonal().getEveryAmount().getEvery(), discountCampaigns.getSeasonal().getEveryAmount().getAmount());
            double result = totalPrice - discount;
            totalPrice = result >= 0 ? result : 0;
            discountCampaigns.getSeasonal().getEveryAmount().setDiscount(discount);
        }

        totalPrice = Double.parseDouble(df.format(totalPrice));
        return DiscountResponse.builder().payload(
                        DiscountDetail.builder()
                                .amount(formatNumber(amount))
                                .totalPrice(formatNumber(totalPrice))
                                .useDiscountCampaign(discountCampaigns)
                                .build())
                .build();
    }

    private void validateDiscountCampaigns(DiscountCampaigns discountCampaigns, double amount) throws BaseException {

        if (amount <= 0) {
            throw DiscountException.amountIsZero();
        }

        if (Objects.isNull(discountCampaigns)) {
            throw DiscountException.discountCampaignsNull();
        }

        if (Objects.isNull(discountCampaigns.getCoupon())) {
            throw DiscountException.couponNull();
        }

        if (Objects.isNull(discountCampaigns.getOnTop())) {
            throw DiscountException.onTopNull();
        }

        if (Objects.isNull(discountCampaigns.getSeasonal())) {
            throw DiscountException.seasonalNull();
        }

        if (Objects.nonNull(discountCampaigns.getCoupon().getFixedAmount()) && Objects.nonNull(discountCampaigns.getCoupon().getPercentageDiscount())) {
            throw DiscountException.couponApplySameCategory();
        }

        if (Objects.nonNull(discountCampaigns.getOnTop().getPercentageCategory()) && Objects.nonNull(discountCampaigns.getOnTop().getPointDiscount())) {
            throw DiscountException.onTopApplySameCategory();
        }

        if (Objects.nonNull(discountCampaigns.getOnTop().getPointDiscount())) {
            int point = discountCampaigns.getOnTop().getPointDiscount().getPoint();
            int maxPoint = DiscountCalculate.maxPoint(amount);
            if (point > maxPoint) {
                throw DiscountException.pointGreaterThanPercentageAmount(maxPoint);
            }
        }

        if (Objects.nonNull(discountCampaigns.getOnTop().getPercentageCategory())) {
            List<String> categoryList = discountCampaigns.getOnTop().getPercentageCategory().getCategory();
            if (categoryList.size() == 0) {
                throw DiscountException.categoryIsEmpty();
            }

            Set<String> setCategory = new HashSet<>(categoryList);
            if (setCategory.size() != categoryList.size()) {
                throw DiscountException.categoryDuplicate();
            }
        }

        if (Objects.nonNull(discountCampaigns.getSeasonal().getEveryAmount())) {
            if (discountCampaigns.getSeasonal().getEveryAmount().getEvery() == 0) {
                throw DiscountException.everyIsZero();
            }
        }


    }

    public String formatNumber(double number) {
        return new Formatter().format("%.2f", number).toString();
    }
}
