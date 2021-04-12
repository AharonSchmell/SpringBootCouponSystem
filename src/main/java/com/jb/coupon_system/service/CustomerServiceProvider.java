package com.jb.coupon_system.service;

import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.entity.Customer;
import com.jb.coupon_system.data.repo.CouponRepository;
import com.jb.coupon_system.data.repo.CustomerRepository;
import com.jb.coupon_system.service.ex.DuplicateEntryException;
import com.jb.coupon_system.service.ex.NoSuchIdException;
import com.jb.coupon_system.service.ex.ZeroCouponAmountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceProvider implements CustomerService {

    private final CouponRepository couponRepo;
    private final CustomerRepository customerRepo;

    @Autowired
    public CustomerServiceProvider(CouponRepository couponRepo, CustomerRepository customerRepo) {
        this.couponRepo = couponRepo;
        this.customerRepo = customerRepo;
    }

    @Override
    public Optional<Coupon> purchaseCoupon(long customerId, long couponId) throws DuplicateEntryException, ZeroCouponAmountException, NoSuchIdException {
        /*Make sure the coupon exists*/
        Optional<Coupon> optCoupon = couponRepo.findById(couponId);
        if (optCoupon.isPresent()) {
            Coupon coupon = optCoupon.get();

            /*Make sure the existing coupons' amount indicates there are more coupons to be purchased*/
            int amount = coupon.getAmount();
            if (amount > 0) {

                /*Make sure the customer exists*/
                Optional<Customer> optCustomer = customerRepo.findById(customerId);
                if (optCustomer.isPresent()) {

                    /*Decrement and update coupon amount*/
                    coupon.setAmount(--amount);
                    couponRepo.save(coupon);

                    /*Add new saved coupon to the customers list of coupons,
                    providing said customer doesn't already own the coupon*/
                    Customer customer = optCustomer.get();
                    customer.getCoupons().add(coupon);
                    try {
                        customerRepo.save(customer);
                    } catch (DataIntegrityViolationException e) {

                        /*since the customer already owns the coupon and the purchase didn't go through,
                        reinstate the coupon back to its previous amount*/
                        coupon.setAmount(++amount);
                        couponRepo.save(coupon);

                        String msg = String.format("customer with id = %d has already purchased coupon with id = %d", customerId, couponId);
                        throw new DuplicateEntryException(msg);
                    }
                    /*Successful purchase*/
                    return Optional.of(coupon);
                }
                String msg = String.format("A customer with id %d does not exist", customerId);
                throw new NoSuchIdException(msg);
            }
            String msg = String.format("Unable to purchase coupon with id %d , no coupons left!", couponId);
            throw new ZeroCouponAmountException(msg);
        }
        String msg = String.format("A coupon with id %d does not exist", couponId);
        throw new NoSuchIdException(msg);
    }


    @Override
    public Optional<Coupon> returnCoupon(long customerId, long couponId) throws NoSuchIdException {
        /*Make sure the coupon exists*/
        Optional<Coupon> optCoupon = couponRepo.findById(couponId);
        if (optCoupon.isPresent()) {
            Coupon coupon = optCoupon.get();

            /*Make sure the customer exists*/
            Optional<Customer> optCustomer = customerRepo.findById(customerId);
            if (optCustomer.isPresent()) {

                /*increment and update coupon amount*/
                int amount = coupon.getAmount();
                coupon.setAmount(++amount);
                couponRepo.save(coupon);

                /*Remove coupon from the customers list of coupons*/
                Customer customer = optCustomer.get();
                customer.getCoupons().remove(coupon);
                customerRepo.save(customer);

                /*Successful return of coupon*/
                return Optional.of(coupon);
            }
            String msg = String.format("A customer with id %d does not exist", customerId);
            throw new NoSuchIdException(msg);
        }
        String msg = String.format("A coupon with id %d does not exist", couponId);
        throw new NoSuchIdException(msg);
    }

    @Override
    public List<Coupon> getMyPurchasedCoupons(long customerId) {
        return customerRepo.getMyPurchasedCoupons(customerId);
    }

    @Override
    public Optional<Integer> getMyPurchasedCouponsAmount(long customerId) {
        return Optional.of(customerRepo.getMyPurchasedCoupons(customerId).size());
    }

    @Override
    public List<Coupon> getNonPurchasedCouponsByCategory(long customerId, int category) {
        return customerRepo.getNonPurchasedCouponsByCategory(customerId, category);
    }

    @Override
    public List<Coupon> getNonPurchasedCouponsLowerThanPrice(long customerId, double price) {
        return customerRepo.getNonPurchasedCouponsLowerThanPrice(customerId, price);
    }

    @Override
    public List<Coupon> getAllCouponsBeforeDate(LocalDateTime date) {
        return couponRepo.findAllByEndDateBefore(date);
    }

    @Override
    public Optional<Customer> updateCustomer(Customer customer) throws NoSuchIdException, DuplicateEntryException {
        /*Make sure the customer being updated exists*/
        Optional<Customer> optCustomer = customerRepo.findById(customer.getId());
        if (optCustomer.isEmpty()) {
            String msg = String.format("A customer with id %d does not exist", customer.getId());
            throw new NoSuchIdException(msg);
        }

        /*Make sure that the updated customer has a unique email*/
        try {
            return Optional.of(customerRepo.save(customer));
        } catch (DataIntegrityViolationException e) {
            String msg = String.format("A customer with the email %s has already been used", customer.getEmail());
            throw new DuplicateEntryException(msg);
        }
    }

    @Override
    public Optional<Customer> getCustomerByEmailAndPassword(String email, String password) {
        return customerRepo.findCustomerByEmailAndPassword(email, password);
    }

    @Override
    public Optional<Customer> getCustomerById(long customerId) {
        return customerRepo.findById(customerId);
    }

    @Override
    public List<Coupon> getAllNonPurchasedCoupons(long customerId) {
        return customerRepo.getAllNonPurchasedCoupons(customerId);
    }
}