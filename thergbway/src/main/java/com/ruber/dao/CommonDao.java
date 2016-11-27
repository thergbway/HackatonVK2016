package com.ruber.dao;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class CommonDao {
    private Map<Integer, String> groupIdsToGroupName = new HashMap<>();
    private Map<Integer, String> groupIdsToCouponTitle = new HashMap<>();
    private Map<Integer, String> groupIdsToCouponContent = new HashMap<>();

    {
        groupIdsToGroupName.put(2, "2 палочки");
        groupIdsToCouponTitle.put(2, "Возьми кофе бесплатно");
        groupIdsToCouponContent.put(2, "Предъявителю флаера полагается фантастический кофе");
    }

    public String getGroupName(Integer id) {
        return groupIdsToGroupName.get(id);
    }

    public void setGroupName(Integer id, String groupName) {
        groupIdsToGroupName.put(id, groupName);
    }

    public String getCouponTitle(Integer id) {
        return groupIdsToCouponTitle.get(id);
    }

    public void setCouponTitle(Integer id, String couponTitle) {
        groupIdsToCouponTitle.put(id, couponTitle);
    }

    public String getCouponContent(Integer id) {
        return groupIdsToCouponContent.get(id);
    }

    public void setCouponContent(Integer id, String couponContent) {
        groupIdsToCouponContent.put(id, couponContent);
    }

    public String nextCoupon() {
        Random r = new Random();
        while (true) {
            Integer code = r.nextInt(9000) + 1000;
            if (coupons.add(code))
                return Integer.toString(code);
        }
    }

    public boolean killCoupon(Integer code) {
        return coupons.remove(code);
    }

    private Set<Integer> coupons = new HashSet<>();
}