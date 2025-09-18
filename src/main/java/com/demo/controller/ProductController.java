package com.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.demo.model.Product.ProductConverter;
import com.demo.model.Product.ProductDTO;
import com.demo.model.Product.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.model.Product.Product;
import com.demo.service.ProductService;

@RestController
@RequestMapping("/common")
@CrossOrigin
public class ProductController {
	
	@Autowired
	ProductService productService;

	@GetMapping("/products")
	public ResponseEntity<List<ProductResponseDTO>> findAllProducts(){
		List<Product> list = productService.findAllProducts();
		List<ProductResponseDTO> products = new ArrayList<>();
        list.forEach(single -> products.add(ProductConverter.toProductResponseDTO(single)));
		return new ResponseEntity<>(products, HttpStatus.OK);
	}
	
	@GetMapping("/products/{category}")
	public ResponseEntity<List<ProductResponseDTO>> findByCategory(@PathVariable String category){
		List<Product> list = productService.findByCategory(category);
        List<ProductResponseDTO> products = new ArrayList<>();
        list.forEach(single -> products.add(ProductConverter.toProductResponseDTO(single)));
        return new ResponseEntity<>(products, HttpStatus.OK);
	}
	
	@GetMapping("/product/{id}")
	public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable long id){
		Optional<Product> product = productService.findProductById(id);
        return product.map(value -> new ResponseEntity<>(ProductConverter.toProductResponseDTO(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new ProductResponseDTO(), HttpStatus.NOT_FOUND));
    }

	@PostMapping("/product")
	public ResponseEntity<String> addProduct(@RequestBody ProductDTO productDTO){
		String response = productService.addProduct(productDTO);
		if(response.startsWith("C")){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/product/{id}")
	public ResponseEntity<String> updateProduct(@RequestBody ProductResponseDTO product){
		String response = productService.updateProduct(product);
		
		if(response.startsWith("C")) {
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable long id) {
		String response = productService.deleteProduct(id);
		if(response.startsWith("C")) {
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
