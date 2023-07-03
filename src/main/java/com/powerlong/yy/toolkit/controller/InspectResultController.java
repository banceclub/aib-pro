package com.powerlong.yy.toolkit.controller;

import com.powerlong.yy.toolkit.entity.InspectionResult;
import com.powerlong.yy.toolkit.service.ExcelExporter;
import com.powerlong.yy.toolkit.service.XmlToExcelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * InspectResultController
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2020/12/7 11:43
 * @version: 1.0
 * @description:
 */
@RestController
@RequestMapping("inspect")
public class InspectResultController {

    @Value("${inspect.xmlDir}")
    private String xmlDir;

    @Autowired
    private XmlToExcelConverter converter;

    @Autowired
    private ExcelExporter excelExporter;

    @GetMapping("excel")
    public String toExcel(HttpServletResponse response) {
        List<InspectionResult> resultList = converter.convert(xmlDir);
        String fileName = "代码规范检查_" + System.currentTimeMillis() + ".xlsx";
        excelExporter.export(fileName, resultList, response);
        return "SUCCESS";
    }
}