package com.ruber.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationPointInfoDto {
    private String name;
    private String uberLink;
    private String address;
    private String availability;
    private String phone;
    private Double lat;
    private Double lon;
}
