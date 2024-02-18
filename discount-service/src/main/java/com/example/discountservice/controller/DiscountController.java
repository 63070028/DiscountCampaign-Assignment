package com.example.discountservice.controller;

import com.example.discountservice.models.DiscountRequest;
import com.example.discountservice.models.DiscountResponse;
import com.example.discountservice.services.DiscountService;
import com.example.discountservice.util.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discount")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping("/campaigns")
    public ResponseEntity<DiscountResponse> campaigns(@RequestBody DiscountRequest request) throws BaseException {
        DiscountResponse response = this.discountService.calculateTotalPrice(request);
        return ResponseEntity.ok(response);
    }
}
