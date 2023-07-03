package com.powerlong.yy.toolkit.controller;

import com.powerlong.yy.toolkit.service.BlMonthlyMemberStatisticService;
import com.powerlong.yy.toolkit.service.ProjectActivityStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * BlMonthlyMemberStatisticController
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2021/6/4 11:38
 * @since: v1.0
 * @description: 宝龙会员月度数据统计控制器
 */
@Slf4j
@RestController
@RequestMapping("member")
public class BlMonthlyMemberStatisticController {

    @Autowired
    BlMonthlyMemberStatisticService statisticService;

    @Autowired
    ProjectActivityStatisticService activityStatisticService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    @GetMapping("statistic/{start}/{end}")
    public String test(@PathVariable("start") String start, @PathVariable("end") String end) throws ParseException {
        statisticService.export( sdf.parse(start),  sdf.parse(end));
        return "success";
    }

    @GetMapping("activity/{projectId}/{start}/{end}")
    public String getActivityStatistic(@PathVariable("projectId") String projectId, @PathVariable("start") String start, @PathVariable("end") String end) throws ParseException {
        activityStatisticService.export(projectId, sdf.parse(start),  sdf.parse(end));
        return "success";
    }
}
