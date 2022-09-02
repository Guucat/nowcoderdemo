package com.example.demo.entity;

import lombok.Getter;

/**
 * @author shengyi
 * @create 2022/9/2 - 0:37
 */

@Getter
public class Page {
    // 当前页码
    private int current = 1;
    // 一页的显示上限
    private int limit = 10;
    // 数据总数
    private int rows;
    // 查询路径(用于复用分页链接
    private String path;

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页起始行
     */
    public int getOffset() {
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     */
    public int getTotal() {
        return rows % limit == 0 ? rows / limit : rows / limit + 1;
    }

    /**
     * 获取分页栏中起始页, jsp中使用
     * @return
     */
    public int getFrom() {
        int from = current - 2;
        return Math.max(from, 1);
    }

    /**
     * 获取分页栏中结束页、jsp中使用
     * @return
     */
    public int getTo() {
        int to = current + 2;
        return Math.min(to, getTotal());
    }
}
