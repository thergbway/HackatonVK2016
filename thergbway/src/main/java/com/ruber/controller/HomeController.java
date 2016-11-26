package com.ruber.controller;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class HomeController {
    private static AtomicInteger nextId = new AtomicInteger(1);

    @RequestMapping(value = {"/", "/home"}, method = GET)
    @ResponseBody
    public String getHomePage(Model model) {
        model.addAttribute("id", nextId.getAndIncrement());

        return "<html>Hello world</html>";
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("HOME CONTROLLER CREATED");
    }
}
