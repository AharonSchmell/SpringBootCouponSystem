package com.jb.coupon_system.service.ex;

public class ZeroCouponAmountException extends Exception {
    /*A specific exception that is thrown when trying to purchase a coupon with amount column == 0*/
    public ZeroCouponAmountException(String msg) {
        super(msg);
    }
}
