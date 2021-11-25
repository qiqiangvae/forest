package online.qiqiang.forest.common.utils;

import org.junit.Test;

import java.util.List;

public class CommandUtilsTest {

    @Test
    public void execute() {
        List<String> list = CommandUtils.execute("hexo version");
        System.out.println(list);
    }
}