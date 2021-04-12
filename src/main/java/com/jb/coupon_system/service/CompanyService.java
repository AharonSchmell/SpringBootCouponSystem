package com.jb.coupon_system.service;

import com.jb.coupon_system.data.entity.Company;
import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.rest.ex.InvalidLoginException;
import com.jb.coupon_system.service.ex.DuplicateEntryException;
import com.jb.coupon_system.service.ex.NoSuchIdException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CompanyService {
    /**
     * A method that creates a new coupon using a specified company id
     * disregarding the coupons' id used in the coupon parameter.
     *
     * @param companyId
     * @param coupon
     * @return
     * @throws DuplicateEntryException if the coupons title is not unique.
     */
    Optional<Coupon> saveCoupon(long companyId, Coupon coupon) throws DuplicateEntryException;

    /**
     * A method that updates a coupon after verifying the coupons' id, the company's id
     * and the new title if given.
     *
     * @param companyId
     * @param coupon
     * @return
     * @throws NoSuchIdException       if the coupon is not found.
     * @throws InvalidLoginException   if companyId != coupons' companyId.
     * @throws DuplicateEntryException if the coupons title is not unique.
     */
    Optional<Coupon> updateCoupon(long companyId, Coupon coupon) throws NoSuchIdException, DuplicateEntryException, InvalidLoginException;

    /**
     * A method that deletes a coupon after verifying the coupons' id and the companies' id.
     *
     * @param companyId
     * @param couponId
     * @throws NoSuchIdException     if the coupon is not found.
     * @throws InvalidLoginException if companyId != coupons' companyId.
     */
    void deleteCouponById(long companyId, long couponId) throws NoSuchIdException, InvalidLoginException;

    /**
     * A method that retrieves a specified company's coupon using coupons' id.
     *
     * @param couponId
     * @return Optional<Coupon>
     */
    Optional<Coupon> getCouponById(long couponId);

    /**
     * A method that retrieves a list of a specific company's coupons,
     * sorted by the amount of times a coupon was sold- from most to least.
     *
     * @param companyId
     * @return List<Coupon>
     */
    List<Coupon> getAllCompanyCoupons(long companyId);

    /**
     * A method that retrieves a categorically specified list of a specific company's coupons.
     *
     * @param companyId
     * @param category
     * @return List<Coupon>
     */
    List<Coupon> getCompanyCouponsByCategory(long companyId, int category);

    /**
     * A method that retrieves a price range specified list of a specific company's coupons.
     *
     * @param companyId
     * @param price
     * @return List<Coupon>
     */
    List<Coupon> getCompanyCouponsLowerThanPrice(long companyId, double price);

    /**
     * A method that retrieves a date range specified list of a specific company's coupons.
     *
     * @param companyId
     * @param date
     * @return List<Coupon>
     */
    List<Coupon> getCompanyCouponsBeforeEndDate(long companyId, LocalDateTime date);

    Optional<Company> getCompanyById(long companyId);

    Optional<Company> updateCompany(Company company) throws NoSuchIdException, DuplicateEntryException;
}