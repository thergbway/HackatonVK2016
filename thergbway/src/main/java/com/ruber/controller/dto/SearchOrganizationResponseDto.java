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

    private Double left_up_lat;
    private Double left_up_lon;

    private Double right_down_lat;
    private Double right_down_lon;

    private List<OrganizationPointInfoDto> organization_points;
}
