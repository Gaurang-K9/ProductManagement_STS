package com.demo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.demo.exception.ConflictResourceException;
import com.demo.exception.ResourceNotFoundException;
import com.demo.exception.ForbiddenAccessException;
import com.demo.model.image.ImageUploadResponse;
import com.demo.model.product.ProductConverter;
import com.demo.model.product.ProductDTO;
import com.demo.model.company.Company;
import com.demo.model.user.Role;
import com.demo.model.user.User;
import com.demo.repo.CompanyRepo;
import com.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    CloudinaryService cloudinaryService;

	public Product findProductById(Long id) {
		return productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, "productId", id));
	}

    public Page<Product> findAllProducts(Pageable pageable){
        return productRepo.findAll(pageable);
    }

    public Page<Product> findByCategory(String category, Pageable pageable){
        return productRepo.findByCategory(category, pageable);
    }

    public Page<Product> findProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepo.findByPriceBetween(minPrice, maxPrice, pageable);
    }

    public Page<Product> findProductsByStarMoreThan(Short star, Pageable pageable) {
        return productRepo.findByAverageStarGreaterThan(star, pageable);
    }

    public List<Product> findProductsById(List<Long> productIds){
        return productRepo.findAllById(productIds);
    }

    public Page<Product> findProductsByOwnerId(Long id, Pageable pageable){
        return productRepo.findByOwner_UserId(id, pageable);
    }

    public Page<Product> findProductsByOwnerUsername(String username, Pageable pageable) {
        return productRepo.findByOwner_Username(username, pageable);
    }

    public String addOrUpdateImageUrl(Long productId, ImageUploadResponse imageUploadResponse) {
        Product product = findProductById(productId);
        product.setImageUrl(imageUploadResponse.getImageUrl());
        product.setPublicId(imageUploadResponse.getPublicId());
        productRepo.save(product);
        return "Image added to Product: "+product.getProductName()+" Successfully";
    }

    public String removeImageUrl(Long productId) {
        Product product = findProductById(productId);
        product.setImageUrl("");
        productRepo.save(product);
        return product.getPublicId();
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

        if(product.getImageUrl() != null && product.getPublicId() != null){
            cloudinaryService.deleteImage(product.getPublicId());
        }

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
            throw new ConflictResourceException("Product already has an owner");
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
            throw new ConflictResourceException("Product has no owner assigned");
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
