package com.demo.model;

import com.demo.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long review_id;
	@ManyToOne
	private User user;
	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonBackReference
	private Product product_review;
	private String review;
}
