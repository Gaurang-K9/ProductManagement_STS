package com.demo.service;

import java.util.List;
import java.util.Optional;

import com.demo.exception.ResourceNotFoundException;
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
		
	public Company findCompanyById(Long id){
        return companyRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Company.class, "companyId", id));
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
	
	public String deleteCompany(Long id) {
        Company company = companyRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Company.class, "companyId", id));

        companyRepo.delete(company);
        return "Company Removed Successfully";
	}
	
	public String updateCompany(CompanyDTO companyDTO) {
        Long companyId = companyDTO.getCompanyId();
		Company oldCompany = companyRepo.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException(Company.class, "companyId", companyId));

			if(companyDTO.getCompany() != null) {
				oldCompany.setCompany(companyDTO.getCompany());
			}
			if(companyDTO.getCompanyType() != null) {
				oldCompany.setCompanyType(companyDTO.getCompanyType());
			}
            companyRepo.save(oldCompany);
            return "Updated Company Successfully";
    }

}
