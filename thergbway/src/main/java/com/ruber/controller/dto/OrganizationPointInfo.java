package com.ruber.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationPointInfo {
    private String name;
    private String description;
    private String address;
    private String url;
    private String availability;
    private String phone;
    private Double lat;
    private Double lon;
}
