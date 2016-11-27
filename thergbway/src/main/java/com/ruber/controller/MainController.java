package com.ruber.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.ruber.controller.dto.ExampleDto;
import com.ruber.controller.dto.GroupSettingsDto;
import com.ruber.controller.dto.OrganizationPointInfoDto;
import com.ruber.controller.dto.SearchOrganizationResponseDto;
import com.ruber.dao.CommonDao;
import com.ruber.util.LengthUtils;
import com.ruber.util.ParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/api")
public class MainController {
    @Autowired
    private CommonDao dao;

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
            @RequestParam("spn_lon") Double spnLon) throws UnirestException {

        log.info("MainController.findOrganization");

        HttpResponse<JsonNode> jsonResponse = Unirest.get("https://search-maps.yandex.ru/v1/")
                .header("accept", "application/json")
                .queryString("text", searchString)
                .queryString("lang", "ru_RU")
                .queryString("results", 500)
                .queryString("apikey", "7e266fa7-68d4-4b2d-a995-d87934428a81")
                .queryString("type", "biz")
                .queryString("ll", "" + lon + "," + lat)
                .queryString("spn", "" + spnLon + "," + spnLat)
                .asObject(JsonNode.class);

        if (jsonResponse.getStatus() != HttpStatus.OK.value())
            throw new RuntimeException("Yandex responsed not OK status: " + jsonResponse.getStatusText());

        SearchOrganizationResponseDto responseDto = ParseUtils.parseSearchOrganizationResponse(jsonResponse.getBody());

        responseDto.setRequest_string(searchString);

        List<Object[]> infosMeta = new ArrayList<>();
        for (int i = 0; i < responseDto.getOrganization_points().size(); i++) {
            OrganizationPointInfoDto currInfo = responseDto.getOrganization_points().get(i);

            Object[] objects = new Object[3];
            objects[0] = i;
            objects[1] = currInfo;
            objects[2] = LengthUtils.findBetween(lat, lon, currInfo.getLat(), currInfo.getLon());

            infosMeta.add(objects);
        }

        List<Object[]> bestTwoInfos = infosMeta
                .stream()
                .sorted(Comparator.comparingDouble(o -> ((Double) o[2])))
                .limit(2)
                .collect(Collectors.toList());

        if (bestTwoInfos.size() < 2)
            return responseDto;
        else {
            responseDto.setClosest_point(((Integer) bestTwoInfos.get(0)[0]));

            Double lat1 = lat;
            Double lon1 = lon;

            Double lat2 = ((OrganizationPointInfoDto) bestTwoInfos.get(1)[1]).getLat();
            Double lon2 = ((OrganizationPointInfoDto) bestTwoInfos.get(1)[1]).getLon();

            Double leftUpLat;
            Double leftUpLon;

            Double rightDownLat;
            Double rightDownLon;

            if (lat1 < lat2) {
                leftUpLat = lat1;
                rightDownLat = lat2;
            } else {
                leftUpLat = lat2;
                rightDownLat = lat1;
            }

            if (lon1 < lon2) {
                leftUpLon = lon2;
                rightDownLon = lon1;
            } else {
                leftUpLon = lon1;
                rightDownLon = lon2;
            }

            responseDto.setLeft_up_lat(leftUpLat);
            responseDto.setLeft_up_lon(leftUpLon);

            responseDto.setRight_down_lat(rightDownLat);
            responseDto.setRight_down_lon(rightDownLon);

            log.info("POINT_INFOS = " + responseDto.getOrganization_points().size());

            return responseDto;
        }
    }

    @RequestMapping(value = "/settings", method = GET)
    public GroupSettingsDto getGroupSettings(
            @RequestParam("group_id") Integer groupId
    ) {
        log.info("MainController.getGroupSettings");

        GroupSettingsDto dto = new GroupSettingsDto();

        dto.setGroup_id(groupId);
        dto.setGroup_name(dao.getGroupName(groupId));
        dto.setCoupon_title(dao.getCouponTitle(groupId));
        dto.setCoupon_content(dao.getCouponContent(groupId));

        log.info("GROUP SETTINGS = " + dto);
        return dto;
    }

    @RequestMapping(value = "/settings", method = POST)
    public void setGroupSettings(
            @RequestParam("group_id") Integer groupId,
            @RequestParam("group_name") String groupName,
            @RequestParam("coupon_title") String couponTitle,
            @RequestParam("coupon_content") String couponContent
    ) {
        log.info("MainController.setGroupSettings");

        dao.setGroupName(groupId, groupName);
        dao.setCouponTitle(groupId, couponTitle);
        dao.setCouponContent(groupId, couponContent);
    }

    @RequestMapping(value = "/coupon", method = GET)
    public String generateCoupon() {
        log.info("MainController.generateCoupon");

        return dao.nextCoupon();
    }

    @RequestMapping(value = "/coupon_kill", method = GET)
    public Integer killCoupon(
            @RequestParam("code") Integer code
    ) {
        log.info("MainController.killCoupon");

        if (dao.killCoupon(code))
            return 1;
        else
            return 0;
    }
}