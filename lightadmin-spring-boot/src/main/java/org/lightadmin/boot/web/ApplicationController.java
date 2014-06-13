package org.lightadmin.boot.web;

import org.lightadmin.boot.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplicationController {

    @Autowired
    private HotelRepository hotelRepository;

    @RequestMapping("/")
    @ResponseBody
    public String helloWorld() {
        return "Hello hotel: " + hotelRepository.findOne(1l).getName();
    }
}