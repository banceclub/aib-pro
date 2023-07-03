package com.powerlong.yy.toolkit.service;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * BlMonthlyMemberStatisticService
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2021/6/4 11:56
 * @since: v1.0
 * @description:
 */
public interface BlMonthlyMemberStatisticService {
    void export(Date startDate, Date endDate);

    void getOrderPic(Date startDate, Date endDate,String pid,String mid);

}
