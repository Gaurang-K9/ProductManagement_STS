package com.demo.controller;

import com.demo.model.company.CompanyConverter;
import com.demo.model.company.CompanyDTO;
import com.demo.model.company.CompanyResponseDTO;
import org.springframework.web.bind.annotation.*;

import com.demo.model.company.Company;
import com.demo.service.CompanyService;

import java.util.List;
import java.util.Map;

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
        List<CompanyDTO> companyList = companyService.findAllCompanies()
                                        .stream()
                                        .map(CompanyConverter::toCompanyDTO)
                                        .toList();
		return ResponseEntity.status(HttpStatus.OK).body(companyList);
	}
	
	@GetMapping("/type/{companyType}")
	public ResponseEntity<List<CompanyDTO>> findCompanyByType(@PathVariable String companyType){
        List<CompanyDTO> companyList = companyService.findCompanyByType(companyType)
                                        .stream()
                                        .map(CompanyConverter::toCompanyDTO)
                                        .toList();
        return ResponseEntity.status(HttpStatus.OK).body(companyList);
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
	public ResponseEntity<CompanyResponseDTO> findCompanyById(@PathVariable Long id){
		Company company = companyService.findCompanyById(id);
        CompanyResponseDTO companyResponseDTO = CompanyConverter.toCompanyResponseDTO(company);
        return ResponseEntity.status(HttpStatus.OK).body(companyResponseDTO);
    }
	
	@PostMapping("/add")
	public ResponseEntity<Map<String, String>> addCompany(@RequestBody CompanyDTO company){
		String response = companyService.addCompany(company);
        Map<String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Map<String, String>> updateCompany(@RequestBody CompanyDTO company){
		String response = companyService.updateCompany(company);
        Map<String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> deleteCompany(@PathVariable long id){
		String response = companyService.deleteCompany(id);
        Map<String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
	}
}
