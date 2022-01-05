package online.qiqiang.forest.common.utils;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.FileUtil;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/**
 * @author qiqiang
 */
public class FileTest {
    @Test
    public void fileReadTest() throws IOException {
        String filename = "/Users/qiqiang/Downloads/SMB_OP_BALANCE20210818_20220104185108196";
        StopWatch watch = new StopWatch();
        watch.start("inmemory");
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {

        }
        watch.stop();
        System.out.println(watch.prettyPrint());

        RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "r");
        watch.start("localFile");
        while ((line = FileUtil.readLine(randomAccessFile, StandardCharsets.UTF_8)) != null) {

        }
        watch.stop();
        System.out.println(watch.prettyPrint());

    }
}
