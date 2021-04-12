package com.jb.coupon_system.rest.controller;

import com.jb.coupon_system.rest.ex.InvalidLoginException;
import com.jb.coupon_system.rest.model.ErrorResponse;
import com.jb.coupon_system.service.ex.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(InvalidLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorized(InvalidLoginException ex) {
        return ErrorResponse.ofNow(ex.getMessage());
    }

    @ExceptionHandler(DuplicateEntryException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUnauthorized(DuplicateEntryException ex) {
        return ErrorResponse.ofNow(ex.getMessage());
    }

    @ExceptionHandler(ZeroCouponAmountException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnauthorized(ZeroCouponAmountException ex) {
        return ErrorResponse.ofNow(ex.getMessage());
    }

    @ExceptionHandler(NoSuchIdException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnauthorized(NoSuchIdException ex) {
        return ErrorResponse.ofNow(ex.getMessage());
    }
}