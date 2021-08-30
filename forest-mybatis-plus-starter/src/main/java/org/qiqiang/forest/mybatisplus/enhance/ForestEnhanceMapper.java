package org.qiqiang.forest.mybatisplus.enhance;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;

/**
 * forest 通用 mapper
 *
 * @author qiqiang
 */
public interface ForestEnhanceMapper<T> extends BaseMapper<T> {
    /**
     * 通过流获取数据
     *
     * @param wrapper 条件构造
     * @param handler {@linkplain ResultHandler}
     */
    void fetchByStream(@Param(Constants.WRAPPER) Wrapper<T> wrapper, ResultHandler<T> handler);
}
