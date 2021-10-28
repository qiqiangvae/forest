package online.qiqiang.forest.orm.sharding.algorithm;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import online.qiqiang.forest.common.utils.DateConvertor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

/**
 * 按月精确查询-Date
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class MonthPreciseShardingAlgorithm implements PreciseShardingAlgorithm<Date> {
    private final MonthLocalDatePreciseShardingAlgorithm delegate;

    public MonthPreciseShardingAlgorithm() {
        this.delegate = new MonthLocalDatePreciseShardingAlgorithm();
    }

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> preciseShardingValue) {
        Date value = preciseShardingValue.getValue();
        String logicTableName = preciseShardingValue.getLogicTableName();
        String columnName = preciseShardingValue.getColumnName();
        LocalDate localDate = DateConvertor.dateToLocalDate(value);
        PreciseShardingValue<LocalDate> shardingValue = new PreciseShardingValue<>(logicTableName, columnName, localDate);
        return delegate.doSharding(availableTargetNames, shardingValue);
    }
}
