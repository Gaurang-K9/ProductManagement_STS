package com.demo.model.company;

import java.util.ArrayList;
import java.util.List;

import com.demo.model.product.Product;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
	
	@Id
	private Long companyId;
	private String company;
	@OneToMany(mappedBy = "company",cascade = {CascadeType.ALL}, orphanRemoval = true)
	@JsonManagedReference
	private List<Product> products;
	private String companyType;
	
	public void addProduct(Product product) {
		if(products == null) {
			products = new ArrayList<>();
		}
		
		products.add(product);
		product.setCompany(this);
	}
}
