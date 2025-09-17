package com.demo.model.user;

import java.util.List;

import com.demo.model.Product.Product;
import com.demo.model.review.Review;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"user\"")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_id;
	private String username;
	private String email;
	private String password;
	@ManyToMany
	@JoinTable(
		name = "user_wishlist",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "product_id")
	)
	private List<Product> wishlist;
	@OneToMany
    @JsonManagedReference
	private List<Review> reviews;
}
