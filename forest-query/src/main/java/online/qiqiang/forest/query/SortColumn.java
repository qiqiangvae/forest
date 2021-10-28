package online.qiqiang.forest.query;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qiqiang
 */
@Getter
@SuppressWarnings("unused")
public class SortColumn implements Comparable<SortColumn> {
    @Setter
    private String column;
    private final Sort sort;
    private final int order;

    public SortColumn(String column, Sort sort, int order) {
        this.column = column;
        this.sort = sort;
        this.order = order;
    }

    public SortColumn(String column, Sort sort) {
        this(column, sort, 0);
    }

    public SortColumn(String column) {
        this(column, Sort.Asc, 0);
    }

    @Override
    public int compareTo(SortColumn o) {
        return Integer.compare(order, o.order);
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
