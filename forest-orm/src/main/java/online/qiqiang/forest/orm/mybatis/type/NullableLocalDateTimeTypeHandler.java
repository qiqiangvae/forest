package online.qiqiang.forest.orm.mybatis.type;


import online.qiqiang.forest.common.utils.SmartDateUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author qiqiang
 */
@MappedTypes(LocalDateTime.class)
@MappedJdbcTypes(value = JdbcType.DATE, includeNullJdbcType = true)
public class NullableLocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        Timestamp timestamp = Timestamp.from(parameter.atZone(ZoneId.systemDefault()).toInstant());
        ps.setObject(i, timestamp);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (null == rs.getObject(columnName)) {
            return null;
        }
        return SmartDateUtils.toLocalDateTime(rs.getObject(columnName).toString());
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (null == rs.getObject(columnIndex)) {
            return null;
        }
        return SmartDateUtils.toLocalDateTime(rs.getObject(columnIndex).toString());
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (null == cs.getObject(columnIndex)) {
            return null;
        }
        return SmartDateUtils.toLocalDateTime(cs.getObject(columnIndex).toString());
    }

}

