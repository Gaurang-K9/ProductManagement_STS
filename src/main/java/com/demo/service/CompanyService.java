package com.demo.service;

import java.util.ArrayList;
import java.util.List;

import com.demo.exception.ResourceNotFoundException;
import com.demo.model.company.CompanyDTO;
import com.demo.model.company.CompanyRequestDTO;
import com.demo.model.product.Product;
import com.demo.model.user.User;
import com.demo.repo.ProductRepo;
import com.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.company.Company;
import com.demo.repo.CompanyRepo;

@Service
public class CompanyService {

	@Autowired
	CompanyRepo companyRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	ProductRepo productRepo;

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

	public String addCompany(CompanyRequestDTO companyRequestDTO) {
        Company company =  new Company();
		company.setCompany(companyRequestDTO.getCompany());
		company.setCompanyType(companyRequestDTO.getCompanyType());
		company.setProducts(new ArrayList<>());
		company.setProductOwners(new ArrayList<>());
		companyRepo.save(company);
		return "Company: "+company.getCompany()+" Added Successfully";
	}
	
	public String deleteCompany(Long id) {
        Company company = companyRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Company.class, "companyId", id));

        companyRepo.delete(company);
		return "CompanyId: "+company.getCompanyId()+" | Company: "+company.getCompany()+" Removed Successfully";
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
		return "CompanyId: "+oldCompany.getCompanyId()+" | Company: "+oldCompany.getCompany()+" Updated Successfully";
    }

	public List<User> getProductOwnersFromCompany(Long id){
		Company company = findCompanyById(id);
		return company.getProductOwners();
	}

	public List<User> addProductOwnerToCompany(Long companyId, Long userId){
		Company company = findCompanyById(companyId);
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", userId));
		company.getProductOwners().add(user);
		user.setCompany(company);
		userRepo.save(user);
		return company.getProductOwners();
	}

	public List<User> removeProductOwnerFromCompany(Long companyId, Long userId){
		Company company = findCompanyById(companyId);
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", userId));
		company.getProductOwners().remove(user);
		user.setCompany(null);
		userRepo.save(user);
		return company.getProductOwners();
	}

	public List<Product> getAllProductsFromCompany(Long companyId){
		Company company = findCompanyById(companyId);
		return company.getProducts();
	}

	public List<Product> addProductToCompany(Long productId, Long companyId){
		Company company = findCompanyById(companyId);
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException(Product.class, "productId", productId));
		company.getProducts().add(product);
		companyRepo.save(company);
		product.setCompany(company);
		productRepo.save(product);
		return company.getProducts();
	}

	public List<Product> removeProductFromCompany(Long productId, Long companyId){
		Company company = findCompanyById(companyId);
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException(Product.class, "productId", productId));
		company.getProducts().remove(product);
		companyRepo.save(company);
		product.setCompany(null);
		productRepo.save(product);
		return company.getProducts();
	}
}
