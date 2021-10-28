package online.qiqiang.forest.orm.mybatisplus.enhance;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.*;
import online.qiqiang.forest.orm.mybatisplus.ForestMybatisPlusConst;

/**
 * 流式获取数据
 *
 * @author qiqiang
 */
public class FetchByStream extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sqlFormat = "<script>\nSELECT %s FROM %s %s %s\n</script>";
        String sql = String.format(sqlFormat, sqlSelectColumns(tableInfo, true),
                tableInfo.getTableName(), sqlWhereEntityWrapper(true, tableInfo),
                sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        String statementName = mapperClass.getName() + DOT + ForestMybatisPlusConst.METHOD_FETCH_BY_STREAM;
        if (configuration.hasStatement(statementName, false)) {
            logger.warn(LEFT_SQ_BRACKET + statementName + "] Has been loaded by XML or SqlProvider or Mybatis's Annotation, so ignoring this injection for [" + getClass() + RIGHT_SQ_BRACKET);
            return null;
        }
        /* 缓存逻辑处理 */
        return builderAssistant.addMappedStatement(ForestMybatisPlusConst.METHOD_FETCH_BY_STREAM, sqlSource, StatementType.PREPARED, SqlCommandType.SELECT,
                1000, null, null, null, null, modelClass,
                ResultSetType.FORWARD_ONLY, true, true, false, null, null, null,
                configuration.getDatabaseId(), languageDriver, null);
    }
}
