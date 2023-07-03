package com.powerlong.yy.toolkit.service.impl;

import com.powerlong.yy.toolkit.dao.ProjectActivityStatisticMapper;
import com.powerlong.yy.toolkit.entity.Item;
import com.powerlong.yy.toolkit.entity.Project;
import com.powerlong.yy.toolkit.service.ProjectActivityStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ProjectActivityStatisticImpl
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2021/6/21 14:52
 * @since:
 * @description:
 */
@Slf4j
@Service
public class ProjectActivityStatisticImpl implements ProjectActivityStatisticService {

    @Autowired
    ProjectActivityStatisticMapper activityStatisticMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public void export(String projectId, Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

//        Map<String, Project> projectMap = initProject();
//        Map<String, Map<String, Item>> itemMaps = initItemMaps(projectMap, startDate, endDate);

        while (!start.after(end)) {
            String endTime = String.format("%d-%02d-%02d 23:59:59", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1, start.get(Calendar.DAY_OF_MONTH));
            String endDay = String.format("%d-%02d-%02d", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1, start.get(Calendar.DAY_OF_MONTH));
            String month = String.format("%d%02d%02d", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1, start.get(Calendar.DAY_OF_MONTH));
            String startTime = String.format("%d-%02d-%02d 00:00:00", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1, start.get(Calendar.DAY_OF_MONTH));
            log.info("==========================开始查询：" + sdf.format(start.getTime()));
            log.info(activityStatisticMapper.selectMainCardTotal(projectId, endTime));
            log.info(activityStatisticMapper.selectMemberTotal(projectId, month));
            log.info(activityStatisticMapper.selectGoldCardTotal(projectId, endDay));
            log.info(activityStatisticMapper.selectSilverCardTotal(projectId, endDay));
            log.info(activityStatisticMapper.selectLongCardTotal(projectId, endDay));
            log.info("---------");
            log.info(activityStatisticMapper.selectComsumeAmountTotal(projectId, startTime,endTime));
            log.info(activityStatisticMapper.selectComsumeAmountGold(projectId, startTime,endTime));
            log.info(activityStatisticMapper.selectComsumeAmountSilver(projectId, startTime,endTime));
            log.info(activityStatisticMapper.selectComsumeAmountLong(projectId, startTime,endTime));
            log.info("---------");
            log.info(activityStatisticMapper.selectCosumeNum(projectId, startTime,endTime));
            log.info(activityStatisticMapper.selectCosumeNumGold(projectId, startTime,endTime));
            log.info(activityStatisticMapper.selectCosumeNumSilver(projectId, startTime,endTime));
            log.info(activityStatisticMapper.selectCosumeNumLong(projectId, startTime,endTime));
            log.info("---------");
            log.info(activityStatisticMapper.selectCosumeMemberNum(projectId, startTime,endTime));
            log.info(activityStatisticMapper.selectCosumeMemberNumGold(projectId, startTime,endTime));
            log.info(activityStatisticMapper.selectCosumeMemberNumSilver(projectId, startTime,endTime));
            log.info(activityStatisticMapper.selectCosumeMemberNumLong(projectId, startTime,endTime));
            log.info("---------");
            printList(activityStatisticMapper.selectConsumeAmountByLevel(projectId, startTime,endTime));
            log.info("---------");
            printList(activityStatisticMapper.selectValidConsumeAmountByLevel(projectId, startTime,endTime));
            log.info("---------");
            printList(activityStatisticMapper.selectConsumeAmountByCategory(projectId, startTime,endTime));
            log.info("---------");
            printList(activityStatisticMapper.selectValidConsumeAmountByCategory(projectId, startTime,endTime));
            log.info("---------");
            printList(activityStatisticMapper.selectDistinctTickerMember(projectId, startTime,endTime));
            log.info("---------");
            printList(activityStatisticMapper.selectDistinctTickerMemberValid(projectId, startTime,endTime));

            log.info("---------");
            log.info(activityStatisticMapper.selectTickerMemberNum(projectId, startTime,endTime));
            log.info(activityStatisticMapper.selectValidTickerMemberNum(projectId, startTime,endTime));
            log.info("---------");
            printList(activityStatisticMapper.selectTicketPickNum(projectId, startTime,endTime));
            log.info("---------");
            printList(activityStatisticMapper.selectTicketValidNum(projectId, startTime,endTime));

            start.add(Calendar.DAY_OF_MONTH, 1);
            log.info("");
        }

    }

    private void printList(List<Map<String, Object>> list) {
        for (Map<String, Object> map : list) {
            String s = "";
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                 s = s + entry.getValue() + "\t";
            }
            log.info(s);
        }
        log.info("");
    }
}
