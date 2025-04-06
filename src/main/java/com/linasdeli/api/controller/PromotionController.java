package com.linasdeli.api.controller;

import com.linasdeli.api.domain.Promotion;
import com.linasdeli.api.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;     // ✅ 추가
import org.springframework.http.HttpStatus;       // ✅ 추가
import org.springframework.web.bind.annotation.*;

import java.util.List;                             // ✅ 추가

@Slf4j
@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/active")
    public ResponseEntity<List<Promotion>> getActivePromotions() {
        List<Promotion> activePromos = promotionService.getActivePromotions();
        return new ResponseEntity<>(activePromos, HttpStatus.OK);
    }
}