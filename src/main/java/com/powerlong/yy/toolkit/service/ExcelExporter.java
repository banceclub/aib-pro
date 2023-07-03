package com.powerlong.yy.toolkit.service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * ExcelExporter
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2020/7/8 11:34
 * @version: 1.0
 * @description:
 */
public interface ExcelExporter {

    void export(String fileName, List<?> datas, HttpServletResponse response);

}
