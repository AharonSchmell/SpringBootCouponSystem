package com.jb.coupon_system.service;

import com.jb.coupon_system.data.entity.Company;
import com.jb.coupon_system.data.entity.Coupon;
import com.jb.coupon_system.data.repo.CompanyRepository;
import com.jb.coupon_system.data.repo.CouponRepository;
import com.jb.coupon_system.rest.ex.InvalidLoginException;
import com.jb.coupon_system.service.ex.DuplicateEntryException;
import com.jb.coupon_system.service.ex.NoSuchIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompanyServiceProvider implements CompanyService {

    private CouponRepository couponRepo;
    private CompanyRepository companyRepo;

    @Autowired
    public CompanyServiceProvider(CouponRepository couponRepo, CompanyRepository companyRepo) {
        this.couponRepo = couponRepo;
        this.companyRepo = companyRepo;
    }

    @Override
    public Optional<Coupon> saveCoupon(long companyId, Coupon coupon) throws DuplicateEntryException {
        /*make sure a new coupon is put in the data base by setting coupon id field to 0*/
        coupon.setId(0);

        /*Make sure the coupons' companyId field is set to the company logged in*/
        Optional<Company> optCompany = companyRepo.findById(companyId);
        optCompany.ifPresent(coupon::setCompany);

        /*Make sure that the coupon has a unique title*/
        try {
            return Optional.of(couponRepo.save(coupon));
        } catch (DataIntegrityViolationException e) {
            String msg = String.format("A coupon with the title %s has already been used", coupon.getTitle());
            throw new DuplicateEntryException(msg);
        }
    }

    @Override
    public Optional<Coupon> updateCoupon(long companyId, Coupon coupon) throws NoSuchIdException, DuplicateEntryException, InvalidLoginException {
        /*Make sure the coupon being updated exists*/
        Optional<Coupon> optCoupon = couponRepo.findById(coupon.getId());
        if (optCoupon.isEmpty()) {
            String msg = String.format("A coupon with id %d does not exist", coupon.getId());
            throw new NoSuchIdException(msg);

            /*Make sure the company id field in the coupon matches the logged in company's id*/
        } else if (optCoupon.get().getCompany().getId() != companyId) {
            throw new InvalidLoginException("You cannot update a coupon that belongs to a different company");

            /*Make sure the updated coupons' companyId field will always be set to the company logged in*/
        } else {
            Optional<Company> optCompany = companyRepo.findById(companyId);
            optCompany.ifPresent(coupon::setCompany);
        }

        /*Make sure that, if title field has been changed, the coupon still has a unique title*/
        try {
            return Optional.of(couponRepo.save(coupon));
        } catch (DataIntegrityViolationException e) {
            String msg = String.format("A coupon with the title %s has already been used", coupon.getTitle());
            throw new DuplicateEntryException(msg);
        }
    }

    @Override
    public void deleteCouponById(long companyId, long couponId) throws NoSuchIdException, InvalidLoginException {
        /*Make sure the coupon being deleted exists*/
        Optional<Coupon> optCoupon = couponRepo.findById(couponId);
        if (optCoupon.isEmpty()) {
            String msg = String.format("coupon with id %d does not exist", couponId);
            throw new NoSuchIdException(msg);

            /*Make sure the company id field in the coupon matches the logged in company's id*/
        } else if (optCoupon.get().getCompany().getId() != companyId) {
            throw new InvalidLoginException("You cannot delete a coupon that belongs to a different company");
        }
        couponRepo.deleteById(couponId);
    }

    @Override
    public Optional<Coupon> getCouponById(long couponId) {
        return couponRepo.findById(couponId);
    }

    @Override
    public List<Coupon> getAllCompanyCoupons(long companyId) {
        /*get company coupons*/
        List<Coupon> companyCoupons = couponRepo.findAllByCompanyId(companyId);
        /*create map of coupons with the amount of times the coupon was sold*/
        Map<Coupon, Integer> map = new HashMap<>();
        for (Coupon coupon : companyCoupons) {
            map.put(coupon, couponRepo.findCouponAmountSold(coupon.getId()));
        }
        /*sort coupons by most bought to least bought*/
        return companyCoupons.stream()
                .sorted(Comparator.comparing(map::get).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Coupon> getCompanyCouponsByCategory(long companyId, int category) {
        return couponRepo.findAllByCompanyIdAndCategory(companyId, category);
    }

    @Override
    public List<Coupon> getCompanyCouponsLowerThanPrice(long companyId, double price) {
        return couponRepo.findAllByPriceLessThanAndCompanyId(price, companyId);
    }

    @Override
    public List<Coupon> getCompanyCouponsBeforeEndDate(long companyId, LocalDateTime date) {
        return couponRepo.findAllByEndDateBeforeAndCompanyId(date, companyId);
    }

    @Override
    public Optional<Company> getCompanyById(long companyId) {
        return companyRepo.findById(companyId);
    }

    @Override
    public Optional<Company> updateCompany(Company company) throws NoSuchIdException, DuplicateEntryException {
        /*Make sure the company being updated exists*/
        Optional<Company> optCompany = companyRepo.findById(company.getId());
        if (optCompany.isEmpty()) {
            String msg = String.format("A company with id %d does not exist", company.getId());
            throw new NoSuchIdException(msg);
        }

        /*Make sure that the company being updated has a unique name*/
        try {
            return Optional.of(companyRepo.save(company));
        } catch (DataIntegrityViolationException e) {
            String msg = String.format("A company with the name %s has already been used", company.getName());
            throw new DuplicateEntryException(msg);
        }
    }
}