package online.qiqiang.forest.query.page;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author qiqiang
 */
@Data
@Accessors(chain = true)
public class ForestPage<T> implements Serializable {
    /**
     * 内容
     */
    private List<T> content;
    /**
     * 当前页
     */
    private Long current;
    /**
     * 当前页数据量
     */
    private Integer currentSize;
    /**
     * 每页数据量
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Long pages;
    /**
     * 总条数
     */
    private Long totalSize;

}
