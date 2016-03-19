package com.hbjycl.basic.model;

import com.hbjycl.basic.dao.IBaseDao;

/**
 * Created by hbjycl on 2016/3/19.
 * 用来传递列表对象的ThreadLocal数据
 */
public class SystemContext {
    private static ThreadLocal<Integer> pageSize = new ThreadLocal<Integer>();//分页大小
    private static ThreadLocal<Integer> pageOffSet = new ThreadLocal<Integer>();//分页的起始页
    private static ThreadLocal<String> sort= new ThreadLocal<String>();//分页的排序字段
    private static ThreadLocal<String> order = new ThreadLocal<String>();//列表的排序方式


    public static Integer getPageSize() {
        return pageSize.get();
    }

    public static void setPageSize(Integer _pageSize) {
        pageSize.set(_pageSize);
    }

    public static void removePageSize() {
        pageSize.remove();
    }

    public static Integer getPageOffSet() {
        return pageOffSet.get();
    }

    public static void setPageOffSet(Integer _pageOffSet) {
        pageOffSet.set(_pageOffSet);
    }

    public static void removeOffSet() {
        pageOffSet.remove();
    }


    public static String getSort() {
        return sort.get();
    }

    public static void setSort(String _sort) {
        sort.set(_sort);
    }

    public static void removeSort() {
        sort.remove();
    }

    public static String getOrder() {
        return order.get();
    }

    public static void setOrder(String _order) {
        order.set(_order);
    }

    public static void removeOrder() {
        order.remove();
    }
}
