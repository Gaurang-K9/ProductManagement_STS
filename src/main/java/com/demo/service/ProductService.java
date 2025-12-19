package com.demo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.demo.exception.ResourceNotFoundException;
import com.demo.model.product.ProductConverter;
import com.demo.model.product.ProductDTO;
import com.demo.model.company.Company;
import com.demo.model.user.Role;
import com.demo.model.user.User;
import com.demo.repo.UserRepo;
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

    @Autowired
    UserRepo userRepo;

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

    public List<Product> findProductsByOwnerId(Long id){
        return productRepo.findByOwner_UserId(id);
    }

    public List<Product> findProductsByOwner(String username) {
        return productRepo.findByOwner_Username(username);
    }

    public String addOrUpdateImageUrl(Long productId, String imageUrl){
        Product product = findProductById(productId);
        product.setImageUrl(imageUrl);
        productRepo.save(product);
        return "Image added to Product: "+product.getProductName()+" Successfully";
    }

    public String addProduct(ProductDTO productDTO, String username) {
        long companyId = productDTO.getCompanyId();
        Company company = companyService.findCompanyById(companyId);
        Product product = ProductConverter.toProduct(productDTO);

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "username", username));
        if(user.getRole() == Role.PRODUCT_OWNER && (companyId !=  user.getCompany().getCompanyId())){
            throw new IllegalStateException("Company Mismatch");
        }

        product.setCompany(company);
        product.setReviews(new ArrayList<>());
        productRepo.save(product);
        return "Product: "+product.getProductName()+" Added Successfully";
    }

    public String deleteProduct(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, "productId", id));
        productRepo.delete(product);
        return "ProductId: "+product.getProductId()+" | Product: "+product.getProductName()+" Removed Successfully";
    }

    public void saveProduct(Product product){
        productRepo.save(product);
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
        return "ProductId: "+oldProduct.getProductId()+" | Product: "+oldProduct.getProductName()+" Updated Successfully";
    }

    public String addProductOwnerToProduct(Long userId, Long productId) {
        User productOwner = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", userId));
        Product product = findProductById(productId);
        product.setOwner(productOwner);
        productRepo.save(product);
        return "Product Owner: "+productOwner.getUsername()+"Added To Product: "+product.getProductName();
    }
}
