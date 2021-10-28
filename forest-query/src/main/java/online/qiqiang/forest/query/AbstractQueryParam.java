package online.qiqiang.forest.query;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiqiang
 */
public abstract class AbstractQueryParam extends PageQuery {

    /**
     * 需要查询的字段
     */
    private List<String> selectList;

    /**
     * 需要查询的字段
     *
     * @param col col
     * @return this
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractQueryParam> T select(String... col) {
        if (col != null && col.length > 0) {
            this.selectList = Stream.of(col).collect(Collectors.toList());
        }
        return (T) this;
    }

    public List<String> getSelectList() {
        return selectList;
    }
}
