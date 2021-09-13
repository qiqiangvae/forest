package org.qiqiang.forest.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiqiang
 */
@SuppressWarnings({"unchecked", "unused"})
public class PageQuery implements QueryParam, Serializable {
    /**
     * 当前页
     */
    private int current = 0;
    /**
     * 每页大小
     */
    private int pageSize = 10;

    private List<SortColumn> sorts = new ArrayList<>();

    public int[] getPaging() {
        return new int[]{current, pageSize};
    }

    public <T extends PageQuery> T setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return (T) this;
    }

    public <T extends PageQuery> T setCurrent(int current) {
        this.current = current;
        return (T) this;
    }

    public <T extends PageQuery> T setPaging(int current, int pageSize) {
        this.current = current;
        this.pageSize = pageSize;
        return (T) this;
    }

    public <T extends PageQuery> T addSort(SortColumn orderItem) {
        if (sorts == null) {
            sorts = new ArrayList<>();
        }
        sorts.add(orderItem);
        return (T) this;
    }

    public <T extends PageQuery> T addSort(String column, SortColumn.Sort sort) {
        return addSort(column, sort, 0);
    }

    public <T extends PageQuery> T addSort(String column, SortColumn.Sort sort, int order) {
        return addSort(new SortColumn(column, sort, order));
    }

    public <T extends PageQuery> T addSorts(List<SortColumn> sorts) {
        this.sorts.addAll(sorts);
        return (T) this;
    }

    public List<SortColumn> getSorts() {
        return sorts;
    }
}
