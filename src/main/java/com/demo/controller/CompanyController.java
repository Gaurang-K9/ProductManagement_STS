package com.demo.controller;

import com.demo.model.company.*;
import com.demo.shared.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
	
	@GetMapping("/get-all")
	public ResponseEntity<List<CompanyDTO>> findAllCompanies(){
        List<Company> companyList = companyService.findAllCompanies();
        List<CompanyDTO> responseList = CompanyConverter.toCompanyDTOList(companyList);
		return ResponseEntity.status(HttpStatus.OK).body(responseList);
	}

	@GetMapping("/all")
	public PageResponse<CompanyResponseDTO> findAllCompanies(Pageable pageable){
		return PageResponse.fromPage(companyService.findAllCompanies(pageable).map(
				CompanyConverter::toCompanyResponseDTO
		));
	}

	@GetMapping("/by-type")
	public ResponseEntity<List<CompanyDTO>> findCompanyByType(@RequestParam String companytype){
        List<Company> companyList = companyService.findCompanyByType(companytype);
        List<CompanyDTO> responseList = CompanyConverter.toCompanyDTOList(companyList);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
	}

	@GetMapping("/type")
	public PageResponse<CompanyResponseDTO> findCompanyByType(Pageable pageable, @RequestParam String category){
		return PageResponse.fromPage(companyService.findCompanyByType(pageable, category).map(
				CompanyConverter::toCompanyResponseDTO
		));
	}

	@GetMapping("/{id}")
	public ResponseEntity<CompanyResponseDTO> findCompanyById(@PathVariable Long id){
		Company company = companyService.findCompanyById(id);
        CompanyResponseDTO companyResponseDTO = CompanyConverter.toCompanyResponseDTO(company);
        return ResponseEntity.status(HttpStatus.OK).body(companyResponseDTO);
    }
	
	@PostMapping("/add")
	public ResponseEntity<Map<String, String>> addCompany(@RequestBody CompanyRequestDTO company){
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
