package org.qiqiang.forest.common.utils;

import junit.framework.TestCase;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class JsonUtilsTest extends TestCase {

    @Test
    public void read2ListTest() {
        List<Obj> objs = JsonUtils.read2List("[{\"name\":\"forest\",\"age\":1},{\"name\":\"kimi\",\"age\":2}]", Obj.class);
        assertEquals("解析失败", 2, objs.size());
    }

    @Getter
    @Setter
    static class Obj {
        String name;
        int age;
    }

}