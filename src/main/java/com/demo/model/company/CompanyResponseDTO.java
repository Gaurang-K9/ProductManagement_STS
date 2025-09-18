package com.demo.model.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponseDTO {

    private Long company_id;
    private String company;
    private List<String> products;
    private String company_type;
}
