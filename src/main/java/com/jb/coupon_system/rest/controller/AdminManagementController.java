package com.jb.coupon_system.rest.controller;

import com.jb.coupon_system.common.ResourceUtils;
import com.jb.coupon_system.data.entity.Company;
import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.entity.Customer;
import com.jb.coupon_system.rest.ClientSession;
import com.jb.coupon_system.rest.ex.InvalidLoginException;
import com.jb.coupon_system.service.AdminService;
import com.jb.coupon_system.service.CompanyService;
import com.jb.coupon_system.service.CustomerService;
import com.jb.coupon_system.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.jb.coupon_system.common.LoginType.ADMIN;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api")
public class AdminManagementController {

    private final AdminService adminService;
    private final CompanyService companyService;
    private final CustomerService customerService;
    private final Map<String, ClientSession> tokensMap;

    @Autowired
    public AdminManagementController(AdminService adminService, CompanyService companyService, CustomerService customerService, @Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.adminService = adminService;
        this.companyService = companyService;
        this.customerService = customerService;
        this.tokensMap = tokensMap;
    }

    /*Admin specified requests:*/
    @PostMapping("/admin/companies/add")
    public ResponseEntity<Company> saveCompany(@RequestBody Company company, @RequestParam String token) throws DuplicateEntryException, InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Company> optCompany = adminService.saveCompany(company);

        return optCompany.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/admin/companies/update")
    public ResponseEntity<Company> updateCompany(@RequestBody Company company, @RequestParam String token) throws DuplicateEntryException, InvalidLoginException, NoSuchIdException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Company> optCompany = adminService.updateCompany(company);

        optCompany.orElseThrow();

