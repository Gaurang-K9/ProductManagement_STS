package com.demo.controller;

import com.demo.model.company.*;
import com.demo.shared.ApiResponse;
import com.demo.shared.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.demo.service.CompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/company")
@CrossOrigin
public class CompanyController {

	@Autowired
	CompanyService companyService;
	
	@GetMapping("/all")
	public ResponseEntity<ApiResponse<PageResponse<CompanyResponseDTO>>> findAllCompanies(Pageable pageable){
		var response = PageResponse
				.fromPage(companyService.findAllCompanies(pageable)
				.map(CompanyConverter::toCompanyResponseDTO));
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}

	@GetMapping("/type")
	public ResponseEntity<ApiResponse<PageResponse<CompanyResponseDTO>>> findCompanyByType(@RequestParam String category, Pageable pageable){
		var response = PageResponse
				.fromPage(companyService.findCompanyByType(category, pageable)
				.map(CompanyConverter::toCompanyResponseDTO));
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<CompanyResponseDTO>> findCompanyById(@PathVariable Long id){
		Company company = companyService.findCompanyById(id);
        CompanyResponseDTO companyResponseDTO = CompanyConverter.toCompanyResponseDTO(company);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(companyResponseDTO));
    }
	
	@PostMapping("/add")
	public ResponseEntity<ApiResponse<String>> addCompany(@RequestBody CompanyRequestDTO company){
		String response = companyService.addCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
	}
	
	@PutMapping("/update")
	public ResponseEntity<ApiResponse<String>> updateCompany(@RequestBody CompanyDTO company){
		String response = companyService.updateCompany(company);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteCompany(@PathVariable long id){
		String response = companyService.deleteCompany(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
	}
}
