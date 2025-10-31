package com.demo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.demo.model.Product.ProductConverter;
import com.demo.model.Product.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.Product.Product;
import com.demo.repo.ProductRepo;

@Service
public class ProductService {

	@Autowired
	ProductRepo productRepo;

    @Autowired
    CompanyService companyService;

	public Optional<Product> findProductById(long id) {
		return productRepo.findById(id);
	}

	public List<Product> findAllProducts(){
		return productRepo.findAll();
	}

	public List<Product> findByCategory(String category){
		return productRepo.findByCategory(category);
	}

    public List<Product> findProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepo.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Product> findProductsByStarMoreThan(Short star){
        return productRepo.findDistinctByReviews_StarGreaterThan(star);
    }

    public List<Product> findProductsById(List<Long> productIds){
        return productRepo.findAllById(productIds);
    }

	public String addProduct(ProductDTO productDTO) {
	    	Long company_id = productDTO.getCompanyId();
            if(companyService.findCompanyById(company_id).isEmpty()){
                return "Could Not Add Product, Company Not Found";
            }
            Product product = ProductConverter.toProduct(productDTO);
            product.setCompany(companyService.findCompanyById(company_id).get());
            product.setUsers(new ArrayList<>());
            product.setReviews(new ArrayList<>());
            productRepo.save(product);
            return "Product Added Successfully";
	}
	
	public String deleteProduct(long id) {
		if(productRepo.findById(id).isEmpty()){
            return "Could Not Locate Resource";
        }
        productRepo.deleteById(id);
        return "Product Removed successfully";
	}
	
	public String updateProduct(Long id ,ProductDTO productDTO) {
		
		Product old = productRepo.findById(id).orElse(null);
	
		if(old != null) {
			
			if(productDTO.getProductName() != null) {
				old.setProductName(productDTO.getProductName());
			}
			
			if(productDTO.getCategory() != null) {
				old.setCategory(productDTO.getCategory());
			}
			
			if(productDTO.getPrice() != null && productDTO.getPrice().compareTo(BigDecimal.ZERO) != 0) {
				old.setPrice(productDTO.getPrice());
			}

			productRepo.save(old);
			return "Updated Product Successfully";
		}

		return "Could Not Locate Resource";
	}
}
