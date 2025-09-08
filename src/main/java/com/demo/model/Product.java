package com.demo.model;

import java.util.List;

import com.demo.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	
	@Id
	private long product_id;
	private String product;
	private String category;
	private double price;
	@ManyToOne
	@JoinColumn(name = "company_id")
	@JsonBackReference
	private Company company;
	@OneToMany(mappedBy = "product_review", cascade = CascadeType.ALL ,orphanRemoval = true)
	@JsonManagedReference
	private List<Review> reviews;
	@ManyToMany
	private List<User> users;
}
