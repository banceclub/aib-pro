package com.powerlong.yy.toolkit.entity;

import lombok.Data;

import java.util.Map;

/**
 * RowData
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2021/6/5 23:59
 * @since:
 * @description:
 */
@Data
public class Item {

    public Item(String regionName, String projectName, String subject) {
        this.regionName = regionName;
        ProjectName = projectName;
        this.subject = subject;
    }

    private String regionName;

    private String ProjectName;

    private String subject;

    private Map<String, Object> values;

}
