package online.qiqiang.forest.orm.mybatisplus.enhance;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author qiqiang
 */
@SuppressWarnings("unused")
public interface IForestEnhanceService<T> extends IService<T> {
    /**
     * 根据自定义条件批量更新
     *
     * @param entityList 需要更新的数据
     * @param function   自定义条件
     * @param batchSize  分批更新大小
     * @return 更新成功的条数
     */
    long updateBatchByWrapper(Collection<T> entityList, Function<T, Wrapper<T>> function, int batchSize);

    /**
     * 流式获取数据
     *
     * @param wrapper  条件构造器
     * @param consumer 结果处理器
     */
    void fetchByStream(@Param(Constants.WRAPPER) Wrapper<T> wrapper, Consumer<T> consumer);

    /**
     * 当达到一定数量时才消费
     *
     * @param wrapper  条件构造器
     * @param consumer 消费
     * @param pageSize 每批次大小
     */
    void fetchByStream(@Param(Constants.WRAPPER) Wrapper<T> wrapper, Consumer<Collection<T>> consumer, int pageSize);

    /**
     * 游标查询方法，使用此方法时，需要在 jdbc url 设置 useCursorFetch=true，在 mybatis 配置中需要指定 fetchSize
     *
     * @param wrapper  条件
     * @param consumer 消费元素
     */
    void fetchByCursor(Wrapper<T> wrapper, Consumer<T> consumer);

    /**
     * 当达到一定数量时才消费
     *
     * @param wrapper  条件构造器
     * @param consumer 消费
     * @param pageSize 每批次大小
     */
    void fetchByCursor(Wrapper<T> wrapper, Consumer<Collection<T>> consumer, int pageSize);

    /**
     * 批量插入
     * 如果jdbc url 设置了 rewriteBatchedStatements=true  ，那么建议使用 {@link com.baomidou.mybatisplus.extension.service.IService#saveBatch(java.util.Collection)}
     * 如果未设置改参数，请使用次方法
     *
     * @param list      插入的列表
     * @param batchSize 一批插入的数量
     * @return success
     */
    boolean insertBatch(Collection<T> list, int batchSize);

    /**
     * 批量插入，默认最大批次1000
     *
     * @param list 插入的列表
     * @return success
     */
    boolean insertBatch(Collection<T> list);
}
