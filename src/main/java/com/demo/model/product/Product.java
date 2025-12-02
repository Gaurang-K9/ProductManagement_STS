package com.demo.model.product;

import java.math.BigDecimal;
import java.util.List;

import com.demo.model.company.Company;
import com.demo.model.review.Review;
import com.demo.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
	private String productName;
	private String category;
	private BigDecimal price;
	@ManyToOne
	@JoinColumn(name = "company_id")
	@JsonBackReference
	private Company company;
	@OneToMany(mappedBy = "productReview", cascade = CascadeType.ALL ,orphanRemoval = true)
	@JsonManagedReference
	private List<Review> reviews;
}
