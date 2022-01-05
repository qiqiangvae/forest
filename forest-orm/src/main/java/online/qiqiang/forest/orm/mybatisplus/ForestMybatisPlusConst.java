package online.qiqiang.forest.orm.mybatisplus;

/**
 * @author qiqiang
 */
public interface ForestMybatisPlusConst {

    /**
     * 流式获取数据的方法
     */
    String METHOD_FETCH_BY_STREAM = "fetchByStream";
    /**
     * 游标查询获取方法
     */
    String METHOD_FETCH_BY_CURSOR = "fetchByCursor";
    /**
     * 批量插入
     */
    String METHOD_INSERT_BATCH = "insertBatch";
    /**
     * replace into
     */
    String METHOD_REPLACE = "replace";
}
