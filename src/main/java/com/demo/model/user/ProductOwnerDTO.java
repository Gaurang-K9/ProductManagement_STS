package com.demo.model.user;

import com.demo.model.company.CompanyDTO;
import com.demo.model.product.ProductResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOwnerDTO {

    private String username;
    private String email;
    private List<ProductResponseDTO> products;
    private CompanyDTO company;
}