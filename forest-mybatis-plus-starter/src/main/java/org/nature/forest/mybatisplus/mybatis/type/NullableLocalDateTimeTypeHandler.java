package org.nature.forest.mybatisplus.mybatis.type;


import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.nature.forest.common.utils.DateUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author qiqiang
 */
@MappedTypes(LocalDateTime.class)
@MappedJdbcTypes(value = JdbcType.DATE, includeNullJdbcType = true)
public class NullableLocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {
    private final DateTimeFormatter formatter;

    public NullableLocalDateTimeTypeHandler() {
        this(DateUtils.Pattern.PATTERN_USUAL_DATE_TIME);
    }

    public NullableLocalDateTimeTypeHandler(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            this.formatter = DateTimeFormatter.ofPattern(DateUtils.Pattern.PATTERN_USUAL_DATE_TIME);
        } else {
            this.formatter = DateTimeFormatter.ofPattern(pattern);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (null == rs.getObject(columnName)) {
            return null;
        }
        return LocalDateTime.parse(rs.getObject(columnName).toString(), formatter);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (null == rs.getObject(columnIndex)) {
            return null;
        }
        return LocalDateTime.parse(rs.getObject(columnIndex).toString(), formatter);
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (null == cs.getObject(columnIndex)) {
            return null;
        }
        return LocalDateTime.parse(cs.getObject(columnIndex).toString(), formatter);
    }

}

