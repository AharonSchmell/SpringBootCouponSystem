package com.jb.coupon_system.rest.controller;

import com.jb.coupon_system.common.ResourceUtils;
import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.entity.Customer;
import com.jb.coupon_system.rest.ClientSession;
import com.jb.coupon_system.rest.ex.InvalidLoginException;
import com.jb.coupon_system.service.CustomerService;
import com.jb.coupon_system.service.ex.DuplicateEntryException;
import com.jb.coupon_system.service.ex.NoSuchIdException;
import com.jb.coupon_system.service.ex.ZeroCouponAmountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.jb.coupon_system.common.LoginType.CUSTOMER;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api")
public class CustomerManagementController {

    private final CustomerService service;
    private final Map<String, ClientSession> tokensMap;

    @Autowired
    public CustomerManagementController(CustomerService service, @Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.service = service;
        this.tokensMap = tokensMap;
    }

    @GetMapping("customers/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable long id, @RequestParam String token) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Customer> optCustomer = service.getCustomerById(id);

        return optCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("customers/token")
    public ResponseEntity<Customer> getCustomerByToken(@RequestParam String token) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        long customerId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Customer> optCustomer = service.getCustomerById(customerId);

        return optCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("customers")
    public ResponseEntity<Customer> getCustomerByEmailAndPassword(@RequestParam String email, @RequestParam String password, @RequestParam String token) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Customer> optCustomer = service.getCustomerByEmailAndPassword(email, password);

        return optCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("customers/update")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @RequestParam String token) throws DuplicateEntryException, InvalidLoginException, NoSuchIdException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Customer> optCustomer = service.updateCustomer(customer);

        optCustomer.orElseThrow();

        return ResponseEntity.ok(optCustomer.get());
    }

    @PostMapping("/customers/coupons/purchase")
    public ResponseEntity<Coupon> purchaseCoupon(@RequestParam Long couponId, @RequestParam String token) throws DuplicateEntryException, ZeroCouponAmountException, InvalidLoginException, NoSuchIdException {

        verifyTokensLoginTypeOrThrow(token);

        long customerId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Coupon> optCoupon = service.purchaseCoupon(customerId, couponId);

        return ResponseEntity.ok(optCoupon.get());//Checked in CustomerService
    }

    @PostMapping("/customers/coupons/purchase/cancel")
    public ResponseEntity<Coupon> returnCoupon(@RequestParam Long couponId, @RequestParam String token) throws DuplicateEntryException, ZeroCouponAmountException, InvalidLoginException, NoSuchIdException {

        verifyTokensLoginTypeOrThrow(token);

        long customerId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Coupon> optCoupon = service.returnCoupon(customerId, couponId);

        return ResponseEntity.ok(optCoupon.get());//Checked in CustomerService
    }

    @GetMapping("/customers/coupons")
    public ResponseEntity<List<Coupon>> getMyPurchasedCoupons(@RequestParam String token) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        long customerId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> purchasedCoupons = service.getMyPurchasedCoupons(customerId);
        if (purchasedCoupons != null && !purchasedCoupons.isEmpty()) {
            return ResponseEntity.ok(purchasedCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("customers/coupons/amount")
    public ResponseEntity<Integer> getMyPurchasedCouponsAmount(@RequestParam String token) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        long customerId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Integer> optCustomerAmount = service.getMyPurchasedCouponsAmount(customerId);

        return optCustomerAmount.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/customers/coupons/category")
    public ResponseEntity<List<Coupon>> getNonPurchasedCouponsByCategory(@RequestParam String token, @RequestParam int category) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        long customerId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> couponsByCategory = service.getNonPurchasedCouponsByCategory(customerId, category);
        if (couponsByCategory != null && !couponsByCategory.isEmpty()) {
            return ResponseEntity.ok(couponsByCategory);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customers/coupons/all")
    public ResponseEntity<List<Coupon>> getNonPurchasedCouponsByCategory(@RequestParam String token) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        long customerId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> allCoupons = service.getAllNonPurchasedCoupons(customerId);
        if (allCoupons != null && !allCoupons.isEmpty()) {
            return ResponseEntity.ok(allCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customers/coupons/lower-than-price")
    public ResponseEntity<List<Coupon>> getNonPurchasedCouponsLowerThanPrice(@RequestParam String token, @RequestParam int price) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        long customerId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> couponsLowerThanPrice = service.getNonPurchasedCouponsLowerThanPrice(customerId, price);
        if (couponsLowerThanPrice != null && !couponsLowerThanPrice.isEmpty()) {
            return ResponseEntity.ok(couponsLowerThanPrice);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customers/coupons/before-end-date")
    public ResponseEntity<List<Coupon>> getAllCouponsBeforeDate(@RequestParam String token,
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                @RequestParam LocalDateTime date) throws InvalidLoginException {
        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> couponsBeforeDate = service.getAllCouponsBeforeDate(date);
        if (couponsBeforeDate != null && !couponsBeforeDate.isEmpty()) {
            return ResponseEntity.ok(couponsBeforeDate);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * A method that ensures that only a customer can have a logged in customers abilities.
     *
     * @param token provided by loginController
     * @throws InvalidLoginException
     */
    private void verifyTokensLoginTypeOrThrow(String token) throws InvalidLoginException {
        if (!token.startsWith(CUSTOMER.name())) {
            String msg = String.format("You cannot perform CUSTOMER functions with %s token!", token);
            throw new InvalidLoginException(msg);
        }
    }
}