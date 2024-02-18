package com.example.discountservice;

import com.example.discountservice.models.DiscountCampaigns;
import com.example.discountservice.models.DiscountRequest;
import com.example.discountservice.models.DiscountResponse;
import com.example.discountservice.models.Item;
import com.example.discountservice.models.campaings.CouponCampaigns;
import com.example.discountservice.models.campaings.OnTopCampaigns;
import com.example.discountservice.models.campaings.SeasonalCampaigns;
import com.example.discountservice.models.campaings.coupon.CouponAmount;
import com.example.discountservice.models.campaings.coupon.CouponPercentage;
import com.example.discountservice.models.campaings.ontop.OnTopPercentageCategory;
import com.example.discountservice.models.campaings.ontop.OnTopPoint;
import com.example.discountservice.models.campaings.seasonal.SeasonalEveryAmount;
import com.example.discountservice.services.DiscountService;
import com.example.discountservice.util.CategoryCampaigns;
import com.example.discountservice.util.exception.BaseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestDiscountService {

    private final List<Item> cart;
    private final List<Item> cartEx1;
    private final List<Item> cartEx2;
    private final List<Item> cartEx3;

    @Autowired
    private DiscountService discountService;

    public TestDiscountService() {
        this.cart = new ArrayList<>();
        cart.add(Item.builder().name("Item1").category("A").price(350).quantity(1).build());
        cart.add(Item.builder().name("Item2").category("B").price(250).quantity(2).build());
        cart.add(Item.builder().name("Item3").category("B").price(550.5).quantity(2).build());
        cart.add(Item.builder().name("Item4").category("C").price(400.25).quantity(3).build());

        this.cartEx1 = new ArrayList<>();
        cartEx1.add(Item.builder().name("Item1").category("A").price(350).quantity(1).build());
        cartEx1.add(Item.builder().name("Item1").category("B").price(250).quantity(1).build());

        this.cartEx2 = new ArrayList<>();
        cartEx2.add(Item.builder().name("Item1").category("A").price(350).quantity(1).build());
        cartEx2.add(Item.builder().name("Item1").category("A").price(700).quantity(1).build());
        cartEx2.add(Item.builder().name("Item1").category("B").price(850).quantity(1).build());
        cartEx2.add(Item.builder().name("Item1").category("B").price(640).quantity(1).build());

        this.cartEx3 = new ArrayList<>();
        cartEx3.add(Item.builder().name("Item1").category("A").price(350).quantity(1).build());
        cartEx3.add(Item.builder().name("Item1").category("A").price(250).quantity(1).build());
        cartEx3.add(Item.builder().name("Item1").category("B").price(230).quantity(1).build());
    }

    @Test
    public void testCaseEx1() throws BaseException {
        DiscountRequest request = new DiscountRequest();

        CouponCampaigns couponCampaigns = new CouponCampaigns();
        couponCampaigns.setFixedAmount(CouponAmount.builder()
                .campaign("Fixed amount")
                .type(CategoryCampaigns.COUPON)
                .amount(50)
                .discount(0)
                .build());

        DiscountCampaigns discountCampaigns = DiscountCampaigns.builder()
                .coupon(couponCampaigns)
                .onTop(new OnTopCampaigns())
                .seasonal(new SeasonalCampaigns())
                .build();

        request.setCart(cartEx1);
        request.setDiscountCampaigns(discountCampaigns);
        DiscountResponse response = discountService.calculateTotalPrice(request);

        Assertions.assertNotNull(response.getPayload());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getCoupon().getFixedAmount());
        Assertions.assertEquals(response.getPayload().getAmount(), "600.00");
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getCoupon().getFixedAmount().getDiscount(), 50);
        Assertions.assertEquals(response.getPayload().getTotalPrice(), "550.00");
    }

    @Test
    public void testCaseEx2() throws BaseException {
        DiscountRequest request = new DiscountRequest();

        CouponCampaigns couponCampaigns = new CouponCampaigns();
        couponCampaigns.setPercentageDiscount(CouponPercentage.builder()
                .campaign("Percentage discount")
                .type(CategoryCampaigns.COUPON)
                .percentage(10)
                .discount(0)
                .build());

        DiscountCampaigns discountCampaigns = DiscountCampaigns.builder()
                .coupon(couponCampaigns)
                .onTop(new OnTopCampaigns())
                .seasonal(new SeasonalCampaigns())
                .build();

        request.setCart(cartEx1);
        request.setDiscountCampaigns(discountCampaigns);
        DiscountResponse response = discountService.calculateTotalPrice(request);

        Assertions.assertNotNull(response.getPayload());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getCoupon().getPercentageDiscount());
        Assertions.assertEquals(response.getPayload().getAmount(), "600.00");
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getCoupon().getPercentageDiscount().getDiscount(), 60);
        Assertions.assertEquals(response.getPayload().getTotalPrice(), "540.00");
    }

    @Test
    public void testCaseEx3() throws BaseException {
        DiscountRequest request = new DiscountRequest();

        OnTopCampaigns onTopCampaigns = new OnTopCampaigns();
        String[] category = {"A"};
        onTopCampaigns.setPercentageCategory(OnTopPercentageCategory.builder()
                .campaign("Percentage discount by category")
                .type(CategoryCampaigns.ONTOP)
                .category(Arrays.asList(category))
                .percentage(15)
                .discount(0)
                .build());

        DiscountCampaigns discountCampaigns = DiscountCampaigns.builder()
                .coupon(new CouponCampaigns())
                .onTop(onTopCampaigns)
                .seasonal(new SeasonalCampaigns())
                .build();

        request.setCart(cartEx2);
        request.setDiscountCampaigns(discountCampaigns);
        DiscountResponse response = discountService.calculateTotalPrice(request);

        Assertions.assertNotNull(response.getPayload());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getOnTop().getPercentageCategory());
        Assertions.assertEquals(response.getPayload().getAmount(), "2540.00");
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getOnTop().getPercentageCategory().getDiscount(), 157.5);
        Assertions.assertEquals(response.getPayload().getTotalPrice(), "2382.50");
    }

    @Test
    public void testCaseEx4() throws BaseException {
        DiscountRequest request = new DiscountRequest();

        OnTopCampaigns onTopCampaigns = new OnTopCampaigns();
        onTopCampaigns.setPointDiscount(OnTopPoint.builder()
                .campaign("Discount by points")
                .type(CategoryCampaigns.ONTOP)
                .point(68)
                .discount(0)
                .build());

        DiscountCampaigns discountCampaigns = DiscountCampaigns.builder()
                .coupon(new CouponCampaigns())
                .onTop(onTopCampaigns)
                .seasonal(new SeasonalCampaigns())
                .build();

        request.setCart(cartEx3);
        request.setDiscountCampaigns(discountCampaigns);
        DiscountResponse response = discountService.calculateTotalPrice(request);

        Assertions.assertNotNull(response.getPayload());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getOnTop().getPointDiscount());
        Assertions.assertEquals(response.getPayload().getAmount(), "830.00");
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getOnTop().getPointDiscount().getDiscount(), 68);
        Assertions.assertEquals(response.getPayload().getTotalPrice(), "762.00");
    }

    @Test
    public void testCaseEx5() throws BaseException {
        DiscountRequest request = new DiscountRequest();

        SeasonalCampaigns seasonalCampaigns = new SeasonalCampaigns();
        seasonalCampaigns.setEveryAmount(SeasonalEveryAmount.builder()
                .campaign("Special campaigns")
                .type(CategoryCampaigns.SEASONAL)
                .every(300)
                .amount(40)
                .discount(0)
                .build());

        DiscountCampaigns discountCampaigns = DiscountCampaigns.builder()
                .coupon(new CouponCampaigns())
                .onTop(new OnTopCampaigns())
                .seasonal(seasonalCampaigns)
                .build();

        request.setCart(cartEx3);
        request.setDiscountCampaigns(discountCampaigns);
        DiscountResponse response = discountService.calculateTotalPrice(request);

        Assertions.assertNotNull(response.getPayload());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getSeasonal().getEveryAmount());
        Assertions.assertEquals(response.getPayload().getAmount(), "830.00");
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getSeasonal().getEveryAmount().getDiscount(), 80);
        Assertions.assertEquals(response.getPayload().getTotalPrice(), "750.00");
    }

    @Test
    public void testNotUsedDiscountCampaigns() throws BaseException {
        DiscountRequest request = new DiscountRequest();

        DiscountCampaigns discountCampaigns = DiscountCampaigns.builder()
                .coupon(new CouponCampaigns())
                .onTop(new OnTopCampaigns())
                .seasonal(new SeasonalCampaigns())
                .build();

        request.setCart(cart);
        request.setDiscountCampaigns(discountCampaigns);
        DiscountResponse response = discountService.calculateTotalPrice(request);

        Assertions.assertNotNull(response.getPayload());
        Assertions.assertEquals(response.getPayload().getAmount(), "3151.75");
        Assertions.assertEquals(response.getPayload().getTotalPrice(), "3151.75");
    }

    @Test
    public void testUsedCouponAmount() throws BaseException {
        DiscountRequest request = new DiscountRequest();

        CouponCampaigns couponCampaigns = new CouponCampaigns();
        couponCampaigns.setFixedAmount(CouponAmount.builder()
                .campaign("Fixed amount")
                .type(CategoryCampaigns.COUPON)
                .amount(100)
                .discount(0)
                .build());

        DiscountCampaigns discountCampaigns = DiscountCampaigns.builder()
                .coupon(couponCampaigns)
                .onTop(new OnTopCampaigns())
                .seasonal(new SeasonalCampaigns())
                .build();

        request.setCart(cart);
        request.setDiscountCampaigns(discountCampaigns);
        DiscountResponse response = discountService.calculateTotalPrice(request);

        Assertions.assertNotNull(response.getPayload());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getCoupon().getFixedAmount());
        Assertions.assertEquals(response.getPayload().getAmount(), "3151.75");
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getCoupon().getFixedAmount().getDiscount(), 100);
        Assertions.assertEquals(response.getPayload().getTotalPrice(), "3051.75");
    }

    @Test
    public void testUsedCouponPercentage() throws BaseException {
        DiscountRequest request = new DiscountRequest();

        CouponCampaigns couponCampaigns = new CouponCampaigns();
        couponCampaigns.setPercentageDiscount(CouponPercentage.builder()
                .campaign("Percentage discount")
                .type(CategoryCampaigns.COUPON)
                .percentage(25)
                .discount(0)
                .build());

        DiscountCampaigns discountCampaigns = DiscountCampaigns.builder()
                .coupon(couponCampaigns)
                .onTop(new OnTopCampaigns())
                .seasonal(new SeasonalCampaigns())
                .build();

        request.setCart(cart);
        request.setDiscountCampaigns(discountCampaigns);
        DiscountResponse response = discountService.calculateTotalPrice(request);

        Assertions.assertNotNull(response.getPayload());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getCoupon().getPercentageDiscount());
        Assertions.assertEquals(response.getPayload().getAmount(), "3151.75");
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getCoupon().getPercentageDiscount().getDiscount(), 787.9375);
        Assertions.assertEquals(response.getPayload().getTotalPrice(), "2363.81");
    }

    @Test
    public void testUsedOnTopPercentageCategory() throws BaseException {
        DiscountRequest request = new DiscountRequest();

        OnTopCampaigns onTopCampaigns = new OnTopCampaigns();
        String[] category = {"B", "C"};
        onTopCampaigns.setPercentageCategory(OnTopPercentageCategory.builder()
                .campaign("Percentage discount by category")
                .type(CategoryCampaigns.ONTOP)
                .category(Arrays.asList(category))
                .percentage(15)
                .discount(0)
                .build());

        DiscountCampaigns discountCampaigns = DiscountCampaigns.builder()
                .coupon(new CouponCampaigns())
                .onTop(onTopCampaigns)
                .seasonal(new SeasonalCampaigns())
                .build();

        request.setCart(cart);
        request.setDiscountCampaigns(discountCampaigns);
        DiscountResponse response = discountService.calculateTotalPrice(request);

        Assertions.assertNotNull(response.getPayload());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getOnTop().getPercentageCategory());
        Assertions.assertEquals(response.getPayload().getAmount(), "3151.75");
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getOnTop().getPercentageCategory().getDiscount(), 420.2625);
        Assertions.assertEquals(response.getPayload().getTotalPrice(), "2731.48");
    }

    @Test
    public void testUsedOnTopOnTopPoint() throws BaseException {
        DiscountRequest request = new DiscountRequest();

        OnTopCampaigns onTopCampaigns = new OnTopCampaigns();
        onTopCampaigns.setPointDiscount(OnTopPoint.builder()
                .campaign("Discount by points")
                .type(CategoryCampaigns.ONTOP)
                .point(630)
                .discount(0)
                .build());

        DiscountCampaigns discountCampaigns = DiscountCampaigns.builder()
                .coupon(new CouponCampaigns())
                .onTop(onTopCampaigns)
                .seasonal(new SeasonalCampaigns())
                .build();

        request.setCart(cart);
        request.setDiscountCampaigns(discountCampaigns);
        DiscountResponse response = discountService.calculateTotalPrice(request);

        Assertions.assertNotNull(response.getPayload());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getOnTop().getPointDiscount());
        Assertions.assertEquals(response.getPayload().getAmount(), "3151.75");
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getOnTop().getPointDiscount().getDiscount(), 630);
        Assertions.assertEquals(response.getPayload().getTotalPrice(), "2521.75");
    }

    @Test
    public void testUsedSeasonalEveryAmount() throws BaseException {
        DiscountRequest request = new DiscountRequest();

        SeasonalCampaigns seasonalCampaigns = new SeasonalCampaigns();
        seasonalCampaigns.setEveryAmount(SeasonalEveryAmount.builder()
                .campaign("Special campaigns")
                .type(CategoryCampaigns.SEASONAL)
                .every(450)
                .amount(100)
                .discount(0)
                .build());

        DiscountCampaigns discountCampaigns = DiscountCampaigns.builder()
                .coupon(new CouponCampaigns())
                .onTop(new OnTopCampaigns())
                .seasonal(seasonalCampaigns)
                .build();

        request.setCart(cart);
        request.setDiscountCampaigns(discountCampaigns);
        DiscountResponse response = discountService.calculateTotalPrice(request);

        Assertions.assertNotNull(response.getPayload());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getSeasonal().getEveryAmount());
        Assertions.assertEquals(response.getPayload().getAmount(), "3151.75");
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getSeasonal().getEveryAmount().getDiscount(), 700);
        Assertions.assertEquals(response.getPayload().getTotalPrice(), "2451.75");
    }

    @Test
    public void testMultipleDiscountCampaigns1() throws BaseException {
        DiscountRequest request = new DiscountRequest();

        CouponCampaigns couponCampaigns = new CouponCampaigns();
        couponCampaigns.setFixedAmount(CouponAmount.builder()
                .campaign("Fixed amount")
                .type(CategoryCampaigns.COUPON)
                .amount(100)
                .discount(0)
                .build());

        OnTopCampaigns onTopCampaigns = new OnTopCampaigns();
        String[] category = {"B", "C"};
        onTopCampaigns.setPercentageCategory(OnTopPercentageCategory.builder()
                .campaign("Percentage discount by category")
                .type(CategoryCampaigns.ONTOP)
                .category(Arrays.asList(category))
                .percentage(15)
                .discount(0)
                .build());

        SeasonalCampaigns seasonalCampaigns = new SeasonalCampaigns();
        seasonalCampaigns.setEveryAmount(SeasonalEveryAmount.builder()
                .campaign("Special campaigns")
                .type(CategoryCampaigns.SEASONAL)
                .every(450)
                .amount(100)
                .discount(0)
                .build());

        DiscountCampaigns discountCampaigns = DiscountCampaigns.builder()
                .coupon(couponCampaigns)
                .onTop(onTopCampaigns)
                .seasonal(seasonalCampaigns)
                .build();

        request.setCart(cart);
        request.setDiscountCampaigns(discountCampaigns);
        DiscountResponse response = discountService.calculateTotalPrice(request);

        Assertions.assertNotNull(response.getPayload());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getCoupon().getFixedAmount());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getOnTop().getPercentageCategory());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getSeasonal().getEveryAmount());

        Assertions.assertEquals(response.getPayload().getAmount(), "3151.75");
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getCoupon().getFixedAmount().getDiscount(), 100);
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getOnTop().getPercentageCategory().getDiscount(), 420.2625);
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getSeasonal().getEveryAmount().getDiscount(), 500);
        Assertions.assertEquals(response.getPayload().getTotalPrice(), "2131.48");
    }

    @Test
    public void testMultipleDiscountCampaigns2() throws BaseException {
        DiscountRequest request = new DiscountRequest();

        CouponCampaigns couponCampaigns = new CouponCampaigns();
        couponCampaigns.setPercentageDiscount(CouponPercentage.builder()
                .campaign("Percentage discount")
                .type(CategoryCampaigns.COUPON)
                .percentage(25)
                .discount(0)
                .build());

        OnTopCampaigns onTopCampaigns = new OnTopCampaigns();
        onTopCampaigns.setPointDiscount(OnTopPoint.builder()
                .campaign("Discount by points")
                .type(CategoryCampaigns.ONTOP)
                .point(150)
                .discount(0)
                .build());

        SeasonalCampaigns seasonalCampaigns = new SeasonalCampaigns();
        seasonalCampaigns.setEveryAmount(SeasonalEveryAmount.builder()
                .campaign("Special campaigns")
                .type(CategoryCampaigns.SEASONAL)
                .every(450)
                .amount(100)
                .discount(0)
                .build());

        DiscountCampaigns discountCampaigns = DiscountCampaigns.builder()
                .coupon(couponCampaigns)
                .onTop(onTopCampaigns)
                .seasonal(seasonalCampaigns)
                .build();

        request.setCart(cart);
        request.setDiscountCampaigns(discountCampaigns);
        DiscountResponse response = discountService.calculateTotalPrice(request);

        Assertions.assertNotNull(response.getPayload());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getCoupon().getPercentageDiscount());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getOnTop().getPointDiscount());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getSeasonal().getEveryAmount());

        Assertions.assertEquals(response.getPayload().getAmount(), "3151.75");
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getCoupon().getPercentageDiscount().getDiscount(), 787.9375);
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getOnTop().getPointDiscount().getDiscount(), 150); //max 420
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getSeasonal().getEveryAmount().getDiscount(), 400);
        Assertions.assertEquals(response.getPayload().getTotalPrice(), "1813.81");
    }

    @Test
    public void testCaseDiscountGatherThanAmount() throws BaseException {
        DiscountRequest request = new DiscountRequest();

        CouponCampaigns couponCampaigns = new CouponCampaigns();
        couponCampaigns.setFixedAmount(CouponAmount.builder()
                .campaign("Fixed amount")
                .type(CategoryCampaigns.COUPON)
                .amount(700)
                .discount(0)
                .build());

        DiscountCampaigns discountCampaigns = DiscountCampaigns.builder()
                .coupon(couponCampaigns)
                .onTop(new OnTopCampaigns())
                .seasonal(new SeasonalCampaigns())
                .build();

        request.setCart(cartEx1);
        request.setDiscountCampaigns(discountCampaigns);
        DiscountResponse response = discountService.calculateTotalPrice(request);

        Assertions.assertNotNull(response.getPayload());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getCoupon().getFixedAmount());
        Assertions.assertEquals(response.getPayload().getAmount(), "600.00");
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getCoupon().getFixedAmount().getDiscount(), 700);
        Assertions.assertEquals(response.getPayload().getTotalPrice(), "0.00");
    }

    @Test
    public void testMultipleDiscountGatherThanAmount() throws BaseException {
        DiscountRequest request = new DiscountRequest();

        CouponCampaigns couponCampaigns = new CouponCampaigns();
        couponCampaigns.setFixedAmount(CouponAmount.builder()
                .campaign("Fixed amount")
                .type(CategoryCampaigns.COUPON)
                .amount(100)
                .discount(0)
                .build());

        OnTopCampaigns onTopCampaigns = new OnTopCampaigns();
        String[] category = {"A"};
        onTopCampaigns.setPercentageCategory(OnTopPercentageCategory.builder()
                .campaign("Percentage discount by category")
                .type(CategoryCampaigns.ONTOP)
                .category(Arrays.asList(category))
                .percentage(15)
                .discount(0)
                .build());

        SeasonalCampaigns seasonalCampaigns = new SeasonalCampaigns();
        seasonalCampaigns.setEveryAmount(SeasonalEveryAmount.builder()
                .campaign("Special campaigns")
                .type(CategoryCampaigns.SEASONAL)
                .every(500)
                .amount(1000)
                .discount(0)
                .build());

        DiscountCampaigns discountCampaigns = DiscountCampaigns.builder()
                .coupon(couponCampaigns)
                .onTop(onTopCampaigns)
                .seasonal(seasonalCampaigns)
                .build();

        request.setCart(cartEx2);
        request.setDiscountCampaigns(discountCampaigns);
        DiscountResponse response = discountService.calculateTotalPrice(request);

        Assertions.assertNotNull(response.getPayload());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getCoupon().getFixedAmount());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getOnTop().getPercentageCategory());
        Assertions.assertNotNull(response.getPayload().getUseDiscountCampaign().getSeasonal().getEveryAmount());

        Assertions.assertEquals(response.getPayload().getAmount(), "2540.00");
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getCoupon().getFixedAmount().getDiscount(), 100);
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getOnTop().getPercentageCategory().getDiscount(), 157.5);
        Assertions.assertEquals(response.getPayload().getUseDiscountCampaign().getSeasonal().getEveryAmount().getDiscount(), 4000);
        Assertions.assertEquals(response.getPayload().getTotalPrice(), "0.00");
    }

}
