package org.qiqiang.forest.common.utils.reflection;

import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyUtilsTest {

    @Test
    public void getValue() {
        Model model = new Model();
        model.setName("forest");
        model.setId("1");
        String name = PropertyUtils.getValue("name", model);
        Assert.assertEquals(name, "forest");
        String id = PropertyUtils.getValue("id", model);
        Assert.assertEquals(id, "1");
    }

    @Setter
    @Getter
    static class Model extends ParentModel {
        private String name;
    }

    @Setter
    @Getter
    static class ParentModel {
        private String id;
    }

}