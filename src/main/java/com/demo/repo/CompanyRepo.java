package com.demo.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.company.Company;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {

	Page<Company> findByCompanyType(String companyType, Pageable pageable);
}
