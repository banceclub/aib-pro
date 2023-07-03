package com.powerlong.yy.toolkit.controller;

import com.powerlong.yy.toolkit.service.BlMonthlyMemberStatisticService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * OrderPicController
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2021/7/1 16:41
 * @since:
 * @description:
 */
@RestController
@RequestMapping("order")
public class OrderPicController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    BlMonthlyMemberStatisticService statisticService;

    @GetMapping(value = {"pic/{start}/{end}","pic/{start}/{end}/{pid}"})
    public String getOrderPic(@PathVariable("start") String start, @PathVariable("end") String end,@PathVariable(value = "pid",required = false) String pid) throws ParseException {
        statisticService.getOrderPic( DateUtils.parseDate(start+"000000","yyyyMMddHHmmss"),  DateUtils.parseDate(end+"235959","yyyyMMddHHmmss"),pid,null);
        return "success";
    }

    @GetMapping("pics")
    public String getOrderPicParam(@RequestParam("start") String start,
                                    @RequestParam("end") String end,
                                    @RequestParam(value = "pid", required = false) String pid,
                                    @RequestParam(value = "mid", required = false) String mid) throws ParseException {
        statisticService.getOrderPic( DateUtils.parseDate(start+"000000","yyyyMMddHHmmss"),  DateUtils.parseDate(end+"235959","yyyyMMddHHmmss"),pid,mid);
        return "success";
    }


}

