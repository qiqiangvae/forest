package online.qiqiang.forest.orm.mybatis.interceptor;

/**
 * @author qiqiang
 */
public interface SlowSqlCallback {
    /**
     * 慢sql回调，在这里可以进行告警或者记录
     *
     * @param time 执行时间
     * @param sql  sql
     */
    void callback(long time, String id, String sql);
}
