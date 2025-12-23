package com.demo.model.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponseDTO {

    private Long companyId;
    private String company;
    private List<String> products;
    private String companyType;
}
