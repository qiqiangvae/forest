package online.qiqiang.forest.orm.mybatisplus.enhance;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import online.qiqiang.forest.orm.mybatisplus.ForestMybatisPlusConst;
import org.apache.ibatis.mapping.*;

/**
 * @author qiqiang
 * @date 2021/8/26 11:42
 */
public class FetchByCursor extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sqlFormat = "<script>\nSELECT %s FROM %s %s %s\n</script>";
        String sql = String.format(sqlFormat, sqlSelectColumns(tableInfo, true),
                tableInfo.getTableName(), sqlWhereEntityWrapper(true, tableInfo),
                sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        String statementName = mapperClass.getName() + DOT + ForestMybatisPlusConst.METHOD_FETCH_BY_CURSOR;
        if (configuration.hasStatement(statementName, false)) {
            logger.warn(LEFT_SQ_BRACKET + statementName + "] Has been loaded by XML or SqlProvider or Mybatis's Annotation, so ignoring this injection for [" + getClass() + RIGHT_SQ_BRACKET);
            return null;
        }
        return builderAssistant.addMappedStatement(ForestMybatisPlusConst.METHOD_FETCH_BY_CURSOR, sqlSource, StatementType.PREPARED, SqlCommandType.SELECT,
                1000, null, null, null, null, modelClass,
                ResultSetType.FORWARD_ONLY, false, true, false, null, null, null,
                configuration.getDatabaseId(), languageDriver, null);
    }
}
