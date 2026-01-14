package com.demo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.demo.exception.ResourceNotFoundException;
import com.demo.exception.ForbiddenAccessException;
import com.demo.model.product.ProductConverter;
import com.demo.model.product.ProductDTO;
import com.demo.model.company.Company;
import com.demo.model.user.Role;
import com.demo.model.user.User;
import com.demo.repo.CompanyRepo;
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
    CompanyRepo companyRepo;

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

    public String addProduct(ProductDTO productDTO, Long userId) {
        Long companyId = productDTO.getCompanyId();
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException(Company.class, "companyId", companyId));
        Product product = ProductConverter.toProduct(productDTO);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", userId));
        if(user.getRole() == Role.CUSTOMER || user.getRole() == Role.DELIVERY_AGENT){
            throw ForbiddenAccessException.forAction("Add", Product.class);
        }
        else if(user.getRole() == Role.PRODUCT_OWNER){
            product.setOwner(user);
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

    public String addProductOwnerToProduct(Long productId, Long userId) {
        User productOwner = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", userId));
        if (productOwner.getRole() != Role.PRODUCT_OWNER) {
            throw ForbiddenAccessException.forAction("owning", Product.class);
        }
        Product product = findProductById(productId);
        if (product.getOwner() != null) {
            throw new IllegalStateException("Product already has an owner");
        }
        product.setOwner(productOwner);
        productRepo.save(product);
        return "Product Owner: " + productOwner.getUsername() + " added to Product: " + product.getProductName();
    }

    public String removeProductOwnerFromProduct(Long productId, Long userId) {
        User productOwner = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", userId));
        Product product = findProductById(productId);
        if (product.getOwner() == null) {
            throw new IllegalStateException("Product has no owner assigned");
        }
        if (!product.getOwner().equals(productOwner)) {
            throw ForbiddenAccessException.forAction("removing", Product.class);
        }
        product.setOwner(null);
        productRepo.save(product);
        return "Product Owner: " + productOwner.getUsername() + " removed from Product: " + product.getProductName();
    }

    public String changeProductCompany(Long productId, Long companyId) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException(Company.class, "companyId", companyId));

        Product product = findProductById(productId);
        product.setCompany(company);
        productRepo.save(product);
        return "Product: " + product.getProductName() + " assigned to Company: " + company.getCompany();
    }
}
