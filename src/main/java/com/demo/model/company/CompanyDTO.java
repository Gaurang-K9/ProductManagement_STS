package com.demo.model.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {

    private Long company_id;
    private String company;
    private String company_type;
}
