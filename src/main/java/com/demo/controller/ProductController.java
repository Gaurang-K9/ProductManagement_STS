package com.demo.controller;

import java.math.BigDecimal;
import java.util.Map;

import com.demo.model.product.ProductConverter;
import com.demo.model.product.ProductDTO;
import com.demo.model.product.ProductResponseDTO;
import com.demo.model.user.UserPrincipal;
import com.demo.shared.ApiResponse;
import com.demo.shared.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.demo.model.product.Product;
import com.demo.service.ProductService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

	@Autowired
	ProductService productService;

	@GetMapping("/all")
	public ResponseEntity<ApiResponse<PageResponse<ProductResponseDTO>>> findAllProducts(
			@PageableDefault(sort = "productId", direction = Sort.Direction.ASC) Pageable pageable){
		var response = PageResponse
				.fromPage(productService.findAllProducts(pageable)
				.map(ProductConverter::toProductResponseDTO));
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}

	@GetMapping("/category")
	public ResponseEntity<ApiResponse<PageResponse<ProductResponseDTO>>> findByCategory(@RequestParam String type,
			@PageableDefault(sort = "productId", direction = Sort.Direction.ASC) Pageable pageable){
		var response = PageResponse
				.fromPage(productService.findByCategory(type, pageable)
						.map(ProductConverter::toProductResponseDTO));
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ProductResponseDTO>> findProductById(@PathVariable Long id){
		Product product = productService.findProductById(id);
        ProductResponseDTO responseDTO = ProductConverter.toProductResponseDTO(product);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(responseDTO));
    }

	@GetMapping("/price")
	public ResponseEntity<ApiResponse<PageResponse<ProductResponseDTO>>> findProductsInPriceRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max,
			@PageableDefault(sort = "price", direction = Sort.Direction.ASC) Pageable pageable){
		var response = PageResponse
				.fromPage(productService.findProductsInPriceRange(min, max, pageable)
				.map(ProductConverter::toProductResponseDTO));
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}

	@GetMapping("/review")
	public ResponseEntity<ApiResponse<PageResponse<ProductResponseDTO>>> findProductsByAverageStarsMoreThan(@RequestParam Short stars,
			@PageableDefault(sort = "productId", direction = Sort.Direction.ASC) Pageable pageable){
		var response = PageResponse
				.fromPage(productService.findProductsByStarMoreThan(stars, pageable)
				.map(ProductConverter::toProductResponseDTO));
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}

	@PostMapping("/add")
	public ResponseEntity<ApiResponse<String>> addProduct(@RequestBody ProductDTO productDTO, @AuthenticationPrincipal UserPrincipal user){
		String response = productService.addProduct(productDTO, user.user().getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
	}

	@PutMapping("/update/{id}/image")
	public ResponseEntity<ApiResponse<String>> addOrUpdateImageUrl(@PathVariable Long id ,@RequestBody Map<String, String> request){
		String imageUrl = request.get("imageUrl");
		String response = productService.addOrUpdateImageUrl(id, imageUrl);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<ApiResponse<String>> updateProduct(@PathVariable Long id ,@RequestBody ProductDTO product){
		String response = productService.updateProduct(id, product);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {
		String response = productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}

	@PostMapping("/{id}/add-owner")
	public ResponseEntity<ApiResponse<String>> addOwnerToProduct(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user){
		String response = productService.addProductOwnerToProduct(id, user.user().getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
	}

	@DeleteMapping("/{id}/remove-owner")
	public ResponseEntity<ApiResponse<String>> removeOwnerFromProduct(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user){
		String response = productService.removeProductOwnerFromProduct(id, user.user().getUserId());
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}

	@PatchMapping("/{productid}/change-company/{companyid}")
	public ResponseEntity<ApiResponse<String>> changeCompany(@PathVariable Long productid, @PathVariable Long companyid){
		String response = productService.changeProductCompany(productid, companyid);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}
}
