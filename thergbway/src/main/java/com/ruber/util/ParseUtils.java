package com.ruber.util;

import java.util.ArrayList;
import java.util.List;

import com.mashape.unirest.http.JsonNode;
import com.ruber.controller.dto.OrganizationPointInfoDto;
import com.ruber.controller.dto.SearchOrganizationResponseDto;
import org.json.JSONArray;
import org.json.JSONObject;

public class ParseUtils {
    public static SearchOrganizationResponseDto parseSearchOrganizationResponse(JsonNode node) {
        List<OrganizationPointInfoDto> infos = new ArrayList<>();

        node
                .getObject()
                .getJSONArray("features")
                .forEach(feature -> {
                    JSONObject jsonObjectFeature = (JSONObject) feature;
                    String name = jsonObjectFeature.getJSONObject("properties").getString("name");

                    String address = jsonObjectFeature
                            .getJSONObject("properties")
                            .getJSONObject("CompanyMetaData")
                            .getString("address");

                    String availability = "режим не указан";
                    try {
                        availability = jsonObjectFeature
                                .getJSONObject("properties")
                                .getJSONObject("CompanyMetaData")
                                .getJSONObject("Hours")
                                .getString("text");
                    } catch (Exception ignore) {
                    }

                    Double lat = jsonObjectFeature
                            .getJSONObject("geometry")
                            .getJSONArray("coordinates")
                            .getDouble(1);

                    Double lon = jsonObjectFeature
                            .getJSONObject("geometry")
                            .getJSONArray("coordinates")
                            .getDouble(0);

//                    String uberLink = String.format("uber://?action=setPickup&dropoff[latitude]=%s&dropoff[longitude]=%s",
//                            lat, lon);

                    String uberLink = String.format("//m.uber.com/ul?action=setPickup&dropoff[latitude]=%s&dropoff[longitude]=%s",
                            lat, lon);

                    //try phone
                    String phone = "+7" + "(812)" + "379" + "-02-00";
                    try {
                        phone = ((JSONObject) feature)
                                .getJSONObject("properties")
                                .getJSONObject("CompanyMetaData")
                                .getJSONArray("Phones")
                                .getJSONObject(0)
                                .getString("formatted");
                    } catch (Exception ignore) {
                    }


                    infos.add(new OrganizationPointInfoDto(name, uberLink, address, availability, phone, lat, lon));
                });

        JSONArray leftUpPoint = node
                .getObject()
                .getJSONObject("properties")
                .getJSONObject("ResponseMetaData")
                .getJSONObject("SearchResponse")
                .getJSONArray("boundedBy")
                .getJSONArray(1);

        JSONArray rightDownPoint = node
                .getObject()
                .getJSONObject("properties")
                .getJSONObject("ResponseMetaData")
                .getJSONObject("SearchResponse")
                .getJSONArray("boundedBy")
                .getJSONArray(0);

        double leftUpLat = leftUpPoint.getDouble(1);
        double leftUpLon = leftUpPoint.getDouble(0);

        double rightDownLat = rightDownPoint.getDouble(1);
        double rightDownLon = rightDownPoint.getDouble(0);

        return new SearchOrganizationResponseDto(null, 1,
                leftUpLat, leftUpLon,
                rightDownLat, rightDownLon,
                infos
        );
    }
}
