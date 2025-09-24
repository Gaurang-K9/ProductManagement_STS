package com.demo.model.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponseDTO {

    private Long companyId;
    private String company;
    private List<String> products;
    private String companyType;
}
