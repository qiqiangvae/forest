package online.qiqiang.forest.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author qiqiang
 */
public class CommandUtils {
    private static boolean printResult = false;

    public static void setPrintResult(boolean printResult) {
        CommandUtils.printResult = printResult;
    }

    public static List<String> execute(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            InputStream inputStream = process.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            IoUtils.copy(inputStream, os);
            String result = new String(os.toByteArray(), StandardCharsets.UTF_8);
            if (printResult) {
                System.out.println(result);
            }
            if (StringUtils.isBlank(result)) {
                return Collections.emptyList();
            }
            return Arrays.asList(result.split("\\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
