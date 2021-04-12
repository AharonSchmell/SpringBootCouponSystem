package com.jb.coupon_system.service.ex;

public class DuplicateEntryException extends Exception {
    /*An exception that is thrown when trying to create or update data without following its unique restrictions.*/
    public DuplicateEntryException(String msg) {
        super(msg);
    }
}
