package com.jb.coupon_system.rest.controller;

import com.jb.coupon_system.common.ResourceUtils;
import com.jb.coupon_system.data.entity.Company;
import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.rest.ClientSession;
import com.jb.coupon_system.rest.ex.InvalidLoginException;
import com.jb.coupon_system.service.CompanyService;
import com.jb.coupon_system.service.ex.DuplicateEntryException;
import com.jb.coupon_system.service.ex.NoSuchIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.jb.coupon_system.common.LoginType.COMPANY;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api")
public class CompanyManagementController {

    private final CompanyService service;
    private final Map<String, ClientSession> tokensMap;

    @Autowired
    public CompanyManagementController(CompanyService service, @Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.service = service;
        this.tokensMap = tokensMap;
    }

    @GetMapping("companies/token")
    public ResponseEntity<Company> getCompanyByToken(@RequestParam String token) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        long companyId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Company> optCompany = service.getCompanyById(companyId);

        return optCompany.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("companies/update")
    public ResponseEntity<Company> updateCompany(@RequestBody Company company, @RequestParam String token) throws DuplicateEntryException, InvalidLoginException, NoSuchIdException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Company> optCompany = service.updateCompany(company);

        optCompany.orElseThrow();

        return ResponseEntity.ok(optCompany.get());
    }

    @PostMapping("/companies/coupons/add")
    public ResponseEntity<Coupon> saveCoupon(@RequestBody Coupon coupon, @RequestParam String token) throws DuplicateEntryException, InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        long companyId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Coupon> optCoupon = service.saveCoupon(companyId, coupon);

        return optCoupon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/companies/coupons/update")
    public ResponseEntity<Coupon> updateCoupon(@RequestBody Coupon coupon, @RequestParam String token) throws DuplicateEntryException, InvalidLoginException, NoSuchIdException {

        verifyTokensLoginTypeOrThrow(token);

        long companyId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Coupon> optCoupon = service.updateCoupon(companyId, coupon);

        return ResponseEntity.ok(optCoupon.get());//Checked in CompanyService.
    }

    @DeleteMapping("/companies/coupons/{id}")
    public ResponseEntity<Void> deleteCouponById(@PathVariable long id, @RequestParam String token) throws InvalidLoginException, NoSuchIdException {

        verifyTokensLoginTypeOrThrow(token);

        long companyId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        service.deleteCouponById(companyId, id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/companies/coupons/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable long id, @RequestParam String token) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        Optional<Coupon> optCoupon = service.getCouponById(id);

        return optCoupon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/companies/coupons")
    public ResponseEntity<List<Coupon>> getAllCompanyCouponsByMostSold(@RequestParam String token) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        long companyId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> companyCoupons = service.getAllCompanyCoupons(companyId);
        if (companyCoupons != null && !companyCoupons.isEmpty()) {
            return ResponseEntity.ok(companyCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/companies/coupons/category")
    public ResponseEntity<List<Coupon>> getAllCompanyCouponsByCategory(@RequestParam String token, @RequestParam int category) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        long companyId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> companyCoupons = service.getCompanyCouponsByCategory(companyId, category);
        if (companyCoupons != null && !companyCoupons.isEmpty()) {
            return ResponseEntity.ok(companyCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/companies/coupons/lower-than-price")
    public ResponseEntity<List<Coupon>> getAllCompanyCouponsLowerThanPrice(@RequestParam String token, @RequestParam double price) throws InvalidLoginException {

        verifyTokensLoginTypeOrThrow(token);

        long companyId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> companyCoupons = service.getCompanyCouponsLowerThanPrice(companyId, price);
        if (companyCoupons != null && !companyCoupons.isEmpty()) {
            return ResponseEntity.ok(companyCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/companies/coupons/before-end-date")
    public ResponseEntity<List<Coupon>> getAllCompanyCouponsBeforeDate(@RequestParam String token,
                                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                       @RequestParam LocalDateTime date) throws InvalidLoginException {
        verifyTokensLoginTypeOrThrow(token);

        long companyId = ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        List<Coupon> companyCoupons = service.getCompanyCouponsBeforeEndDate(companyId, date);
        if (companyCoupons != null && !companyCoupons.isEmpty()) {
            return ResponseEntity.ok(companyCoupons);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * A method that ensures that only a company can have a logged in companies abilities.
     *
     * @param token provided by loginController.
     * @throws InvalidLoginException
     */
    private void verifyTokensLoginTypeOrThrow(String token) throws InvalidLoginException {
        if (!token.startsWith(COMPANY.name())) {
            String msg = String.format("You cannot perform COMPANY functions with %s token!", token);
            throw new InvalidLoginException(msg);
        }
    }
}