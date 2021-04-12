package com.jb.coupon_system.service;

import com.jb.coupon_system.data.entity.Company;
import com.jb.coupon_system.data.entity.Customer;
import com.jb.coupon_system.service.ex.DuplicateEntryException;
import com.jb.coupon_system.service.ex.NoSuchIdException;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    Optional<Company> saveCompany(Company company) throws DuplicateEntryException;

    Optional<Company> updateCompany(Company company) throws NoSuchIdException, DuplicateEntryException;

    void deleteCompanyById(long companyId) throws NoSuchIdException;

    List<Company> getAllCompanies();

    Optional<Company> getCompanyById(long companyId);

    Optional<Customer> saveCustomer(Customer customer) throws DuplicateEntryException;

    Optional<Customer> updateCustomer(Customer customer) throws NoSuchIdException, DuplicateEntryException;

    void deleteCustomerById(long customerId) throws NoSuchIdException;

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(long customerId);
}
