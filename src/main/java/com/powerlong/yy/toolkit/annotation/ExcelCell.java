package com.powerlong.yy.toolkit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ExcelCell
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2020/5/30 12:04
 * @version: 1.0
 * @description:
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelCell {

    /**
     * 单元格所在列的索引
     *
     * @return
     */
    int index();

    /**
     * 单元格所在列的标题
     *
     * @return
     */
    String caption();

    /**
     * 数据格式化使用的格式，数字类型的字段使用DecimalFormat格式化，日期类型的字段使用SimpleDateFormat格式化
     * example:
     * 0.###、00.000
     * yyyy-MM-dd HH:mm:ss、yyyyMMddHHmmssSSS
     *
     * @return
     */
    String format() default "";

    /**
     * 配置单元格所在列的宽度，即字符数
     *
     * @return
     */
    int width() default -1;

    /**
     * 单元格超链接配置，如果值为"sheet"则在本excel中创建链接指定该注解字段同名的sheet页，否则，直接链接到url的值
     *
     * @return
     */
    String url() default "";
}
