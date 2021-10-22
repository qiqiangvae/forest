package org.nature.forest.orm.sharding.algorithm;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.time.LocalDate;
import java.util.Collection;

/**
 * 按月精确查询-LocalDate
 *
 * @author qiqiang
 */
public class MonthLocalDatePreciseShardingAlgorithm implements PreciseShardingAlgorithm<LocalDate> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<LocalDate> preciseShardingValue) {
        LocalDate date = preciseShardingValue.getValue();
        String logicTableName = preciseShardingValue.getLogicTableName();
        int year = date.getYear();
        int monthValue = date.getMonthValue();
        String tableName = logicTableName + "_" + year + "_" + monthValue;
        if (availableTargetNames.contains(tableName)) {
            return tableName;
        }
        return null;
    }
}