package online.qiqiang.forest.report.data;

import lombok.Getter;
import online.qiqiang.forest.orm.mybatisplus.enhance.IForestEnhanceService;
import online.qiqiang.forest.query.AbstractQueryParam;

/**
 * @author qiqiang
 */
@Getter
public class EnhanceForestDataCondition<T> implements DataCondition {
    private IForestEnhanceService<T> forestEnhanceService;
    private AbstractQueryParam queryParam;
}
