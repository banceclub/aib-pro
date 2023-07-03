package com.powerlong.yy.toolkit.dao;

import com.powerlong.yy.toolkit.entity.CommonEntity;
import com.powerlong.yy.toolkit.entity.PointCategoryEntity;
import com.powerlong.yy.toolkit.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * BlMonthlyMemberStatisticMapper
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2021/6/4 11:57
 * @since: v1.0
 * @description:
 */
@Mapper
public interface BlMonthlyMemberStatisticMapper {

    List<Project> getProjects();

    List<Project> getProjectsByProjectId(@Param("projectId") String projectId);

    List<CommonEntity> selectMainCardTotal(@Param("endTime") String endTime);

    List<CommonEntity> selectMemberTotal(@Param("month") String month);

    List<CommonEntity> selectGoldCardTotal(@Param("endDate") String endDate);

    List<CommonEntity> selectSilverCardTotal(@Param("endDate") String endDate);

    List<CommonEntity> selectLongCardTotal(@Param("endDate") String endDate);

    List<PointCategoryEntity> selectPointsByCategory(@Param("endDate") String endDate);

    List<CommonEntity> selectNewRegister(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectConsumeMembers(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectPointsChange(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectConsumeNum(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectComsumeAmount(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectWxpayMerchantNum(@Param("endDate") String endDate);

    List<CommonEntity> selectAlipayMerchantNum(@Param("endDate") String endDate);

    List<CommonEntity> selectMerchantTotal(@Param("endDate") String endDate);

    List<CommonEntity> selectPointsTotal(@Param("endDate") String endDate);

    List<CommonEntity> selectNewPointsTotal(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectNewPointsRegister(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectNewPointsCosume(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectNewPointsActivity(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectNewPointsPhoto(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectNewPointsWxPay(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectNewPointsAliPay(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectNewPointsBulu(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectPointsCostTotal(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectPointsCostPark(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectPointsCostParkTicket(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectPointsCostGiftCoupon(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectPointsCostVoucher(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectPointsCostLimitBuy(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> selectPointsCostShare(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> getMemberConsumeAmount(@Param("memberLevel") Integer memberLevel, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<CommonEntity> getConsumeMemberNum(@Param("memberLevel") Integer memberLevel, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> selectOrderPic(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    List<Map<String, Object>> selectOrderPicByMerchantId(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("merchantId") String merchantId, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    int countOrderPic(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    int countOrderPicByMerchantId(@Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("merchantId") String merchantId);

    List<CommonEntity> selectFreeParkTimes(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
