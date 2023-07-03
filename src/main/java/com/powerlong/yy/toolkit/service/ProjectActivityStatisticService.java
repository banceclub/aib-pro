package com.powerlong.yy.toolkit.service;

import java.util.Date;

/**
 * ProjectActivityStatisticService
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2021/6/21 14:52
 * @since:
 * @description:
 */
public interface ProjectActivityStatisticService {

    void export(String projectId, Date startDate, Date endDate);

}
