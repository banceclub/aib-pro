package com.powerlong.yy.toolkit.service.impl;

import com.powerlong.yy.toolkit.dao.BlMonthlyMemberStatisticMapper;
import com.powerlong.yy.toolkit.entity.CommonEntity;
import com.powerlong.yy.toolkit.entity.Item;
import com.powerlong.yy.toolkit.entity.PointCategoryEntity;
import com.powerlong.yy.toolkit.entity.Project;
import com.powerlong.yy.toolkit.service.BlMonthlyMemberStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * BlMonthlyMemberStatisticServiceImpl
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2021/6/4 11:56
 * @since: v1.0
 * @description:
 */
@Slf4j
@Service
public class BlMonthlyMemberStatisticServiceImpl implements BlMonthlyMemberStatisticService {

    @Autowired
    BlMonthlyMemberStatisticMapper statisticMapper;

    @Value("${output.dir}")
    private String outputDir;

    @Value("${output.picdir}")
    private String outputPicDir;

    private static final String[] ITEMS = {
            "会员累积数（主卡）", "会员累计数", "金龙卡累积数", "银龙卡累积数", "龙卡累积会员数", "新增会员数", "消费会员数",
            "金龙卡消费会员数", "银龙卡消费会员数", "龙卡消费会员数",  "积分变动会员数",
            "会员消费金额", "金龙卡消费金额", "银龙卡消费金额", "龙卡消费金额",
            "会员交易笔数", "微信圈店数", "支付宝圈店数", "店铺总数", "积分累积数", "新增积分数", "新增积分数(注册赠送积分)",
            "新增积分数(消费产生积分)", "新增积分数(活动赠送积分)", "新增消费积分(拍照积分)", "新增消费积分(微信积分)", "新增消费积分(支付宝积分)",
            "新增消费积分(人工补录积分)", "消耗积分数", "停车缴费", "停车券", "礼品券", "优惠券", "限时抢购", "共享设备", "免费停车使用次数",
            "会员累计数(<=0)", "会员累计数(1-99)", "会员累计数(100-199)", "会员累计数(200-299)", "会员累计数(300-399)", "会员累计数(400-499)", "会员累计数(500-999)", "会员累计数(1000-4999)", "会员累计数(>=5000)"
    };

    private List<String> months = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

