package com.jb.coupon_system.rest.ex;

public class InvalidLoginException extends Exception {
    /*An exception that is thrown when invalid credentials are being used.
    examples: 1. email/password - Logging in with email/password that don't exist or match.
              2. token - Using expired or wrong token (CUSTOMER instead of COMPANY).
              3. id - Trying to delete a coupon with wrong company id.
    */
    public InvalidLoginException(String msg) {
        super(msg);
    }
}
