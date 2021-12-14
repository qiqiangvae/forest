package online.qiqiang.forest.orm.mybatis.type;


import online.qiqiang.forest.common.utils.SmartDateUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.LocalDateTypeHandler;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author qiqiang
 */
@MappedTypes(LocalDate.class)
@MappedJdbcTypes(value = JdbcType.DATE, includeNullJdbcType = true)
@SuppressWarnings("unused")
public class NullableLocalDateTypeHandler extends LocalDateTypeHandler {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType) throws SQLException {
        Timestamp timestamp = Timestamp.from(parameter.atStartOfDay(ZoneId.systemDefault()).toInstant());
        ps.setTimestamp(i, timestamp);
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (null == rs.getObject(columnName)) {
            return null;
        }
        return SmartDateUtils.toLocalDate(rs.getObject(columnName).toString());
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (null == rs.getObject(columnIndex)) {
            return null;
        }
        return SmartDateUtils.toLocalDate(rs.getObject(columnIndex).toString());
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (null == cs.getObject(columnIndex)) {
            return null;
        }
        return SmartDateUtils.toLocalDate(cs.getObject(columnIndex).toString());
    }

}

