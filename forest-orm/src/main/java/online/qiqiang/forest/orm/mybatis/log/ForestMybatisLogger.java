package online.qiqiang.forest.orm.mybatis.log;

import lombok.RequiredArgsConstructor;
import online.qiqiang.forest.common.utils.DateConvertor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author qiqiang
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "queryCursor", args = {MappedStatement.class, Object.class, RowBounds.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
@RequiredArgsConstructor
public class ForestMybatisLogger implements Interceptor {
    private final MybatisLoggerFunction mybatisLoggerFunction;

    private SlowSqlCallback slowSqlCallback;

    public void setSlowSqlCallback(SlowSqlCallback slowSqlCallback) {
        this.slowSqlCallback = slowSqlCallback;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        boolean isUpdate = args.length == 2;
        MappedStatement ms = (MappedStatement) args[0];
        // 判断是否开启
        if (!mybatisLoggerFunction.enable()) {
            return invocation.proceed();
        }
        // 条件一：sql command type 条件是否生效
        boolean byType = mybatisLoggerFunction.supportCommand(ms.getSqlCommandType());
        String sqlId = ms.getId();
        // 条件二：sql id 条件是否生效
        boolean bySqlId = mybatisLoggerFunction.supportSqlId(sqlId);
        // 条件一跟条件二都不生效
        if (!byType && !bySqlId) {
            return invocation.proceed();
        }
        Object parameter = null;
        //获取参数，if语句成立，表示sql语句有参数，参数格式是 map 形式
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        BoundSql boundSql = null;
        if (!isUpdate && ms.getSqlCommandType() == SqlCommandType.SELECT) {
            // queryCursor 和 query
            if (args.length == 3 || args.length == 4) {
                boundSql = ms.getBoundSql(parameter);
            } else {
                // 几乎不可能走进这里面,除非使用Executor的代理对象调用query[args[6]]
                boundSql = (BoundSql) args[5];
            }
        } else if (isUpdate) {
            boundSql = ms.getBoundSql(parameter);
        }
        long start = System.currentTimeMillis();
        Object result = invocation.proceed();
        try {
            if (boundSql != null) {
                int size = 1;
                String fullSql = getFullSql(ms.getConfiguration(), boundSql);
                if (result instanceof Collection) {
                    size = ((Collection<?>) result).size();
                }
                long time = System.currentTimeMillis() - start;
                // 慢 sql 回调
                if (slowSqlCallback != null && time > mybatisLoggerFunction.slowSqlTime()) {
                    slowSqlCallback.callback(new SlowSqlSource(sqlId, fullSql, time));
                }
                // 判断长度
                if (fullSql.length() > mybatisLoggerFunction.maxlength()) {
                    fullSql = StringUtils.substring(fullSql, 0, mybatisLoggerFunction.maxlength()) + "……";
                }
                Logger logger = LoggerFactory.getLogger(ms.getId());
                logger.info("耗时【{}】,结果数量【{}】,sql【{}】", time, size, fullSql);
            }
        } catch (Exception ignored) {
            // 不要影响主流程
        }
        return result;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    public static String getFullSql(Configuration configuration, BoundSql boundSql) {
        // 获取参数
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // sql语句中多个空格都用一个空格代替
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterObject != null && parameterMappings != null && parameterMappings.size() > 0) {
            // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换　　　　　
            // 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,
                // 主要支持对JavaBean、Collection、Map三种类型对象的操作
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        // 该分支是动态sql
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        //打印出缺失，提醒该参数缺失并防止错位
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }
        return sql;
    }

    /**
     * 如果参数是String，则添加单引号， 如果是日期，则转换为时间格式器并加单引号；
     * 对参数是null和不是null的情况作了处理
     */
    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj + "'";
        } else if (obj instanceof Date) {
            value = "'" + DateConvertor.format((Date) obj, DateConvertor.Pattern.USUAL_DATE_TIME) + "'";
        } else if (obj instanceof LocalDate) {
            value = "'" + DateConvertor.format((LocalDate) obj, DateConvertor.Pattern.USUAL_DATE) + "'";
        } else if (obj instanceof LocalDateTime) {
            value = "'" + DateConvertor.format((LocalDateTime) obj, DateConvertor.Pattern.USUAL_DATE_TIME) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

}