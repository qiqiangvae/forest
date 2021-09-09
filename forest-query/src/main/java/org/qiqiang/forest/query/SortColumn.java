package org.qiqiang.forest.query;

import lombok.Getter;

/**
 * @author qiqiang
 */
@Getter
public class SortColumn {
    private final String column;
    private Sort sort = Sort.Asc;

    public SortColumn(String column, Sort sort) {
        this.column = column;
        this.sort = sort;
    }

    public SortColumn(String column) {
        this.column = column;
    }

    public enum Sort {
        /**
         * 正序
         */
        Asc,
        /**
         * 倒序
         */
        Desc
    }

}
