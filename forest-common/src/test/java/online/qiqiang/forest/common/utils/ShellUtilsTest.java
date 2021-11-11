package online.qiqiang.forest.common.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ShellUtilsTest {

    @Test
    public void execute() {
        ShellUtils.execute(ShellUtils.ShellType.bash, "ls");
    }
}