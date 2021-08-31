package org.qiqiang.forest.mybatisplus.query;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.qiqiang.forest.common.utils.DateUtils;
import org.qiqiang.forest.query.Condition;
import org.qiqiang.forest.query.Express;

import java.util.Date;

@RunWith(JUnit4.class)
public class AbstractQueryParamTest {

    @Test
    public void toWrapper() {
        StudentParam studentParam = new StudentParam();
        Date start = DateUtils.parse2Date("2015-09-01");
        Date end = DateUtils.parse2Date("2017-09-01");
        Wrapper<StudentDO> studentDOWrapper = studentParam.<StudentParam>select("name", "age")
                .setName("forest")
                .setAge(5)
                .setBirthdayRange(new Date[]{start, end})
                .toWrapper();
        System.out.println(studentDOWrapper.getCustomSqlSegment());
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    static class StudentParam extends AbstractQueryParam<StudentDO> {
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