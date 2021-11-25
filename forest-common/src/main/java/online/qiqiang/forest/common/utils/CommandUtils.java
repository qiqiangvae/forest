package online.qiqiang.forest.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qiqiang
 */
public class CommandUtils {
    private static boolean printResult = false;
    private static final String[] envArray;

    static {
        Map<String, String> env = System.getenv();
        envArray = env.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).toArray(String[]::new);
    }

    public static void setPrintResult(boolean printResult) {
        CommandUtils.printResult = printResult;
    }

    public static List<String> execute(String command) {
        return execute(command, System.getProperty("user.dir"));
    }

    public static List<String> execute(String command, String workDir) {
        try {
            String[] commands = command.split("\\s+");
            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            processBuilder.directory(new File(workDir));
            Process process = processBuilder.start();
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
