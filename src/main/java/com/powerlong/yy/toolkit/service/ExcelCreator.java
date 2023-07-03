package com.powerlong.yy.toolkit.service;

import com.powerlong.yy.toolkit.annotation.ExcelCell;
import com.powerlong.yy.toolkit.entity.SheetContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName: toolkits
 * @File: com.chance.toolkits.excel.export.ExcelExporter
 * @Author: ZhiLuXu
 * @Date: 2019-08-15 10:41
 * @Version: v1.0.0
 * @Description:
 */
@Slf4j
public class ExcelCreator {

    public void createSheet(Workbook workbook, SheetContext context) {
        if (CollectionUtils.isEmpty(context.getDatas())) {
            log.info("没有可用数据");
            return;
        }
        Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(context.getName()));
        int captionRow = createCaption(workbook, sheet, context);
        int rowNo = captionRow + 1;
        List<?> datas = context.getDatas();
        for (Object data : datas) {
            List<Field> excelFields = FieldUtils.getFieldsListWithAnnotation(data.getClass(), ExcelCell.class);
            Row dataRow = sheet.createRow(rowNo++);
            for (Field field : excelFields) {
                ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
                String formattedValue = formatValue(readValue(field, data), excelCell.format());
                if (context.isContent() && StringUtils.isNotBlank(excelCell.url())) {
                    createDataCell(workbook, dataRow, excelCell.index(), formattedValue, formattedValue);
                } else {
                    createDataCell(workbook, dataRow, excelCell.index(), formattedValue, null);
                }
            }
        }
    }

    private int createCaption(Workbook workbook, Sheet sheet, SheetContext context) {
        int startRow = 0;
        if (context.isExistContent() && !context.isContent()) {
            createDataCell(workbook, sheet.createRow(startRow), 0, "<< 返回目录", context.getContentSheetName());
            startRow++;
        }
        List<?> datas = context.getDatas();
        List<Field> excelFields = FieldUtils.getFieldsListWithAnnotation(datas.get(0).getClass(), ExcelCell.class);
        Row captionRow = sheet.createRow(startRow);
        for (Field excelField : excelFields) {
            ExcelCell excelCell = excelField.getAnnotation(ExcelCell.class);
            Cell cell = captionRow.createCell(excelCell.index());
            cell.setCellStyle(makeCaptionCellStyle(workbook));
            cell.setCellType(CellType.STRING);
            cell.setCellValue(excelCell.caption());
            if (excelCell.width() != -1) {
                captionRow.getSheet().setColumnWidth(excelCell.index(), excelCell.width() * 256);
            }
        }
        sheet.createFreezePane(0, startRow + 1);
        return startRow;
    }

    private void createDataCell(Workbook workbook, Row row, int index, String value, String linkedSheet) {
        Cell cell = row.createCell(index);
        if (StringUtils.isNotBlank(linkedSheet)) {
            cell.setCellStyle(makeLinkCellStyle(workbook));
            Hyperlink hyperlink = workbook.getCreationHelper().createHyperlink(HyperlinkType.DOCUMENT);
            hyperlink.setAddress(String.format("#%s!A1", linkedSheet));
            cell.setHyperlink(hyperlink);
        } else {
            cell.setCellStyle(makeDataCellStyle(workbook));
        }
//        cell.setCellType(CellType.STRING);
        cell.setCellValue(value);
    }

    private String formatValue(Object value, String format) {
        if (value == null) {
            return "";
        }
        String formatted = String.valueOf(value);

        if (value instanceof Number) {
            if (StringUtils.isNotBlank(format)) {
                formatted = new DecimalFormat(format).format(value);
            }
        } else if (value instanceof Date) {
            if (StringUtils.isNotBlank(format)) {
                formatted = new SimpleDateFormat(format).format(value);
            } else {
                formatted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
            }
        }
        return formatted;
    }

    private Object readValue(Field field, Object target) {
        try {
            field.setAccessible(true);
            return field.get(target);
        } catch (IllegalAccessException e) {
            log.error("cannot access filed: " + field.getName(), e);
        }
        throw new RuntimeException("cannot read value of field " + field.getName());
    }

    private CellStyle makeCommonCellStyle(Workbook workbook) {
        CellStyle commonCellStyle = workbook.createCellStyle();
        commonCellStyle.setBorderTop(BorderStyle.THIN);
        commonCellStyle.setBorderLeft(BorderStyle.THIN);
        commonCellStyle.setBorderBottom(BorderStyle.THIN);
        commonCellStyle.setBorderRight(BorderStyle.THIN);
        commonCellStyle.setAlignment(HorizontalAlignment.CENTER);
        commonCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        commonCellStyle.setWrapText(true);
        return commonCellStyle;
    }

    private CellStyle makeCaptionCellStyle(Workbook workbook) {
        CellStyle cellStyle = makeCommonCellStyle(workbook);
        Font f = workbook.createFont();
        f.setBold(true);
        f.setFontHeightInPoints((short) 11);
        cellStyle.setFont(f);
        return cellStyle;
    }

    private CellStyle makeLinkCellStyle(Workbook workbook) {
        CellStyle cellStyle = makeCommonCellStyle(workbook);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        Font f = workbook.createFont();
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLUE.getIndex());
        f.setUnderline(Font.U_SINGLE);
        cellStyle.setFont(f);
        return cellStyle;
    }

    private CellStyle makeDataCellStyle(Workbook workbook) {
        CellStyle cellStyle = makeCommonCellStyle(workbook);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        Font f = workbook.createFont();
        f.setFontHeightInPoints((short) 10);
        f.setBold(false);
        cellStyle.setFont(f);
        return cellStyle;
    }

}
