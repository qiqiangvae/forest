package online.qiqiang.forest.orm.mybatisplus.enhance.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import online.qiqiang.forest.orm.mybatisplus.ForestMybatisPlusConst;
import org.apache.ibatis.mapping.*;

/**
 * @author qiqiang
 */
public class LeftJoin extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sqlFormat = "<script>\nSELECT %s %s FROM %s LEFT JOIN %s ON %S %s %s\n</script>";
        String leftCol = sqlSelectColumns(tableInfo, true);
        String rightCol = sqlSelectColumns(tableInfo, true);
        String leftTableName = tableInfo.getTableName();
        String rightTableName = tableInfo.getTableName();
        String on = "";
        String leftWhere = sqlWhereEntityWrapper(true, tableInfo);
        String rightWhrer = sqlWhereEntityWrapper(true, tableInfo);
        String sql = String.format(sqlFormat, leftCol, rightCol, leftTableName, rightTableName, on, leftWhere, rightWhrer, sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        String statementName = mapperClass.getName() + DOT + ForestMybatisPlusConst.LEFT_JOIN;
        if (configuration.hasStatement(statementName, false)) {
            logger.warn(LEFT_SQ_BRACKET + statementName + "] Has been loaded by XML or SqlProvider or Mybatis's Annotation, so ignoring this injection for [" + getClass() + RIGHT_SQ_BRACKET);
            return null;
        }
        // fetchSize 必须设置为 Integer.MIN_VALUE 才能是流模式
        return builderAssistant.addMappedStatement(ForestMybatisPlusConst.METHOD_FETCH_BY_STREAM, sqlSource, StatementType.PREPARED, SqlCommandType.SELECT,
                Integer.MIN_VALUE, null, null, null, null, modelClass,
                ResultSetType.FORWARD_ONLY, true, true, false, null, null, null,
                configuration.getDatabaseId(), languageDriver, null);
    }
}
