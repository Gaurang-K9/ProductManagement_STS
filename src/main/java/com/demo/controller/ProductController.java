package com.demo.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.demo.model.product.ProductConverter;
import com.demo.model.product.ProductDTO;
import com.demo.model.product.ProductResponseDTO;
import com.demo.model.user.UserPrincipal;
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
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

	@Autowired
	ProductService productService;

	@GetMapping("/get-all")
	public ResponseEntity<List<ProductResponseDTO>> findAllProducts(){
        List<Product> productList = productService.findAllProducts();
		List<ProductResponseDTO> products = ProductConverter.toProductResponseList(productList);
        return ResponseEntity.status(HttpStatus.OK).body(products);
	}

	@GetMapping("/all")
	public PageResponse<ProductResponseDTO> findAllProducts(
			@PageableDefault(sort = "productId", direction = Sort.Direction.ASC) Pageable pageable){
		return PageResponse.fromPage(productService.findAllProducts(pageable).map(
				ProductConverter::toProductResponseDTO
		));
	}

	@GetMapping("/category")
	public ResponseEntity<List<ProductResponseDTO>> findByCategory(@RequestParam String category){
        List<Product> productList = productService.findByCategory(category);
        List<ProductResponseDTO> products = ProductConverter.toProductResponseList(productList);
        return ResponseEntity.status(HttpStatus.OK).body(products);
	}

	@GetMapping("/type")
	public PageResponse<ProductResponseDTO> findByCategory(@RequestParam String type,
		   @PageableDefault(sort = "productId", direction = Sort.Direction.ASC) Pageable pageable) {
		return PageResponse.fromPage(productService.findByCategory(type, pageable).map(
				ProductConverter::toProductResponseDTO
		));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable Long id){
		Product product = productService.findProductById(id);
        ProductResponseDTO responseDTO = ProductConverter.toProductResponseDTO(product);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/product-price")
    public ResponseEntity<List<ProductResponseDTO>> findProductsInPriceRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max){
        List<Product> productList = productService.findProductsInPriceRange(min, max);
        List<ProductResponseDTO> products = ProductConverter.toProductResponseList(productList);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

	@GetMapping("/price")
	public PageResponse<ProductResponseDTO> findProductsInPriceRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max,
			@PageableDefault(sort = "price", direction = Sort.Direction.ASC) Pageable pageable){
		return PageResponse.fromPage(productService.findProductsInPriceRange(min, max, pageable)
				.map(ProductConverter::toProductResponseDTO));
	}

    @GetMapping("/stars")
    public ResponseEntity<List<ProductResponseDTO>> findProductsByStarsMoreThan(@RequestParam Short s){
        List<Product> productList = productService.findProductsByStarMoreThan(s);
        List<ProductResponseDTO> products = ProductConverter.toProductResponseList(productList);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

	@GetMapping("/review")
	public PageResponse<ProductResponseDTO> findProductsByAverageStarsMoreThan(@RequestParam Short stars,
			@PageableDefault(sort = "productId", direction = Sort.Direction.ASC) Pageable pageable){
		return  PageResponse.fromPage(productService.findProductsByStarMoreThan(stars, pageable)
				.map(ProductConverter::toProductResponseDTO));
	}

	@PostMapping("/add")
	public ResponseEntity<Map<String, String>> addProduct(@RequestBody ProductDTO productDTO, @AuthenticationPrincipal UserPrincipal user){
		String response = productService.addProduct(productDTO, user.user().getUserId());
		Map<String, String> body = Map.of("response", response);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@PutMapping("/{id}/image")
	public ResponseEntity<Map<String, String>> addOrUpdateImageUrl(@PathVariable Long id ,@RequestBody Map<String, String> request){
		String imageUrl = request.get("imageUrl");
		String response = productService.addOrUpdateImageUrl(id, imageUrl);
		Map<String, String> body = Map.of("response", response);
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Map<String, String>> updateProduct(@PathVariable Long id ,@RequestBody ProductDTO product){
		String response = productService.updateProduct(id, product);
        Map<String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
		String response = productService.deleteProduct(id);
        Map<String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
	}

	@PostMapping("/{id}/add-owner")
	public ResponseEntity<Map<String, String>> addOwnerToProduct(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user){
		String response = productService.addProductOwnerToProduct(id, user.user().getUserId());
		Map<String, String> body = Map.of("response", response);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@DeleteMapping("/{id}/remove-owner")
	public ResponseEntity<Map<String, String>> removeOwnerFromProduct(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user){
		String response = productService.removeProductOwnerFromProduct(id, user.user().getUserId());
		Map<String, String> body = Map.of("response", response);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@PatchMapping("/{productid}/change-company/{companyid}")
	public ResponseEntity<Map<String, String>> changeCompany(@PathVariable Long productid, @PathVariable Long companyid){
		String response = productService.changeProductCompany(productid, companyid);
		Map<String, String> body = Map.of("response", response);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}
}
