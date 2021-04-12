package com.jb.coupon_system.data.repo;

import com.jb.coupon_system.data.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    /**
     * A JPA query method used in order to find all company coupons using a company's id.
     *
     * @param id of company to find.
     * @return List<Coupon>
     */
    List<Coupon> findAllByCompanyId(long id);

    /**
     * A JPA query method used in order to find all categorically specified company coupons using a company's id and category.
     *
     * @param id       of company to find.
     * @param category specified category (any coupon belonging to the company, with this category will be in the list).
     * @return List<Coupon>
     */
    List<Coupon> findAllByCompanyIdAndCategory(long id, int category);

    /**
     * A JPA query method used in order to find all price specified company coupons using a company's id and price.
     *
     * @param id    of company to find.
     * @param price specified price (any coupon belonging to the company, lower than this price will be in the list).
     * @return List<Coupon>
     */
    List<Coupon> findAllByPriceLessThanAndCompanyId(double price, long id);

    /**
     * A JPA query method used in order to find all date specified coupons using a date.
     *
     * @param date specified date (any coupon, before this date will be in the list).
     * @return List<Coupon>
     */
    List<Coupon> findAllByEndDateBefore(LocalDateTime date);

    /**
     * A JPA query method used in order to find all date specified company coupons using a company's id and date.
     *
     * @param id   of company to find.
     * @param date specified date (any coupon belonging to the company, before this date will be in the list).
     * @return List<Coupon>
     */
    List<Coupon> findAllByEndDateBeforeAndCompanyId(LocalDateTime date, long id);

    /**
     * An HQL query method called upon by a scheduled cleanup method in order to erase expired coupons.
     */
    @Modifying
    @Transactional
    @Query("delete from Coupon c where c.endDate <= CURRENT_TIME")
    void deleteAllExpiredCoupons();

    /**
     * An HQL query method that counts the amount of times a coupon was sold to a customer.
     */
    @Query("select count(c) from Customer as c where c in" +
            "(select c from Customer as c inner join c.coupons as coup where coup.id =:couponId)")
    int findCouponAmountSold(long couponId);
}