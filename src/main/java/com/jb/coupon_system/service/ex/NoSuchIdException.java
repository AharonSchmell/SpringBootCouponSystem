package com.jb.coupon_system.service.ex;

public class NoSuchIdException extends Exception {
    /*An exception that is thrown when trying to modify (update or delete) data using a non existent id.
      (As appose to a similar case when trying to retrieve data which will return a No Content HTTP status)*/
    public NoSuchIdException(String msg) {
        super(msg);
    }
}
