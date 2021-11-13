package online.qiqiang.forest.common.utils;

import org.junit.Test;

import java.util.List;

public class ShellUtilsTest {

    @Test
    public void execute() {
        List<String> list = CommandUtils.execute("ls");
        System.out.println(list);
    }
}