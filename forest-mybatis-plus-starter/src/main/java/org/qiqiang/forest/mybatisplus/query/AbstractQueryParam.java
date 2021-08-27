package org.qiqiang.forest.mybatisplus.query;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.hibernate.validator.HibernateValidator;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @author qiqiang
 */
public abstract class AbstractQueryParam<T> implements QueryParam {

    private static final Validator VALIDATOR = Validation.byProvider(HibernateValidator.class)
            .configure().failFast(true).buildValidatorFactory()
            .getValidator();

    public Wrapper<T> toWrapper() {
        return new QueryWrapper<>();
    }
}
