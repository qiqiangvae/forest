package org.qiqiang.forest.common.utils;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.List;

public class JsonUtilsTest extends BaseTest {

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