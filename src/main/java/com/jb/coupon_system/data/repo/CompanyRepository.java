package com.jb.coupon_system.data.repo;

import com.jb.coupon_system.data.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    /**
     * A JPA query method used in order to find a specific company by its email and password
     *
     * @param email    of company to find
     * @param password of same company
     * @return Optional<Company>
     */
    Optional<Company> findCompanyByEmailAndPassword(String email, String password);
}