        return ResponseEntity.ok(optCompany.get());
    }

    @DeleteMapping("/admin/companies/{id}")
    public ResponseEntity<Void> deleteCompanyById(@PathVariable long id, @RequestParam String token) throws InvalidLoginException, NoSuchIdException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        adminService.deleteCompanyById(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/companies/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable long id, @RequestParam String token) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Company> optCompany = adminService.getCompanyById(id);

        return optCompany.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/admin/companies")
    public ResponseEntity<List<Company>> getAllCompanies(@RequestParam String token) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Company> companies = adminService.getAllCompanies();
        if (companies != null && !companies.isEmpty()) {
            return ResponseEntity.ok(companies);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin/customers/add")
    public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer, @RequestParam String token) throws DuplicateEntryException, InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Customer> optCustomer = adminService.saveCustomer(customer);

        return optCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/admin/customers/update")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @RequestParam String token) throws DuplicateEntryException, InvalidLoginException, NoSuchIdException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Customer> optCustomer = adminService.updateCustomer(customer);

        optCustomer.orElseThrow();

        return ResponseEntity.ok(optCustomer.get());
    }

    @DeleteMapping("/admin/customers/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable long id, @RequestParam String token) throws InvalidLoginException, NoSuchIdException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        adminService.deleteCustomerById(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/customers/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable long id, @RequestParam String token) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Customer> optCustomer = adminService.getCustomerById(id);

        return optCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/admin/customers")
    public ResponseEntity<List<Customer>> getAllCustomers(@RequestParam String token) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Customer> customers = adminService.getAllCustomers();
        if (customers != null && !customers.isEmpty()) {
            return ResponseEntity.ok(customers);
        }
        return ResponseEntity.noContent().build();
    }

    /*Company specified requests:*/
    @PostMapping("/admin/companies/coupons/add")
    public ResponseEntity<Coupon> saveCompanyCoupon(@RequestBody Coupon coupon, @RequestParam String token, @RequestParam long companyId) throws DuplicateEntryException, InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Coupon> optCoupon = companyService.saveCoupon(companyId, coupon);

        return optCoupon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/admin/companies/coupons/update")
    public ResponseEntity<Coupon> updateCompanyCoupon(@RequestBody Coupon coupon, @RequestParam String token, @RequestParam long companyId) throws DuplicateEntryException, InvalidLoginException, NoSuchIdException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Coupon> optCoupon = companyService.updateCoupon(companyId, coupon);

        return ResponseEntity.ok(optCoupon.get());//Checked in CompanyService.
    }

    @DeleteMapping("/admin/companies/coupons/{id}")
    public ResponseEntity<Void> deleteCompanyCouponById(@PathVariable long id, @RequestParam String token, @RequestParam long companyId) throws InvalidLoginException, NoSuchIdException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        companyService.deleteCouponById(companyId, id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/companies/coupons/{id}")
    public ResponseEntity<Coupon> getCompanyCouponById(@PathVariable long id, @RequestParam String token) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Coupon> optCoupon = companyService.getCouponById(id);

        return optCoupon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/admin/companies/coupons")
    public ResponseEntity<List<Coupon>> getAllCompanyCoupons(@RequestParam String token, @RequestParam long companyId) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> companyCoupons = companyService.getAllCompanyCoupons(companyId);
        if (companyCoupons != null && !companyCoupons.isEmpty()) {
            return ResponseEntity.ok(companyCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/companies/coupons/category")
    public ResponseEntity<List<Coupon>> getAllCompanyCouponsByCategory(@RequestParam String token, @RequestParam int category, @RequestParam long companyId) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> companyCoupons = companyService.getCompanyCouponsByCategory(companyId, category);
        if (companyCoupons != null && !companyCoupons.isEmpty()) {
            return ResponseEntity.ok(companyCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/companies/coupons/lower-than-price")
    public ResponseEntity<List<Coupon>> getAllCompanyCouponsLowerThanPrice(@RequestParam String token, @RequestParam double price, @RequestParam long companyId) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> companyCoupons = companyService.getCompanyCouponsLowerThanPrice(companyId, price);
        if (companyCoupons != null && !companyCoupons.isEmpty()) {
            return ResponseEntity.ok(companyCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/companies/coupons/before-end-date")
    public ResponseEntity<List<Coupon>> getAllCompanyCouponsBeforeDate(@RequestParam String token,
                                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                       @RequestParam LocalDateTime date,
                                                                       @RequestParam long companyId) throws InvalidLoginException {
        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> companyCoupons = companyService.getCompanyCouponsBeforeEndDate(companyId, date);
        if (companyCoupons != null && !companyCoupons.isEmpty()) {
            return ResponseEntity.ok(companyCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    /*Customer specified requests:*/
    @PostMapping("/admin/customers/coupons/purchase")
    public ResponseEntity<Coupon> purchaseCustomersCoupon(@RequestParam long couponId, @RequestParam String token, @RequestParam long customerId) throws DuplicateEntryException, ZeroCouponAmountException, InvalidLoginException, NoSuchIdException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Coupon> optCoupon = customerService.purchaseCoupon(customerId, couponId);

        optCoupon.orElseThrow();

        return ResponseEntity.ok(optCoupon.get());
    }

    @GetMapping("/admin/customers/coupons")
    public ResponseEntity<List<Coupon>> getPurchasedCustomersCoupons(@RequestParam String token, @RequestParam long customerId) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> purchasedCoupons = customerService.getMyPurchasedCoupons(customerId);
        if (purchasedCoupons != null && !purchasedCoupons.isEmpty()) {
            return ResponseEntity.ok(purchasedCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/customers/coupons/category")
    public ResponseEntity<List<Coupon>> getNonPurchasedCustomersCouponsByCategory(@RequestParam String token, @RequestParam int category, @RequestParam long customerId) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> couponsByCategory = customerService.getNonPurchasedCouponsByCategory(customerId, category);
        if (couponsByCategory != null && !couponsByCategory.isEmpty()) {
            return ResponseEntity.ok(couponsByCategory);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/customers/coupons/lower-than-price")
    public ResponseEntity<List<Coupon>> getNonPurchasedCustomersCouponsLowerThanPrice(@RequestParam String token, @RequestParam int price, @RequestParam long customerId) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> couponsLowerThanPrice = customerService.getNonPurchasedCouponsLowerThanPrice(customerId, price);
        if (couponsLowerThanPrice != null && !couponsLowerThanPrice.isEmpty()) {
            return ResponseEntity.ok(couponsLowerThanPrice);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/customers/coupons/before-end-date")
    public ResponseEntity<List<Coupon>> getAllCouponsBeforeDate(@RequestParam String token,
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                @RequestParam LocalDateTime date) throws InvalidLoginException {
        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> couponsBeforeDate = customerService.getAllCouponsBeforeDate(date);
        if (couponsBeforeDate != null && !couponsBeforeDate.isEmpty()) {
            return ResponseEntity.ok(couponsBeforeDate);
        }
        return ResponseEntity.noContent().build();
    }


    /**
     * A method that ensures that only an admin can have a logged in admins abilities
     *
     * @param token provided by loginController
     * @throws InvalidLoginException
     */
    private void verifyTokensLoginTypeOrThrow(String token) throws InvalidLoginException {
        if (!token.startsWith(ADMIN.name())) {
            String msg = String.format("You cannot perform ADMIN functions with %s token!", token);
            throw new InvalidLoginException(msg);
        }
    }
}