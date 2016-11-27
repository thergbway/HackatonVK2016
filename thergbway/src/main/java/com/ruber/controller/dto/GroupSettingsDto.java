package com.ruber.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupSettingsDto {
    private Integer group_id;
    private String group_name;
    private String coupon_title;
    private String coupon_content;
}