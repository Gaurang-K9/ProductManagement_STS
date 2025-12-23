package com.demo.model.company;

import java.util.ArrayList;
import java.util.List;

import com.demo.model.product.Product;
import com.demo.model.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long companyId;
	private String company;
	@OneToMany(mappedBy = "company",cascade = {CascadeType.ALL}, orphanRemoval = true)
	@JsonManagedReference
	private List<Product> products = new ArrayList<>();
	private String companyType;
    @OneToMany(mappedBy = "company")
    private List<User> productOwners = new ArrayList<>();

    public void addProduct(Product product) {
		if(products == null) {
			products = new ArrayList<>();
		}
		
		products.add(product);
		product.setCompany(this);
	}
}
