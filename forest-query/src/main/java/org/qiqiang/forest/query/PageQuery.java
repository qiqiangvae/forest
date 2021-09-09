package org.qiqiang.forest.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiqiang
 */
public class PageQuery implements QueryParam, Serializable {
    /**
     * 当前页
     */
    private int current = 0;
    /**
     * 每页大小
     */
    private int pageSize = 10;

    private List<SortColumn> orders;

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

    public <T extends PageQuery> T addOrder(SortColumn orderItem) {
        if (orders == null) {
            orders = new ArrayList<>();
        }
        orders.add(orderItem);
        return (T) this;
    }

    public <T extends PageQuery> T addOrder(String column, SortColumn.Sort sort) {
        return addOrder(new SortColumn(column, sort));
    }

    public <T extends PageQuery> T setOrders(List<SortColumn> orders) {
        this.orders = orders;
        return (T) this;
    }

    public List<SortColumn> getOrders() {
        return orders;
    }
}
