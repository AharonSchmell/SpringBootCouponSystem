package com.jb.coupon_system.rest.model;

public class Token {
    private String tokenName;

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public Token(String tokenName) {
        this.tokenName = tokenName;
    }
}
