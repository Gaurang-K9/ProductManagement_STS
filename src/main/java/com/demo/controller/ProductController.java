package com.demo.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.demo.model.product.ProductConverter;
import com.demo.model.product.ProductDTO;
import com.demo.model.product.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.model.product.Product;
import com.demo.service.ProductService;

@RestController
@RequestMapping("/common")
@CrossOrigin
public class ProductController {
	
	@Autowired
	ProductService productService;

	@GetMapping("/products")
	public ResponseEntity<List<ProductResponseDTO>> findAllProducts(){
        List<Product> productList = productService.findAllProducts();
		List<ProductResponseDTO> products = ProductConverter.toProductResponseList(productList);
        return ResponseEntity.status(HttpStatus.OK).body(products);
	}
	
	@GetMapping("/products/{category}")
	public ResponseEntity<List<ProductResponseDTO>> findByCategory(@PathVariable String category){
        List<Product> productList = productService.findByCategory(category);
        List<ProductResponseDTO> products = ProductConverter.toProductResponseList(productList);
        return ResponseEntity.status(HttpStatus.OK).body(products);
	}
	
	@GetMapping("/products/{id}")
	public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable Long id){
		Product product = productService.findProductById(id);
        ProductResponseDTO responseDTO = ProductConverter.toProductResponseDTO(product);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/products/price")
    public ResponseEntity<List<ProductResponseDTO>> findProductsInPriceRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max){
        List<Product> productList = productService.findProductsInPriceRange(min, max);
        List<ProductResponseDTO> products = ProductConverter.toProductResponseList(productList);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/products/stars")
    public ResponseEntity<List<ProductResponseDTO>> findProductsByStarsMoreThan(@RequestParam Short s){
        List<Product> productList = productService.findProductsByStarMoreThan(s);
        List<ProductResponseDTO> products = ProductConverter.toProductResponseList(productList);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

	@PostMapping("/product")
	public ResponseEntity<Map<String, String>> addProduct(@RequestBody ProductDTO productDTO){
		String response = productService.addProduct(productDTO);
		Map<String, String> body = Map.of("response", response);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}
	
	@PutMapping("/products/{id}")
	public ResponseEntity<Map<String, String>> updateProduct(@PathVariable Long id ,@RequestBody ProductDTO product){
		String response = productService.updateProduct(id, product);
        Map<String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
	}
	
	@DeleteMapping("/products/{id}")
	public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable long id) {
		String response = productService.deleteProduct(id);
        Map<String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
	}

}
