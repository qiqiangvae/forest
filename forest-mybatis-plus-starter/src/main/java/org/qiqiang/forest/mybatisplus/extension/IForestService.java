package org.qiqiang.forest.mybatisplus.extension;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;

import java.util.List;

/**
 * @author qiqiang
 */
public interface IForestService<T> extends IService<T> {
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
}
