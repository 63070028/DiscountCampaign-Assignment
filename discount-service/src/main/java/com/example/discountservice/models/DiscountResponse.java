package com.example.discountservice.models;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DiscountResponse {
    private DiscountDetail payload;
}
