package com.demo.model.company;

import java.util.ArrayList;
import java.util.List;

public class CompanyConverter {

    public static Company toCompany(CompanyDTO companyDTO){
        Company company = new Company();
        company.setCompany_id(companyDTO.getCompany_id());
        company.setCompany(companyDTO.getCompany());
        company.setCompany_type(companyDTO.getCompany_type());
        return company;
    }

    public static CompanyDTO toCompanyDTO(Company company){
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompany_id(company.getCompany_id());
        companyDTO.setCompany(company.getCompany());
        companyDTO.setCompany_type(company.getCompany_type());
        return companyDTO;
    }

    public static CompanyResponseDTO toCompanyResponseDTO(Company company){
        CompanyResponseDTO companyResponseDTO = new CompanyResponseDTO();
        companyResponseDTO.setCompany_id(company.getCompany_id());
        companyResponseDTO.setCompany(company.getCompany());
        companyResponseDTO.setCompany_type(company.getCompany_type());

        List<String> products = new ArrayList<>();
        company.getProducts().forEach(product -> products.add(product.getProduct()));
        companyResponseDTO.setProducts(products);

        return companyResponseDTO;
    }
}
