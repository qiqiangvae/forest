package org.qiqiang.forest.mybatisplus.query;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.qiqiang.forest.common.utils.DateUtils;
import org.qiqiang.forest.query.Condition;
import org.qiqiang.forest.query.Express;
import org.qiqiang.forest.query.SortColumn;

import java.util.Date;

@RunWith(JUnit4.class)
public class AbstractQueryParamTest {

    @Test
    public void toWrapper() {
        StudentParam studentParam = new StudentParam();
        Date start = DateUtils.parse2Date("2015-09-01");
        Date end = DateUtils.parse2Date("2017-09-01");
        studentParam.<StudentParam>select("name", "age")
                .setName("forest")
                .setAge(5)
                .setBirthdayRange(new Date[]{start, end})
                .setPaging(1, 20)
                .addOrder(new SortColumn("age", SortColumn.Sort.Desc))
        ;
        Wrapper<StudentDO> studentDOWrapper = QueryParamBuilder.toWrapper(studentParam);
        System.out.println(studentDOWrapper.getCustomSqlSegment());
        Page<StudentDO> page = QueryParamBuilder.toPage(studentParam);
        System.out.println(page);
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    static class StudentParam extends AbstractQueryParam {
        @Condition
        String name;
        @Condition(express = Express.gt)
        Integer age;
        @Condition(express = Express.between, col = "birthday")
        Date[] birthdayRange;
    }

    @Getter
    @Setter
    static class StudentDO {
        String name;
        Integer age;
        Date birthday;
    }
}