package com.powerlong.yy.toolkit.entity;

import com.powerlong.yy.toolkit.annotation.ExcelCell;
import lombok.Data;

/**
 * InspectionResult
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2020/12/7 17:57
 * @version: 1.0
 * @description:
 */
@Data
public class InspectionResult {

    @ExcelCell(index = 0, caption = "问题描述", width = 30)
    private String description;

    @ExcelCell(index = 1, caption = "严重级别", width = 10)
    private String severity;

    @ExcelCell(index = 2, caption = "代码规范", width = 50)
    private String problem;

    @ExcelCell(index = 3, caption = "问题java文件", width = 30)
    private String file;

    @ExcelCell(index = 4, caption = "行数", width = 5)
    private int lineNo;

    @ExcelCell(index = 5, caption = "入口点", width = 60)
    private String entryPoint;

    @ExcelCell(index = 6, caption = "入口类型", width = 10)
    private String entryPoinType;

    @ExcelCell(index = 7, caption = "代码模块", width = 12)
    private String module;

}
