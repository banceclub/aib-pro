package com.powerlong.yy.toolkit.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ProjectActivityStatisticMapper
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2021/6/21 14:25
 * @since:
 * @description:
 */
@Mapper
public interface ProjectActivityStatisticMapper {
    String selectMainCardTotal(@Param("projectId") String projectId, @Param("endTime") String endTime);

    String selectMemberTotal(@Param("projectId") String projectId, @Param("month") String month);

    String selectGoldCardTotal(@Param("projectId") String projectId, @Param("endDate") String endDate);

    String selectSilverCardTotal(@Param("projectId") String projectId, @Param("endDate") String endDate);

    String selectLongCardTotal(@Param("projectId") String projectId, @Param("endDate") String endDate);

    String selectComsumeAmountTotal(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    String selectComsumeAmountGold(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    String selectComsumeAmountSilver(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    String selectComsumeAmountLong(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    String selectCosumeNum(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    String selectCosumeNumGold(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    String selectCosumeNumSilver(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    String selectCosumeNumLong(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    String selectCosumeMemberNum(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    String selectCosumeMemberNumGold(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    String selectCosumeMemberNumSilver(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    String selectCosumeMemberNumLong(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> selectConsumeAmountByLevel(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> selectValidConsumeAmountByLevel(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> selectConsumeAmountByCategory(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> selectValidConsumeAmountByCategory(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> selectDistinctTickerMember(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> selectDistinctTickerMemberValid(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    String selectTickerMemberNum(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    String selectValidTickerMemberNum(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> selectTicketPickNum(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> selectTicketValidNum(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

}
