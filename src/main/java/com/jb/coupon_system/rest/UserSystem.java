package com.jb.coupon_system.rest;

import com.jb.coupon_system.common.LoginType;
import com.jb.coupon_system.data.entity.Admin;
import com.jb.coupon_system.data.entity.Company;
import com.jb.coupon_system.data.entity.Customer;
import com.jb.coupon_system.data.repo.CompanyRepository;
import com.jb.coupon_system.data.repo.CustomerRepository;
import com.jb.coupon_system.rest.ex.InvalidLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserSystem {
    private final CustomerRepository customerRepo;
    private final CompanyRepository companyRepo;

    @Autowired
    public UserSystem(CustomerRepository customerRepo, CompanyRepository companyRepo) {
        this.customerRepo = customerRepo;
        this.companyRepo = companyRepo;
    }

    /**
     * A method with the purpose of logging a user into the system using the relevant credentials and loginType
     *
     * @param email
     * @param password
     * @param loginType
     * @return the relevant ClientSession
     * @throws InvalidLoginException
     */
    public ClientSession createSession(String email, String password, LoginType loginType) throws InvalidLoginException {

        switch (loginType) {
            case ADMIN:
                Optional<Integer> optAdmin = Admin.getAdminByEmailAndPassword(email, password);
                if (optAdmin.isPresent()) {
                    return ClientSession.create(optAdmin.get());
                } else {
                    throwWrongCredentials(email, password);
                }

            case COMPANY:
                Optional<Company> optCompany = companyRepo.findCompanyByEmailAndPassword(email, password);
                if (optCompany.isPresent()) {
                    return ClientSession.create(optCompany.get().getId());
                } else {
                    throwWrongCredentials(email, password);
                }

            case CUSTOMER:
                Optional<Customer> optCustomer = customerRepo.findCustomerByEmailAndPassword(email, password);
                if (optCustomer.isPresent()) {
                    return ClientSession.create(optCustomer.get().getId());
                } else {
                    throwWrongCredentials(email, password);
                }

            default:
                return throwWrongCredentials(email, password);
        }
    }

    private ClientSession throwWrongCredentials(String email, String password) throws InvalidLoginException {
        String msg = String.format("Unable to login with provided credentials using email = %s and password = %s", email, password);
        throw new InvalidLoginException(msg);
    }
}