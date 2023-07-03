package com.powerlong.yy.toolkit.service.impl;

import com.powerlong.yy.toolkit.entity.SheetContext;
import com.powerlong.yy.toolkit.service.ExcelCreator;
import com.powerlong.yy.toolkit.service.ExcelExporter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * ExcelExportterImpl
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2020/7/8 11:34
 * @version: 1.0
 * @description:
 */
@Slf4j
@Service
public class ExcelExporterImpl implements ExcelExporter {

    @Override
    public void export(String fileName, List<?> datas, HttpServletResponse response) {
        SheetContext dataContext = SheetContext.ofDataSheet(datas);
        dataContext.setName(fileName.substring(0, fileName.lastIndexOf(".")));
        Workbook workbook = createWorkBook(fileName);
        new ExcelCreator().createSheet(workbook, dataContext);//创建普通数据sheet页
        exportAsFile(workbook, fileName, response);
    }

    private Workbook createWorkBook(String fileName) {
        if (!(fileName.endsWith(".xlsx") || fileName.endsWith(".xls"))) {
            throw new RuntimeException("不支持的文化部");
        }
        return fileName.endsWith(".xlsx") ? new XSSFWorkbook() : new HSSFWorkbook();
    }

    private void exportAsFile(Workbook workbook, String fileName, HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.reset();
            response.setContentType("application/vnd.ms-excel;chartset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            log.error(fileName + "导出失败", e);
            throw new RuntimeException(fileName + "导出失败");
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

}
