package online.qiqiang.forest.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author qiqiang
 */

public class CommonTest extends BaseTest {

    @Test
    public void split() {
        String text = "123&&456&";
        System.out.println(text.split("&").length);
        System.out.println(text.split("&", 4).length);
        System.out.println(text.split("&", -1).length);
        System.out.println(StringUtils.split(text, "&").length);
        System.out.println(StringUtils.split(text, "&", -1).length);
        System.out.println(StringUtils.split(text, "&", 3).length);
    }
}
