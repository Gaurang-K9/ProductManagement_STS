package com.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.demo.model.company.Company;
import com.demo.model.Product.Product;
import com.demo.model.specifications.CompanySpecification;
import com.demo.repo.CompanyRepo;

@Service
public class CompanyService {

	@Autowired
	CompanyRepo companyRepo;
		
	public Optional<Company> findCompanyById(long id){
		return companyRepo.findById(id);
	}
	
	public List<Company> findAllCompanies(){
		return companyRepo.findAll();
	}
	
	public List<Company> findCompanyByType(String company_type){
		return companyRepo.findCompanyByType(company_type);
	}
	
	public List<Company> findCompanyByNameOrType(String company, String company_type){
		List<Company> list = companyRepo.findAll(
				Specification.where(CompanySpecification.hasCompany(company))
				.or(CompanySpecification.hasCompanyType(company_type))
				);
		return list;
	}
	
	public String addCompany(Company company) {
		
		if(company.getProducts() != null) {
			for(Product product : company.getProducts()) {
				product.setCompany(company);
			}
		}
				
		companyRepo.save(company);
		return "Added Company Successfully";
	}
	
	public void deleteCompany(long id) {
		companyRepo.deleteById(id);
	}
	
	public String updateProduct(Company company, long id) {
		Company old = companyRepo.findById(id).orElse(null);
		
		if(old != null) {
			
			if(company.getCompany() != null) {
				old.setCompany(company.getCompany());
			}
			
			if(company.getCompany_type() != null) {
				old.setCompany_type(company.getCompany_type());
			}
			
			if(company.getProducts() != null && !company.getProducts().isEmpty()) {
				for(Product product : company.getProducts()) {
					old.addProduct(product);
				}
			}
			
			companyRepo.save(old);
			return "Updated Company Successfully";
		}
		
		return "Could Not Locate Resource";
	}
}
