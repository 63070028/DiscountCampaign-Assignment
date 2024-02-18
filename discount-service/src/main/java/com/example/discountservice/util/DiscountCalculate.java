package com.example.discountservice.util;

import com.example.discountservice.models.Item;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class DiscountCalculate {
    private static final double FIXEDPRICE = 0.2;
    private static final DecimalFormat df = new DecimalFormat("#.####");

    public DiscountCalculate() {
        df.setRoundingMode(RoundingMode.FLOOR);
    }

    public static double percentage(double totalPrice, int percentageDiscount) {
        return Double.parseDouble(df.format(totalPrice * percentageDiscount / 100));
    }

    public static double percentageCategory(List<Item> cart, List<String> category, int percentageDiscount) {
        double result = 0;
        for (String s : category) {
            for (Item item : cart) {
                if (item.getCategory().equals(s)) {
                    result += percentage(item.getPrice(), percentageDiscount) * item.getQuantity();
                }
            }
        }
        return Double.parseDouble(df.format(result));
    }

    public static int maxPoint(double totalPrice) {
        return (int) Math.floor(totalPrice * FIXEDPRICE);
    }

    public static double everyAmount(double totalPrice, int every, int amountDiscount) {
        return Math.floor(totalPrice / every) * amountDiscount;
    }
}
