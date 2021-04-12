package com.jb.coupon_system.service;

import com.jb.coupon_system.data.entity.Company;
import com.jb.coupon_system.data.entity.Customer;
import com.jb.coupon_system.data.repo.CompanyRepository;
import com.jb.coupon_system.data.repo.CustomerRepository;
import com.jb.coupon_system.service.ex.DuplicateEntryException;
import com.jb.coupon_system.service.ex.NoSuchIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceProvider implements AdminService {

    private final CustomerRepository customerRepo;
    private final CompanyRepository companyRepo;

    @Autowired
    public AdminServiceProvider(CustomerRepository customerRepo, CompanyRepository companyRepo) {
        this.customerRepo = customerRepo;
        this.companyRepo = companyRepo;
    }

    @Override
    public Optional<Company> saveCompany(Company company) throws DuplicateEntryException {
        /*Make sure a new company is put in the data base by setting company id field to 0*/
        company.setId(0);

        /*Make sure that the company has a unique name*/
        try {
            return Optional.of(companyRepo.save(company));
        } catch (DataIntegrityViolationException e) {
            String msg = String.format("A company with the name %s has already been used", company.getName());
            throw new DuplicateEntryException(msg);
        }
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

    @Override
    public void deleteCompanyById(long companyId) throws NoSuchIdException {
        /*Make sure the company being deleted exists*/
        Optional<Company> optCompany = companyRepo.findById(companyId);
        if (optCompany.isEmpty()) {
            String msg = String.format("A company with id %d does not exist", companyId);
            throw new NoSuchIdException(msg);
        }
        companyRepo.deleteById(companyId);
    }

    @Override
    public Optional<Company> getCompanyById(long companyId) {
        return companyRepo.findById(companyId);
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    @Override
    public Optional<Customer> saveCustomer(Customer customer) throws DuplicateEntryException {
        /*In order to make sure a new customer is put in the data base customer id is set to 0*/
        customer.setId(0);

        /*Make sure that the customer has a unique email*/
        try {
            return Optional.of(customerRepo.save(customer));
        } catch (DataIntegrityViolationException e) {
            String msg = String.format("A customer with the email %s has already been used", customer.getEmail());
            throw new DuplicateEntryException(msg);
        }
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
    public void deleteCustomerById(long customerId) throws NoSuchIdException {
        /*Make sure the customer being deleted exists*/
        Optional<Customer> optCustomer = customerRepo.findById(customerId);
        if (optCustomer.isEmpty()) {
            String msg = String.format("A customer with id %d does not exist", customerId);
            throw new NoSuchIdException(msg);
        }
        customerRepo.deleteById(customerId);
    }

    @Override
    public Optional<Customer> getCustomerById(long customerId) {
        return customerRepo.findById(customerId);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }
}