package com.jb.coupon_system.rest;

import com.jb.coupon_system.data.repo.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ScheduledCleanUp {
    private static final long HALF_AN_HOUR = 1_800_000;

    private final Map<String, ClientSession> tokensMap;
    private CouponRepository couponRepo;

    @Autowired
    public ScheduledCleanUp(Map<String, ClientSession> tokensMap, CouponRepository couponRepo) {
        this.tokensMap = tokensMap;
        this.couponRepo = couponRepo;
    }

    @Scheduled(fixedRateString = "${rate.delete.expired.coupons}")
    public void deleteAllExpiredCoupons() {
        couponRepo.deleteAllExpiredCoupons();
    }

    @Scheduled(initialDelayString = "${initial.delay.delete.expired.tokens}", fixedRateString = "${rate.delete.expired.tokens}")
    private void deleteExpiredTokens() {
        long now = System.currentTimeMillis();
        tokensMap.keySet().removeIf(key -> now - tokensMap.get(key).getLastAccessedMillis() > HALF_AN_HOUR);
    }
}