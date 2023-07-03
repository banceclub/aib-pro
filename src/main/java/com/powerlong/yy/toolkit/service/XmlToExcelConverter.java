package com.powerlong.yy.toolkit.service;


import com.powerlong.yy.toolkit.entity.InspectionResult;

import java.util.List;

/**
 * XmlToExcelConverter
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2020/12/7 11:52
 * @version: 1.0
 * @description:
 */
public interface XmlToExcelConverter {

    List<InspectionResult> convert(String xmlPath);
}
