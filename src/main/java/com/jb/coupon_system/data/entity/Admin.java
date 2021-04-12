package com.jb.coupon_system.data.entity;

import com.jb.coupon_system.rest.ex.InvalidLoginException;

import java.util.Optional;

/**
 * An entity class of sorts (Not an @Entity).
 * There is no Admin table in the data base.
 * All Admin credentials are privately stored in this class.
 */
public class Admin {
    private static final Integer ID = -1;//ID = -1 to be certain it's not confused with other valid ids.
    private static final String EMAIL = "admin@gmail.com";
    private static final String PASSWORD = "1234";

    /**
     * A method which is unique only to the admin, since there is no admin table in the data base.
     * The method is used in order to get the admins' id after checking credentials.
     *
     * @param email
     * @param password
     * @return Optional.of(ID) if credentials are correct, Optional.empty() otherwise
     */
    public static Optional<Integer> getAdminByEmailAndPassword(String email, String password) throws InvalidLoginException {
        if (EMAIL.equals(email) && PASSWORD.equals(password)) {
            return Optional.of(ID);
        }
        String msg = String.format("Unable to login as Admin with provided credentials using email = %s and password = %s", email, password);
        throw new InvalidLoginException(msg);
    }
}