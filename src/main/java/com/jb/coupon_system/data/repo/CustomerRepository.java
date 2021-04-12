package com.jb.coupon_system.data.repo;

import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * A JPA query method used in order to find a specific customer by its email and password
     *
     * @param email    of customer to find
     * @param password of same customer
     * @return Optional<Customer>
     */
    Optional<Customer> findCustomerByEmailAndPassword(String email, String password);

    /**
     * An HQL query method used in order to find all of a customers' purchased coupons (represented in the customer coupon joined table)
     *
     * @param customerId of the specified customer
     * @return List<Coupon>
     */
    @Query("select c from Coupon as c inner join c.customers as cust where cust.id =:customerId")
    List<Coupon> getMyPurchasedCoupons(long customerId);

    /**
     * An HQL query method used in order to find all of a customers' categorically specified non purchased coupons (meaning coupons that do not appear in the customer coupon joined table)
     *
     * @param customerId of the specified customer
     * @param category   specified category
     * @return List<Coupon>
     */
    @Query("select c from Coupon as c where c not in(select c from Coupon as c inner join c.customers as cust where cust.id =:customerId) " +
            "and category =:category")
    List<Coupon> getNonPurchasedCouponsByCategory(long customerId, int category);

    /**
     * An HQL query method used in order to find all of a customers' price specified non purchased coupons (meaning coupons that do not appear in the customer coupon joined table)
     *
     * @param customerId of the specified customer
     * @param price      specified price (any coupon lower than this price will be in the list)
     * @return List<Coupon>
     */
    @Query("select c from Coupon as c where c not in(select c from Coupon as c inner join c.customers as cust where cust.id =:customerId) " +
            "and price <:price")
    List<Coupon> getNonPurchasedCouponsLowerThanPrice(long customerId, double price);

    /**
     * An HQL query method used in order to find all of a customers non purchased coupons.
     *
     * @param customerId of the specified customer
     * @return List<Coupon>
     */
    @Query("select c from Coupon as c where c not in(select c from Coupon as c inner join c.customers as cust where cust.id =:customerId) ")
    List<Coupon> getAllNonPurchasedCoupons(long customerId);
}