package online.qiqiang.forest.orm.mybatisplus.enhance;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import online.qiqiang.forest.common.utils.BatchUtils;
import online.qiqiang.forest.orm.exception.ForestOrmException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * forest 通用服务
 * 注意，如果是 mysql，在jdbc url 加上参数 rewriteBatchedStatements=true 可极大提升批量操作的效率
 *
 * @author qiqiang
 */
@Slf4j
@SuppressWarnings("unused")
public class ForestEnhanceServiceImpl<M extends ForestEnhanceMapper<T>, T> extends ServiceImpl<M, T> implements IForestEnhanceService<T> {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long updateBatchByWrapper(Collection<T> entityList, Function<T, Wrapper<T>> function, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            log.warn("updateBatchByWrapper entityList is empty");
            return 0;
        }
        AtomicLong count = new AtomicLong(0);
        String sqlStatement = getSqlStatement(SqlMethod.UPDATE_BY_ID);
        executeBatch(entityList, batchSize, (sqlSession, entity) -> {
            MapperMethod.ParamMap<Object> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            param.put(Constants.WRAPPER, function.apply(entity));
            count.addAndGet(sqlSession.update(sqlStatement, param));
        });
        return count.get();
    }

    @Override
    public void fetchByStream(Wrapper<T> wrapper, Consumer<T> consumer) {
        baseMapper.fetchByStream(wrapper, resultContext -> consumer.accept(resultContext.getResultObject()));
    }

    @Override
    public void fetchByStream(Wrapper<T> wrapper, Consumer<Collection<T>> consumer, int pageSize) {
        BatchUtils.execute(pageSize,
                (Consumer<BatchUtils.Generator<T>>) generator ->
                        baseMapper.fetchByStream(wrapper, resultContext -> generator.add(resultContext.getResultObject())),
                optional -> optional.isNotEmpty(consumer));
    }

    /**
     * 加上事务的原因是保持游标一直在连接状态，否则连接会断开
     *
     * @param wrapper  条件
     * @param consumer 消费元素
     */
    @Override
    @Transactional
    public void fetchByCursor(Wrapper<T> wrapper, Consumer<T> consumer) {
        try (Cursor<T> cursor = baseMapper.fetchByCursor(wrapper)) {
            for (T t : cursor) {
                consumer.accept(t);
            }
        } catch (IOException e) {
            throw new ForestOrmException(e);
        }
    }

    @Override
    public void fetchByCursor(Wrapper<T> wrapper, Consumer<Collection<T>> consumer, int pageSize) {
        BatchUtils.execute(pageSize,
                (Consumer<BatchUtils.Generator<T>>) generator -> this.fetchByCursor(wrapper, generator::add),
                optional -> optional.isNotEmpty(consumer));
    }

    @Override
    public boolean insertBatch(Collection<T> list, int batchSize) {
        BatchUtils.execute(batchSize, (Consumer<BatchUtils.Generator<T>>) generator -> list.forEach(generator::add),
                optional -> optional.isNotEmpty(baseMapper::insertBatch));
        return true;
    }

    @Override
    public boolean insertBatch(Collection<T> list) {
        return insertBatch(list, 1000);
    }
}
