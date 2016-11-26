package com.ruber.controller;

import java.util.Arrays;
import java.util.Random;

import com.ruber.controller.dto.ExampleDto;
import com.ruber.controller.dto.OrganizationPointInfo;
import com.ruber.controller.dto.SearchOrganizationResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
        log.info("MainController.findOrganization search={}, lat={}, lon={}, spn_lat={}, spn_lon={}",
                searchString, lat, lon, spnLat, spnLon);
        String type = "biz";

        OrganizationPointInfo op1 = new OrganizationPointInfo("точка на невском", "описание 1", "Невский 12",
                "http://nevskij12.ru", "каждый день", "89112900042", 10.10, 11.11);
        OrganizationPointInfo op2 = new OrganizationPointInfo("точка на садовой", "описание 2", "Садовая 44",
                "http://sadovaja12.ru", "по выходным", "89122223344", 12.12, 13.13);

        return new SearchOrganizationResponseDto(searchString, 1, Arrays.asList(op1, op2));
    }
}