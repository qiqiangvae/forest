package org.qiqiang.forest.mybatisplus.enhance;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * forest 通用服务
 * 注意，如果是 mysql，在jdbc url 加上参数 rewriteBatchedStatements=true 可极大提升批量操作的效率
 *
 * @author qiqiang
 */
@Slf4j
public class ForestEnhanceServiceImpl<M extends ForestEnhanceMapper<T>, T> extends ServiceImpl<M, T> implements IForestEnhanceService<T> {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchByWrapper(Collection<T> entityList, Function<T, Wrapper<T>> function, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            log.warn("updateBatchByWrapper entityList is empty");
            return true;
        }
        String sqlStatement = getSqlStatement(SqlMethod.UPDATE_BY_ID);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> {
            MapperMethod.ParamMap<Object> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            param.put(Constants.WRAPPER, function.apply(entity));
            sqlSession.update(sqlStatement, param);
        });
    }

    @Override
    public void fetchByStream(Wrapper<T> wrapper, ResultHandler<T> resultHandler) {
        baseMapper.fetchByStream(wrapper, resultHandler);
    }

    @Override
    public List<T> fetchByStream(Wrapper<T> wrapper) {
        return fetchByStream(wrapper, -1);
    }

    @Override
    public List<T> fetchByStream(Wrapper<T> wrapper, int maxSize) {
        List<T> result = new ArrayList<>();
        baseMapper.fetchByStream(wrapper, resultContext -> {
            result.add(resultContext.getResultObject());
            if (maxSize > 0 && resultContext.getResultCount() == maxSize) {
                resultContext.stop();
                log.debug("到达最大数量[{}]，停止获取数据", maxSize);
            }
        });
        return result;
    }
}
