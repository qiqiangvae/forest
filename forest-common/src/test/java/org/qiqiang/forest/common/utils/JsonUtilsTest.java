package org.nature.forest.common.utils;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class JsonUtilsTest extends BaseTest {

    @Test
    public void read2ListTest() {
        List<Obj> objs = JsonUtils.read2List("[{\"name\":\"forest\",\"age\":1},{\"name\":\"kimi\",\"age\":2}]", Obj.class);
        assertEquals("解析失败", 2, objs.size());
    }

    @Test
    public void writeSpecialObjectTest() {
        String json = JsonUtils.write2String(CompletableFuture.supplyAsync(new Supplier<Object>() {
            @Override
            public Object get() {
                return null;
            }
        }));
        System.out.println(json);
    }

    @Getter
    @Setter
    static class Obj {
        String name;
        int age;
    }

}