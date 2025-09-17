package com.demo.controller;

import org.springframework.web.bind.annotation.*;

import com.demo.model.company.Company;
import com.demo.service.CompanyService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/company")
@CrossOrigin
public class CompanyController {

	@Autowired
	CompanyService companyService;
	
	@GetMapping("/all")
	public ResponseEntity<List<Company>> findAllCompanies(){
		List<Company> list = companyService.findAllCompanies();
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/type/{company_type}")
	public ResponseEntity<List<Company>> findByCompanyType(@PathVariable String company_type){
		List<Company> list = companyService.findCompanyByType(company_type);
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/fetch")
	@ResponseBody
	//http://localhost:8080/company/fetch?name=Gada Electronics&type=Local
	public ResponseEntity<List<Company>> findCompanyByNameOrType(@RequestParam String name, @RequestParam String type){
		List<Company> list = companyService.findCompanyByNameOrType(name, type);
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Company> findCompanyById(@PathVariable long id){
		Optional<Company> company = companyService.findCompanyById(id);
		
		if(company.isPresent()) {
			return new ResponseEntity<>(company.get(), HttpStatus.OK);
		}
		else {
			Company company2 = new Company();
			return new ResponseEntity<>(company2, HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<String> addCompany(@RequestBody Company company){
		String response = companyService.addCompany(company);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> updateCompany(@RequestBody Company company, @PathVariable long id){
		String response = companyService.updateProduct(company, id);
		if(response.startsWith("C")) {
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCompany(@PathVariable long id){
		Optional<Company> company = companyService.findCompanyById(id);
		
		if(company.isPresent()) {
			companyService.deleteCompany(id);
			return new ResponseEntity<>("Company Deleted Successfully", HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>("Could Not Locate Resource", HttpStatus.NOT_FOUND);
		}
	}
}