    @Override
    public void export(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        Map<String, Project> projectMap = initProject();
        Map<String, Map<String, Item>> itemMaps = initItemMaps(projectMap, startDate, endDate);

        while (!start.after(end)) {
            String yearMonth = String.format("%d-%02d", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1);
            String endTime;
            String endDay;
            String month;
            if (start.get(Calendar.YEAR) == end.get(Calendar.YEAR)
                    && start.get(Calendar.MONTH) == end.get(Calendar.MONTH)) {
                endTime = String.format("%d-%02d-%02d 23:59:59", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1, end.get(Calendar.DAY_OF_MONTH));
                endDay = String.format("%d-%02d-%02d", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1, end.get(Calendar.DAY_OF_MONTH));
                month = String.format("%d%02d%02d", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1, end.get(Calendar.DAY_OF_MONTH));
            } else {
                endTime = String.format("%d-%02d-%02d 23:59:59", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1, start.getActualMaximum(Calendar.DAY_OF_MONTH));
                endDay = String.format("%d-%02d-%02d", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1, start.getActualMaximum(Calendar.DAY_OF_MONTH));
                month = String.format("%d%02d%02d", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1, start.getActualMaximum(Calendar.DAY_OF_MONTH));
            }
            String startTime = String.format("%d-%02d-01 00:00:00", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1);

            retrieveData(yearMonth, itemMaps, "会员累积数（主卡）", statisticMapper.selectMainCardTotal(endTime));
            retrieveData(yearMonth, itemMaps, "会员累计数", statisticMapper.selectMemberTotal(month));
            retrieveData(yearMonth, itemMaps, "金龙卡累积数", statisticMapper.selectGoldCardTotal(endDay));
            retrieveData(yearMonth, itemMaps, "银龙卡累积数", statisticMapper.selectSilverCardTotal(endDay));
            retrieveData(yearMonth, itemMaps, "龙卡累积会员数", statisticMapper.selectLongCardTotal(endDay));
            retrieveData(yearMonth, itemMaps, statisticMapper.selectPointsByCategory(endDay));
            retrieveData(yearMonth, itemMaps, "新增会员数", statisticMapper.selectNewRegister(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "消费会员数", statisticMapper.selectConsumeMembers(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "积分变动会员数", statisticMapper.selectPointsChange(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "会员消费金额", statisticMapper.selectComsumeAmount(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "会员交易笔数", statisticMapper.selectConsumeNum(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "微信圈店数", statisticMapper.selectWxpayMerchantNum(endDay));
            retrieveData(yearMonth, itemMaps, "支付宝圈店数", statisticMapper.selectAlipayMerchantNum(endDay));
            retrieveData(yearMonth, itemMaps, "店铺总数", statisticMapper.selectMerchantTotal(endDay));
            retrieveData(yearMonth, itemMaps, "积分累积数", statisticMapper.selectPointsTotal(endDay));
            retrieveData(yearMonth, itemMaps, "新增积分数", statisticMapper.selectNewPointsTotal(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "新增积分数(注册赠送积分)", statisticMapper.selectNewPointsRegister(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "新增积分数(消费产生积分)", statisticMapper.selectNewPointsCosume(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "新增积分数(活动赠送积分)", statisticMapper.selectNewPointsActivity(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "新增消费积分(拍照积分)", statisticMapper.selectNewPointsPhoto(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "新增消费积分(微信积分)", statisticMapper.selectNewPointsWxPay(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "新增消费积分(支付宝积分)", statisticMapper.selectNewPointsAliPay(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "新增消费积分(人工补录积分)", statisticMapper.selectNewPointsBulu(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "消耗积分数", statisticMapper.selectPointsCostTotal(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "停车缴费", statisticMapper.selectPointsCostPark(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "停车券", statisticMapper.selectPointsCostParkTicket(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "礼品券", statisticMapper.selectPointsCostGiftCoupon(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "优惠券", statisticMapper.selectPointsCostVoucher(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "限时抢购", statisticMapper.selectPointsCostLimitBuy(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "共享设备", statisticMapper.selectPointsCostShare(startTime, endTime));
            retrieveData(yearMonth, itemMaps, "免费停车使用次数", statisticMapper.selectFreeParkTimes(startTime, endTime));

            retrieveData(yearMonth, itemMaps, "金龙卡消费金额", statisticMapper.getMemberConsumeAmount(3, startTime, endTime));
            retrieveData(yearMonth, itemMaps, "银龙卡消费金额", statisticMapper.getMemberConsumeAmount(2, startTime, endTime));
            retrieveData(yearMonth, itemMaps, "龙卡消费金额", statisticMapper.getMemberConsumeAmount(1, startTime, endTime));

            retrieveData(yearMonth, itemMaps, "金龙卡消费会员数", statisticMapper.getConsumeMemberNum(3, startTime, endTime));
            retrieveData(yearMonth, itemMaps, "银龙卡消费会员数", statisticMapper.getConsumeMemberNum(2, startTime, endTime));
            retrieveData(yearMonth, itemMaps, "龙卡消费会员数", statisticMapper.getConsumeMemberNum(1, startTime, endTime));

            start.add(Calendar.MONTH, 1);
        }
        String filePath = outputDir + File.separator;
        Workbook workbook = createWorkbook(itemMaps, projectMap);
        String filName = String.format("宝龙悠悠会员数据统计%s-%s.xlsx", sdf.format(startDate), sdf.format(endDate));
        try {
            FileOutputStream output = new FileOutputStream(filePath + filName);
            workbook.write(output);//写入磁盘
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(String.format("成功导出文件%s到%s", filName, filePath));
    }

    private Workbook createWorkbook(Map<String, Map<String, Item>> itemMaps, Map<String, Project> projectMap) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        int startRow = createCaptionRow(itemMaps, workbook, sheet);
        for (Map.Entry<String, Map<String, Item>> entry : itemMaps.entrySet()) {
            int cellIndex = 0;
            Row dataRow = sheet.createRow(startRow);
            Cell firstCell = dataRow.createCell(cellIndex++);
            firstCell.setCellStyle(makeCommonCellStyle(workbook));
            firstCell.setCellValue(projectMap.get(entry.getKey()).getRegionName());
            Cell secondCell = dataRow.createCell(cellIndex++);
            secondCell.setCellStyle(makeCommonCellStyle(workbook));
            secondCell.setCellValue(projectMap.get(entry.getKey()).getProjectName());
            Map<String, Item> itemMap = entry.getValue();
            for (Map.Entry<String, Item> itemEntry : itemMap.entrySet()) {
                Item item = itemEntry.getValue();
                for (Map.Entry<String, Object> valueEntry : item.getValues().entrySet()) {
                    Cell dataCell = dataRow.createCell(cellIndex);
                    dataCell.setCellStyle(makeCommonCellStyle(workbook));
                    dataCell.setCellType(CellType.NUMERIC);
                    dataCell.setCellValue(valueEntry.getValue() == null || StringUtils.isBlank(valueEntry.getValue().toString()) ? null : valueEntry.getValue().toString());
                    cellIndex++;
                }
            }
            startRow++;
        }
        return workbook;
    }

    private int createCaptionRow(Map<String, Map<String, Item>> itemMaps, Workbook workbook, Sheet sheet) {
        int startRow = 0;
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        Row captionRow = sheet.createRow(startRow);
        int capCellIndex = 0;
        Cell cell0 = captionRow.createCell(capCellIndex++);
        cell0.setCellStyle(makeCaptionCellStyle(workbook));
        cell0.setCellValue("区域名称");
        Cell cell1 = captionRow.createCell(capCellIndex++);
        cell1.setCellStyle(makeCaptionCellStyle(workbook));
        cell1.setCellValue("项目名称");
        Row captionRow2 = sheet.createRow(startRow + 1);
        for (Map.Entry<String, Map<String, Item>> entry : itemMaps.entrySet()) {
            Map<String, Item> itemMap = entry.getValue();
            for (Map.Entry<String, Item> itemEntry : itemMap.entrySet()) {
                Item item = itemEntry.getValue();
                if (item.getValues().size() > 1) {
                    int endCol = capCellIndex + item.getValues().size() - 1;
                    sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, capCellIndex, endCol));
                }
                Cell cell = captionRow.createCell(capCellIndex);
                cell.setCellStyle(makeCaptionCellStyle(workbook));
                cell.setCellValue(item.getSubject());

                for (Map.Entry<String, Object> valueEntry : item.getValues().entrySet()) {
                    Cell cell2 = captionRow2.createCell(capCellIndex);
                    cell2.setCellStyle(makeCaptionCellStyle(workbook));
                    cell2.setCellValue(valueEntry.getKey());
                    capCellIndex++;
                }
            }
            break;
        }
        return startRow + 2;
    }

    private Map<String, Map<String, Item>> initItemMaps(Map<String, Project> projectMap, Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        List<String> yearMonths = new ArrayList<>();
        while (!start.after(end)) {
            String yearMonth = String.format("%d-%02d", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1);
            yearMonths.add(yearMonth);
            start.add(Calendar.MONTH, 1);
        }
        Map<String, Map<String, Item>> itemsMap = new HashMap<>();
        for (Map.Entry<String, Project> entry : projectMap.entrySet()) {
            Project project = entry.getValue();

            Map<String, Item> itemMap = new LinkedHashMap<>();
            for (String itemName : ITEMS) {
                Item item = new Item(project.getRegionName(), project.getProjectName(), itemName);
                Map<String, Object> values = new LinkedHashMap<>();
                for (String yearMonth : yearMonths) {
                    values.put(yearMonth, null);
                }
                item.setValues(values);
                itemMap.put(itemName, item);
            }

            itemsMap.put(entry.getKey(), itemMap);
        }
        months = yearMonths;
        return itemsMap;
    }

    private Map<String, Project> initProject() {
        List<Project> projects = statisticMapper.getProjects();
        return projects.stream().collect(Collectors.toMap(Project::getProjectName, Function.identity()));
    }

    private void retrieveData(String yearMonth, Map<String, Map<String, Item>> itemMaps, String name, List<CommonEntity> entities) {
        log.info("{}-{}", yearMonth, name);
        for (CommonEntity entity : entities) {
            if (StringUtils.isBlank(entity.getProjectName())) {
                continue;
            }
            log.info("{}-{}-{}", yearMonth, name, entity.getProjectName());
            Map<String, Item> itemMap = itemMaps.get(entity.getProjectName());
            if (itemMap != null) {
                itemMap.get(name).getValues().put(yearMonth, entity.getValue());
            }
        }
    }

    private void retrieveData(String yearMonth, Map<String, Map<String, Item>> itemMaps, List<PointCategoryEntity> entities) {
        log.info("{}-{}", yearMonth, "各类别累计会员数");
        for (PointCategoryEntity entity : entities) {
            if (StringUtils.isBlank(entity.getProjectName())) {
                continue;
            }
            log.info("{}-{}-{}", yearMonth, "各类别累计会员数", entity.getProjectName());
            Map<String, Item> itemMap = itemMaps.get(entity.getProjectName());
            if (itemMap != null) {
                itemMap.get("会员累计数(<=0)").getValues().put(yearMonth, entity.getBelowZero());
                itemMap.get("会员累计数(1-99)").getValues().put(yearMonth, entity.getEnd99());
                itemMap.get("会员累计数(100-199)").getValues().put(yearMonth, entity.getEnd199());
                itemMap.get("会员累计数(200-299)").getValues().put(yearMonth, entity.getEnd299());
                itemMap.get("会员累计数(300-399)").getValues().put(yearMonth, entity.getEnd399());
                itemMap.get("会员累计数(400-499)").getValues().put(yearMonth, entity.getEnd499());
                itemMap.get("会员累计数(500-999)").getValues().put(yearMonth, entity.getEnd999());
                itemMap.get("会员累计数(1000-4999)").getValues().put(yearMonth, entity.getEnd4999());
                itemMap.get("会员累计数(>=5000)").getValues().put(yearMonth, entity.getUp5000());
            }
        }
    }

    private CellStyle makeCaptionCellStyle(Workbook workbook) {
        CellStyle cellStyle = makeCommonCellStyle(workbook);
        Font f = workbook.createFont();
        f.setBold(true);
        f.setFontHeightInPoints((short) 11);
        cellStyle.setFont(f);
        return cellStyle;
    }

    private CellStyle makeCommonCellStyle(Workbook workbook) {
        CellStyle commonCellStyle = workbook.createCellStyle();
        commonCellStyle.setBorderTop(BorderStyle.THIN);
        commonCellStyle.setBorderLeft(BorderStyle.THIN);
        commonCellStyle.setBorderBottom(BorderStyle.THIN);
        commonCellStyle.setBorderRight(BorderStyle.THIN);
        commonCellStyle.setAlignment(HorizontalAlignment.CENTER);
        commonCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return commonCellStyle;
    }

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public void getOrderPic(Date startDate, Date endDate,String pid,String mid) {

       /* Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);*/
        //String startTime = String.format("%d-%02d-%02d 00:00:00", start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1, start.get(Calendar.DAY_OF_MONTH));
        //String endTime = String.format("%d-%02d-%02d 23:59:59", end.get(Calendar.YEAR), end.get(Calendar.MONTH) + 1, end.getActualMaximum(Calendar.DAY_OF_MONTH));
        String startTime = DateFormatUtils.format(startDate,"yyyy-MM-dd HH:mm:ss");
        String endTime = DateFormatUtils.format(endDate,"yyyy-MM-dd HH:mm:ss");
        List<Project> projects = statisticMapper.getProjectsByProjectId(pid);
        for (Project project : projects) {
            int total = statisticMapper.countOrderPicByMerchantId(project.getProjectId(), startTime, endTime,mid);
            int pageSize = 1000;
            int pages = (total + pageSize - 1) / pageSize;
            int page = 1;

            String dirName = outputPicDir + File.separator + project.getProjectName() ;
            File dir = new File(dirName);
            if (!dir.exists()) {
                dir.mkdir();
            }

            try {
                while (page <= pages) {
                    int offset = (page - 1) * pageSize;
                    List<Map<String, Object>> dataList = statisticMapper.selectOrderPicByMerchantId(project.getProjectId(), startTime, endTime, mid,offset, pageSize);

                    for (Map<String, Object> map : dataList) {
                        Date payTime = (Date) map.get("payTime");
                        String url = map.get("url").toString();
                        String merchantName = map.get("merchantName").toString().replace(" ", "").replace("/", "_").replace("\t", "");
                        String projectName = map.get("projectName").toString();
                        String fileName = String.format("%s_%s_%s", projectName, merchantName, url.substring(url.lastIndexOf("/") + 1));
                        fileName = fileName.replace(" ", "");
                        log.info(projectName + "-" + merchantName + " " + fileName);

                        String dateDir = dirName + File.separator + sdf.format(payTime);
                        File f = new File(dateDir);
                        if (!f.exists()) {
                            f.mkdir();
                        }

                        FileOutputStream fos = null;
                        try {
                            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, byte[].class);

                            byte[] body = responseEntity.getBody();
                            //创建输出流  输出到本地
                            fos = new FileOutputStream(new File(dateDir + File.separator + fileName));
                            fos.write(body);
                            //关闭流

                        } catch (Exception e) {
                            log.error("发生异常", e);
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (Exception e1) {

                                }
                            }
                        }
                    }

                    page++;
                }

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

    }
}
