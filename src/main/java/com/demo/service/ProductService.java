package com.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.Product;
import com.demo.repo.ProductRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class ProductService {

	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	EntityManager entityManager;
	
	public List<Product> findProductByNameOrCategory(String product, String category) {
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Product> query = cb.createQuery(Product.class);
	    Root<Product> root = query.from(Product.class);

	    List<Predicate> predicates = new ArrayList<>();

	    if (product != null && !product.isEmpty()) {
	        predicates.add(cb.equal(root.get("product"), product));
	    }

	    if (category != null && !category.isEmpty()) {
	        predicates.add(cb.equal(root.get("category"), category));
	    }

	    if (!predicates.isEmpty()) {
	        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
	        query.select(root).where(cb.or(predicateArray));
	    } else {
	        query.select(root);
	    }

	    return entityManager.createQuery(query).getResultList();
	}

	public Optional<Product> findProductById(long id) {
		return productRepo.findById(id);
	}
	
	public List<Product> findAllProducts(){
		return productRepo.findAll();
	}
	
	public List<Product> findByCategory(String category){
		return productRepo.findByCategory(category);
	}
	
	public String addProduct(Product product) {
		productRepo.save(product); 
		return "Added Product Successfully";
	}
	
	public void deleteProduct(long id) {
		productRepo.deleteById(id);
	}
	
	public String updateProduct(Product product, long up_id) {
		
		Product old = productRepo.findById(up_id).orElse(null);
	
		if(old != null) {
			
			if(product.getProduct() != null) {
				old.setProduct(product.getProduct());
			}
			
			if(product.getCategory() != null) {
				old.setCategory(product.getCategory());
			}
			
			if(product.getPrice() != 0.0) {
				old.setPrice(product.getPrice());
			}
			
			if(product.getCompany() != null) {
				old.setCompany(product.getCompany());
			}
			
			productRepo.save(old);
			return "Updated Product Successfully";
		}
		
		return "Could Not Locate Resource";
	}
}
