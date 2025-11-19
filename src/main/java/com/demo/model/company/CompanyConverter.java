package com.demo.model.company;

import java.util.ArrayList;
import java.util.List;

public class CompanyConverter {

    public static Company toCompany(CompanyDTO companyDTO){
        Company company = new Company();
        company.setCompanyId(companyDTO.getCompanyId());
        company.setCompany(companyDTO.getCompany());
        company.setCompanyType(companyDTO.getCompanyType());
        return company;
    }

    public static CompanyDTO toCompanyDTO(Company company){
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyId(company.getCompanyId());
        companyDTO.setCompany(company.getCompany());
        companyDTO.setCompanyType(company.getCompanyType());
        return companyDTO;
    }

    public static CompanyResponseDTO toCompanyResponseDTO(Company company){
        CompanyResponseDTO companyResponseDTO = new CompanyResponseDTO();
        companyResponseDTO.setCompanyId(company.getCompanyId());
        companyResponseDTO.setCompany(company.getCompany());
        companyResponseDTO.setCompanyType(company.getCompanyType());

        List<String> products = new ArrayList<>();
        company.getProducts().forEach(product -> products.add(product.getProductName()));
        companyResponseDTO.setProducts(products);

        return companyResponseDTO;
    }
}
