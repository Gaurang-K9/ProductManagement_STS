package com.demo.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.demo.model.product.ProductConverter;
import com.demo.model.product.ProductDTO;
import com.demo.model.product.ProductResponseDTO;
import com.demo.model.user.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
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

	@GetMapping("/all")
	public ResponseEntity<List<ProductResponseDTO>> findAllProducts(){
        List<Product> productList = productService.findAllProducts();
		List<ProductResponseDTO> products = ProductConverter.toProductResponseList(productList);
        return ResponseEntity.status(HttpStatus.OK).body(products);
	}
	
	@GetMapping("/type")
	public ResponseEntity<List<ProductResponseDTO>> findByCategory(@RequestParam String category){
        List<Product> productList = productService.findByCategory(category);
        List<ProductResponseDTO> products = ProductConverter.toProductResponseList(productList);
        return ResponseEntity.status(HttpStatus.OK).body(products);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable Long id){
		Product product = productService.findProductById(id);
        ProductResponseDTO responseDTO = ProductConverter.toProductResponseDTO(product);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/price")
    public ResponseEntity<List<ProductResponseDTO>> findProductsInPriceRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max){
        List<Product> productList = productService.findProductsInPriceRange(min, max);
        List<ProductResponseDTO> products = ProductConverter.toProductResponseList(productList);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/stars")
    public ResponseEntity<List<ProductResponseDTO>> findProductsByStarsMoreThan(@RequestParam Short s){
        List<Product> productList = productService.findProductsByStarMoreThan(s);
        List<ProductResponseDTO> products = ProductConverter.toProductResponseList(productList);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

	@PostMapping("/add")
	public ResponseEntity<Map<String, String>> addProduct(@RequestBody ProductDTO productDTO, @AuthenticationPrincipal UserPrincipal user){
		String response = productService.addProduct(productDTO, user.getUsername());
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
	public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable long id) {
		String response = productService.deleteProduct(id);
        Map<String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
	}

}
