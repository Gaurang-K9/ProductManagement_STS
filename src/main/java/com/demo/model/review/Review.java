package com.demo.model.review;

import com.demo.model.product.Product;
import com.demo.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;
	@ManyToOne
    @JsonBackReference
	private User user;
	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonBackReference
	private Product productReview;
	private String review;
    private Short star;
}
