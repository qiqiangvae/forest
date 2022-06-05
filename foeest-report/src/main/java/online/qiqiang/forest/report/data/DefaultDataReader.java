package online.qiqiang.forest.report.data;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import online.qiqiang.forest.orm.mybatisplus.enhance.IForestEnhanceService;
import online.qiqiang.forest.orm.mybatisplus.query.QueryParamBuilder;
import online.qiqiang.forest.query.AbstractQueryParam;

/**
 * @author qiqiang
 */
public class DefaultDataReader implements DataReader<EnhanceForestDataCondition> {
    @Override
    public void read(EnhanceForestDataCondition dataCondition) {
        IForestEnhanceService forestEnhanceService = dataCondition.getForestEnhanceService();
        AbstractQueryParam queryParam = dataCondition.getQueryParam();
        Wrapper<Object> wrapper = QueryParamBuilder.toWrapper(queryParam);
        // forestEnhanceService.fetchByCursor(wrapper,);
    }
}
