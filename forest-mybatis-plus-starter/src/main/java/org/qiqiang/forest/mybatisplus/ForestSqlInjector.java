package org.qiqiang.forest.mybatisplus;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import org.qiqiang.forest.mybatisplus.enhance.FetchByStream;

import java.util.List;

/**
 * sql 注入器
 *
 * @author qiqiang
 */
public class ForestSqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new FetchByStream());
        return methodList;
    }
}
