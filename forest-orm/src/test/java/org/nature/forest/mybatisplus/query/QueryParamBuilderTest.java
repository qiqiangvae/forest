package online.qiqiang.forest.mybatisplus.query;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import online.qiqiang.forest.common.utils.DateConvertor;
import online.qiqiang.forest.orm.mybatisplus.query.QueryParamBuilder;
import online.qiqiang.forest.query.*;

import java.util.Date;

@RunWith(JUnit4.class)
public class QueryParamBuilderTest {

    @Test
    public void toWrapper() {
        StudentParam studentParam = new StudentParam();
        Date start = DateConvertor.parseToDate("2015-09-01", DateConvertor.Pattern.USUAL_DATE);
        Date end = DateConvertor.parseToDate("2017-09-01", DateConvertor.Pattern.USUAL_DATE);
        studentParam.<StudentParam>select("name", "age")
                .setName("forest")
                .setAge(5)
                .setBirthdayRange(new Date[]{start, end})
                .setPaging(1, 20)
                .addSort(new SortColumn("age", SortColumn.Sort.Desc, 1))
                .addSort(new SortColumn("birthday", SortColumn.Sort.Desc))
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
        @Sort
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