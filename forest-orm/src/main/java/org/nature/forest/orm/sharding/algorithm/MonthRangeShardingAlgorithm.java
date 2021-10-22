package org.nature.forest.orm.sharding.algorithm;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.nature.forest.common.utils.DateConvertor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

/**
 * 按月范围查询-Date
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class MonthRangeShardingAlgorithm implements RangeShardingAlgorithm<Date> {
    private final MonthLocalDateRangeShardingAlgorithm delegate;

    public MonthRangeShardingAlgorithm() {
        this.delegate = new MonthLocalDateRangeShardingAlgorithm();
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Date> rangeShardingValue) {
        String logicTableName = rangeShardingValue.getLogicTableName();
        String columnName = rangeShardingValue.getColumnName();
        Range<Date> valueRange = rangeShardingValue.getValueRange();
        if (valueRange.hasLowerBound() && valueRange.hasUpperBound()) {
            Date from = valueRange.lowerEndpoint();
            Date to = valueRange.upperEndpoint();
            Range<LocalDate> range = Range.range(DateConvertor.dateToLocalDate(from), valueRange.lowerBoundType(),
                    DateConvertor.dateToLocalDate(to), valueRange.upperBoundType());
            RangeShardingValue<LocalDate> value = new RangeShardingValue<>(logicTableName, columnName, range);
            return delegate.doSharding(collection, value);
        } else {
            throw new RuntimeException("时间范围必须是闭区间");
        }
    }
}