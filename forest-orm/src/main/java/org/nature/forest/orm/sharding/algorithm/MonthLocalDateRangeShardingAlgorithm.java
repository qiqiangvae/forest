package org.nature.forest.orm.sharding.algorithm;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 按月范围查询-LocalDate
 *
 * @author qiqiang
 */
public class MonthLocalDateRangeShardingAlgorithm implements RangeShardingAlgorithm<LocalDate> {

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<LocalDate> rangeShardingValue) {
        String logicTableName = rangeShardingValue.getLogicTableName();
        Range<LocalDate> valueRange = rangeShardingValue.getValueRange();
        if (valueRange.hasLowerBound() && valueRange.hasUpperBound()) {
            LocalDate from = valueRange.lowerEndpoint();
            LocalDate to = valueRange.upperEndpoint();
            int fromYear = from.getYear();
            int fromMonth = from.getMonthValue();
            int fromMonthValue = fromYear * 12 + fromMonth;
            int toYear = to.getYear();
            int toMonth = to.getMonthValue();
            int toMonthValue = toYear * 12 + toMonth;
            String tableName;
            Set<String> result = new HashSet<>();
            while (fromMonthValue <= toMonthValue) {
                int year = fromMonthValue / 12;
                int month = fromMonthValue % 12;
                if (month == 0) {
                    year--;
                    month = 12;
                }
                tableName = logicTableName + "_" + year + "_" + month;
                if (availableTargetNames.contains(tableName)) {
                    result.add(tableName);
                }
                fromMonthValue++;
            }
            return result;
        } else {
            throw new RuntimeException("时间范围必须是闭区间");
        }

    }
}