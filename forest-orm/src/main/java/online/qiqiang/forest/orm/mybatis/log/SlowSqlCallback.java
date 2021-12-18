package online.qiqiang.forest.orm.mybatis.log;

/**
 * @author qiqiang
 */
public interface SlowSqlCallback {
    /**
     * 慢sql回调，在这里可以进行告警或者记录
     *
     * @param slowSqlSource 慢 sql source
     */
    void callback(SlowSqlSource slowSqlSource);
}
