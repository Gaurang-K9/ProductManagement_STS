package com.demo.controller;

import java.util.List;
import java.util.Optional;

import com.demo.model.Product.ProductDTO;
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
	public ResponseEntity<List<Product>> findAllProducts(){
		List<Product> list = productService.findAllProducts();
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/products/{category}")
	public ResponseEntity<List<Product>> findByCategory(@PathVariable String category){
		List<Product> list = productService.findByCategory(category);
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/product/{id}")
	public ResponseEntity<Product> findProductById(@PathVariable long id){
		Optional<Product> product = productService.findProductById(id);
		
		if(product.isPresent()) {
			return new ResponseEntity<>(product.get(), HttpStatus.OK);
		}
		else {
			Product product2 = new Product();
			return new ResponseEntity<>(product2, HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/fetch/products")
	@ResponseBody
	//http://localhost:8080/common/fetch/products?name=Oreo&category=Biscuit
	public ResponseEntity<List<Product>> findProductByNameOrCategory(@RequestParam String name, @RequestParam String category){
		List<Product> products = productService.findProductByNameOrCategory(name, category);
		
		return new ResponseEntity<>(products, HttpStatus.OK);
	}
	
	@PostMapping("/product")
	public ResponseEntity<String> addProduct(@RequestBody ProductDTO productDTO){
		String response = productService.addProduct(productDTO);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/product/{id}")
	public ResponseEntity<String> updateProduct(@RequestBody Product product, @PathVariable long id){
		String response = productService.updateProduct(product, id);
		
		if(response.startsWith("C")) {
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable long id) {
		Optional<Product> product = productService.findProductById(id);
		if(product.isPresent()) {
			productService.deleteProduct(id);
			return new ResponseEntity<>("Product Deleted Successfully", HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>("Could Not Locate Resource", HttpStatus.NOT_FOUND);
		}
	}

}
