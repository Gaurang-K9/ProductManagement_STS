package com.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demo.model.company.Company;

import java.util.List;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

	@Query("SELECT c FROM Company c WHERE c.company_type = ?1")
	List<Company> findCompanyByType(String company_type);	
	
}
