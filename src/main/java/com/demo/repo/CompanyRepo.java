package com.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.company.Company;

import java.util.List;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {

	List<Company> findByCompanyType(String companyType);
	
}
