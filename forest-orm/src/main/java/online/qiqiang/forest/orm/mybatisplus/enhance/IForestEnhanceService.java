package online.qiqiang.forest.orm.mybatisplus.enhance;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;

import java.util.Collection;
import java.util.List;
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
     * @return 是否更新成功
     */
    boolean updateBatchByWrapper(Collection<T> entityList, Function<T, Wrapper<T>> function, int batchSize);

    /**
     * 流式获取数据
     *
     * @param wrapper       条件构造器
     * @param resultHandler 结果处理器
     */
    void fetchByStream(@Param(Constants.WRAPPER) Wrapper<T> wrapper, ResultHandler<T> resultHandler);


    /**
     * 流式获取数据，返回结果集合
     *
     * @param wrapper 条件构造器
     * @return 结果集合
     */
    List<T> fetchByStream(@Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 流式获取数据,返回结果集合
     *
     * @param wrapper 条件构造器
     * @param maxSize 条目数最大值(小于等于0时,不验证)
     * @return 结果集合
     */
    List<T> fetchByStream(@Param(Constants.WRAPPER) Wrapper<T> wrapper, int maxSize);

    /**
     * 批量插入
     * 如果jdbc url 设置了 rewriteBatchedStatements=true  ，那么次方法和 {@link com.baomidou.mybatisplus.extension.service.IService#saveBatch(java.util.Collection)}效果相同
     * 如果未设置改参数，请使用次方法
     *
     * @param list      插入的列表
     * @param batchSize 一批插入的数量
     * @return success
     */
    boolean insertBatch(List<T> list, int batchSize);

    /**
     * 批量插入，默认最大批次1000
     *
     * @param list 插入的列表
     * @return success
     */
    boolean insertBatch(List<T> list);
}
