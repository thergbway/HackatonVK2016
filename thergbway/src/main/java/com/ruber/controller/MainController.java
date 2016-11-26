package com.ruber.controller;

import java.util.Arrays;
import java.util.Random;

import com.ruber.controller.dto.ExampleDto;
import com.ruber.controller.dto.OrganizationPointInfoDto;
import com.ruber.controller.dto.SearchOrganizationResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api")
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = "/example", method = GET)
    public ExampleDto getOrder() {
        int userId = new Random().nextInt();
        return new ExampleDto(userId);
    }

    @RequestMapping(value = "/find_organization", method = GET)
    public SearchOrganizationResponseDto findOrganization(
            @RequestParam("search") String searchString,
            @RequestParam("lat") Double lat,
            @RequestParam("lon") Double lon,
            @RequestParam("spn_lat") Double spnLat,
            @RequestParam("spn_lon") Double spnLon
    ){
        log.info("MainController.findOrganization");
        String type = "biz";

        OrganizationPointInfoDto op1 = new OrganizationPointInfoDto("точка на невском", "описание 1", "Невский 12",
                "http://nevskij12.ru", "каждый день", "89112900042", 59.9880207, 30.358890);
        OrganizationPointInfoDto op2 = new OrganizationPointInfoDto("точка на садовой", "описание 2", "Садовая 44",
                "http://sadovaja12.ru", "по выходным", "89122223344",59.9380207, 30.308890);

        return new SearchOrganizationResponseDto(searchString, 1, Arrays.asList(op1, op2));
    }
}