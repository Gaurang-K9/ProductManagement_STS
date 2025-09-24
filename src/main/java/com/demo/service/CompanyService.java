package com.demo.service;

import java.util.List;
import java.util.Optional;

import com.demo.model.company.CompanyConverter;
import com.demo.model.company.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.company.Company;
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
		return companyRepo.findByCompanyType(company_type);
	}
	
//	public List<Company> findCompanyByNameOrType(String company, String company_type){
//		List<Company> list = companyRepo.findAll(
//				Specification.where(CompanySpecification.hasCompany(company))
//				.or(CompanySpecification.hasCompanyType(company_type))
//				);
//		return list;
//	}
	
	public String addCompany(CompanyDTO companyDTO) {
        Company company = CompanyConverter.toCompany(companyDTO);
		companyRepo.save(company);
		return "Added Company Successfully";
	}
	
	public String deleteCompany(long id) {
		if(companyRepo.findById(id).isEmpty()){
            return "Could Not Locate Resource";
        }
        companyRepo.deleteById(id);
        return "Company Removed Successfully";
	}
	
	public String updateProduct(CompanyDTO companyDTO) {
		Company old = companyRepo.findById(companyDTO.getCompanyId()).orElse(null);
		
		if(old != null) {
			
			if(companyDTO.getCompany() != null) {
				old.setCompany(companyDTO.getCompany());
			}
			
			if(companyDTO.getCompanyType() != null) {
				old.setCompanyType(companyDTO.getCompanyType());
			}

            companyRepo.save(old);
            return "Updated Company Successfully";
        }
        return "Could Not Locate Resource";
    }

}
