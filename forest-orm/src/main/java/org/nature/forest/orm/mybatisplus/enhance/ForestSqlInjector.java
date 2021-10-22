package org.nature.forest.orm.mybatisplus.enhance;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

import java.util.List;

/**
 * sql 注入器
 * 需要自己在代码中注入该配置或直接将 ForestSqlInjector 交给 spring 管理
 * <code>
 *     MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
 *     GlobalConfig conf = new GlobalConfig();
 *     conf.setSqlInjector(customizedSqlInjector);
 *     sqlSessionFactory.setGlobalConfig(conf);
 * </code>
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
