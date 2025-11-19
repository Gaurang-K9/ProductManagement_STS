package com.demo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.demo.exception.ResourceNotFoundException;
import com.demo.model.product.ProductConverter;
import com.demo.model.product.ProductDTO;
import com.demo.model.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.product.Product;
import com.demo.repo.ProductRepo;

@Service
public class ProductService {

	@Autowired
	ProductRepo productRepo;

    @Autowired
    CompanyService companyService;

	public Product findProductById(Long id) {
		return productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, "productId", id));
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
	    	Long companyId = productDTO.getCompanyId();
            Company company = companyService.findCompanyById(companyId);
            Product product = ProductConverter.toProduct(productDTO);
            product.setCompany(company);
            product.setUsers(new ArrayList<>());
            product.setReviews(new ArrayList<>());
            productRepo.save(product);
            return "product Added Successfully";
	}
	
	public String deleteProduct(Long id) {
		Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, "productId", id));
        productRepo.delete(product);
        return "product Deleted Successfully";
	}
	
	public String updateProduct(Long id ,ProductDTO productDTO) {
		
		Product oldProduct = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, "productId", id));

			if(productDTO.getProductName() != null) {
				oldProduct.setProductName(productDTO.getProductName());
			}
			if(productDTO.getCategory() != null) {
				oldProduct.setCategory(productDTO.getCategory());
			}
			if(productDTO.getPrice() != null && productDTO.getPrice().compareTo(BigDecimal.ZERO) != 0) {
				oldProduct.setPrice(productDTO.getPrice());
			}

        productRepo.save(oldProduct);
		return "Updated product Successfully";
	}
}
