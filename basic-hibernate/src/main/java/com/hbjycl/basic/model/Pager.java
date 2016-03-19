package com.hbjycl.basic.model;

import java.util.List;

/**
 * Created by hbjycl on 2016/3/19.
 * 分页对象
 */
public class Pager<T> {
    private int size;//分页大小
    private int offset;//分页起始页
    private long total;//总记录数
    private List<T> datas;//数据

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
