package com.ruber.controller.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchOrganizationResponseDto {
    private String request_string;
    private Integer closest_point;
    private List<OrganizationPointInfo> organization_points;
}
