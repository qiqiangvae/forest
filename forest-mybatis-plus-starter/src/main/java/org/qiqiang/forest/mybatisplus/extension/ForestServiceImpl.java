package org.qiqiang.forest.mybatisplus.extension;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.session.ResultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * forest 通用服务
 *
 * @author qiqiang
 */
public class ForestServiceImpl<M extends ForestBaseMapper<T>, T> extends ServiceImpl<M, T> implements IForestService<T> {
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
            if (maxSize > 0) {
                if (resultContext.getResultCount() == maxSize) {
                    resultContext.stop();
                }
            }
        });
        return result;
    }
}
