package online.qiqiang.forest.orm.mybatis.type;


import online.qiqiang.forest.common.utils.DateConvertor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.LocalDateTypeHandler;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author qiqiang
 */
@MappedTypes(LocalDate.class)
@MappedJdbcTypes(value = JdbcType.DATE, includeNullJdbcType = true)
@SuppressWarnings("unused")
public class NullableLocalDateTypeHandler extends LocalDateTypeHandler {

    private final DateTimeFormatter formatter;

    public NullableLocalDateTypeHandler() {
        this(DateConvertor.Pattern.USUAL_DATE);
    }

    public NullableLocalDateTypeHandler(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            this.formatter = DateTimeFormatter.ofPattern(DateConvertor.Pattern.USUAL_DATE);
        } else {
            this.formatter = DateTimeFormatter.ofPattern(pattern);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i, Timestamp.from(parameter.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (null == rs.getObject(columnName)) {
            return null;
        }
        return LocalDate.parse(rs.getObject(columnName).toString(), formatter);
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (null == rs.getObject(columnIndex)) {
            return null;
        }
        return LocalDate.parse(rs.getObject(columnIndex).toString(), formatter);
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (null == cs.getObject(columnIndex)) {
            return null;
        }
        return LocalDate.parse(cs.getObject(columnIndex).toString(), formatter);
    }

}

