package com.jb.coupon_system.service;

import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.entity.Customer;
import com.jb.coupon_system.service.ex.DuplicateEntryException;
import com.jb.coupon_system.service.ex.NoSuchIdException;
import com.jb.coupon_system.service.ex.ZeroCouponAmountException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    /**
     * A method used in order to add a coupon to a customer.
     * The ids' of customer and coupon will be added in the customer_coupon joined table
     * and the amount column in the coupon will be decremented.
     *
     * @param customerId
     * @param couponId
     * @return Optional<Coupon>
     * @throws DuplicateEntryException   If the coupon amount column is not > 0.
     * @throws ZeroCouponAmountException If the customer has already purchased the coupon.
     * @throws NoSuchIdException         If the coupon is not found.
     */
    Optional<Coupon> purchaseCoupon(long customerId, long couponId) throws DuplicateEntryException, ZeroCouponAmountException, NoSuchIdException;

    /**
     * A method that retrieves a list of a customers purchased coupons.
     *
     * @param customerId
     * @return List<Coupon>
     */
    List<Coupon> getMyPurchasedCoupons(long customerId);

    /**
     * A method that retrieves the number of a customers purchased coupons.
     *
     * @param customerId
     * @return Optional<Integer>
     */
    Optional<Integer> getMyPurchasedCouponsAmount(long customerId);

    /**
     * A method that retrieves a categorically specified list of the coupons that have not yet been purchased by the customer.
     *
     * @param customerId
     * @param category
     * @return List<Coupon>
     */
    List<Coupon> getNonPurchasedCouponsByCategory(long customerId, int category);

    /**
     * A method that retrieves a price range specified list of the coupons that have not yet been purchased by the customer.
     *
     * @param customerId
     * @param price
     * @return List<Coupon>
     */
    List<Coupon> getNonPurchasedCouponsLowerThanPrice(long customerId, double price);

    /**
     * A method that retrieves a date range specified list of all the coupons.
     *
     * @param date
     * @return List<Coupon>
     */
    List<Coupon> getAllCouponsBeforeDate(LocalDateTime date);

    Optional<Customer> updateCustomer(Customer customer) throws NoSuchIdException, DuplicateEntryException;

    Optional<Customer> getCustomerByEmailAndPassword(String email, String password);

    Optional<Customer> getCustomerById(long id);

    List<Coupon> getAllNonPurchasedCoupons(long customerId);

    Optional<Coupon> returnCoupon(long customerId, long couponId) throws NoSuchIdException;
}
