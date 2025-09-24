package com.demo.controller;

import com.demo.model.company.CompanyConverter;
import com.demo.model.company.CompanyDTO;
import com.demo.model.company.CompanyResponseDTO;
import org.springframework.web.bind.annotation.*;

import com.demo.model.company.Company;
import com.demo.service.CompanyService;

import java.util.ArrayList;
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
	public ResponseEntity<List<CompanyDTO>> findAllCompanies(){
		List<Company> list = companyService.findAllCompanies();
		List<CompanyDTO> companyList = new ArrayList<>();
        list.forEach(company -> companyList.add(CompanyConverter.toCompanyDTO(company)));
		return new ResponseEntity<>(companyList, HttpStatus.OK);
	}
	
	@GetMapping("/type/{companyType}")
	public ResponseEntity<List<CompanyDTO>> findCompanyByType(@PathVariable String companyType){
		List<Company> list = companyService.findCompanyByType(companyType);
        List<CompanyDTO> companyList = new ArrayList<>();
        list.forEach(company -> companyList.add(CompanyConverter.toCompanyDTO(company)));
        return new ResponseEntity<>(companyList, HttpStatus.OK);
	}
	
//	@GetMapping("/fetch")
//	@ResponseBody
//	//http://localhost:8080/company/fetch?name=Gada Electronics&type=Local
//	public ResponseEntity<List<Company>> findCompanyByNameOrType(@RequestParam String name, @RequestParam String type){
//		List<Company> list = companyService.findCompanyByNameOrType(name, type);
//
//		return new ResponseEntity<>(list, HttpStatus.OK);
//	}

	@GetMapping("/{id}")
	public ResponseEntity<CompanyResponseDTO> findCompanyById(@PathVariable long id){
		Optional<Company> company = companyService.findCompanyById(id);

        return company.map(value -> new ResponseEntity<>(CompanyConverter.toCompanyResponseDTO(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new CompanyResponseDTO(), HttpStatus.NOT_FOUND));
    }
	
	@PostMapping("/add")
	public ResponseEntity<String> addCompany(@RequestBody CompanyDTO company){
		String response = companyService.addCompany(company);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<String> updateCompany(@RequestBody CompanyDTO company){
		String response = companyService.updateProduct(company);
		if(response.startsWith("C")) {
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCompany(@PathVariable long id){
		String response = companyService.deleteCompany(id);
        if(response.startsWith("Cou")){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
