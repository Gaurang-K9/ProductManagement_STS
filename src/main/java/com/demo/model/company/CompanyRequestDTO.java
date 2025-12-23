package com.demo.model.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequestDTO {

    private String company;
    private String companyType;
}
