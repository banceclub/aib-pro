package com.powerlong.yy.toolkit.entity;

import lombok.Data;

import java.util.List;

/**
 * SheetContext
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2020/7/8 13:57
 * @version: 1.0
 * @description:
 */
@Data
public class SheetContext {

    /**
     * 创建目录sheet页的上下文数据
     *
     * @param datas 目录sheet页的数据集合
     * @return
     */
    public static SheetContext ofContentSheet(List<?> datas) {
        SheetContext contentContext = new SheetContext();
        contentContext.setName("目录");
        contentContext.setContent(true);
        contentContext.setExistContent(true);
        contentContext.setDatas(datas);
        return contentContext;
    }

    /**
     * 创建数据sheet页的上下文数据
     *
     * @param contentSheetName 目录所在sheet页的名称
     * @param datas            当前sheet页数据集合
     * @return
     */
    public static SheetContext ofDataSheet(String contentSheetName, List<?> datas) {
        SheetContext dataContext = new SheetContext();
        dataContext.setContent(false);
        dataContext.setExistContent(true);
        dataContext.setDatas(datas);
        dataContext.setContentSheetName(contentSheetName);
        return dataContext;
    }

    /**
     * 创建数据sheet页的上下文数据, 所在excel工作簿没有目录
     *
     * @param datas 当前sheet页数据集合
     * @return
     */
    public static SheetContext ofDataSheet(List<?> datas) {
        SheetContext dataContext = new SheetContext();
        dataContext.setContent(false);
        dataContext.setExistContent(false);
        dataContext.setDatas(datas);
        return dataContext;
    }

    /**
     * 当前sheet页名称
     */
    private String name;

    /**
     * 目录所在sheet页名称
     */
    private String contentSheetName;

    /**
     * 当前sheet页是否是目录页
     */
    private boolean content;

    /**
     * 是否有目录页
     */
    private boolean existContent;

    /**
     * 当前sheet页的数据集合
     */
    private List<?> datas;

}
