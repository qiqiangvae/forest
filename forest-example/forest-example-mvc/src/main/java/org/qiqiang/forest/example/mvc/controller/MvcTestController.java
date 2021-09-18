package org.qiqiang.forest.example.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.qiqiang.forest.example.mvc.logwriter.ExampleLogWriter;
import org.qiqiang.forest.example.mvc.service.TestPoolService;
import org.qiqiang.forest.example.mvc.vo.TimeTestVO;
import org.qiqiang.forest.example.mvc.vo.XssTestVO;
import org.qiqiang.forest.framework.context.ForestContext;
import org.qiqiang.forest.framework.log.LogPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

/**
 * @author qiqiang
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class MvcTestController {
    private final TestPoolService testPoolService;
    private final ThreadPoolExecutor threadPoolExecutor;

    @RequestMapping("/hello")
    public String hello() {
        log.info("hello");
        printTraceId();
        IntStream.range(0, 10).forEach(i -> testPoolService.print());
        return "hello";
    }

    @RequestMapping("/timeTest")
    @LogPrinter(ignoreReq = {"timeTestVO.date", "timeTestVO.xssTestVO.context"}, ignoreResp = "xssTestVO.context", writer = ExampleLogWriter.class)
    public TimeTestVO timeTest(@RequestBody TimeTestVO timeTestVO) {
        System.out.println(timeTestVO);
        return timeTestVO;
    }

    @PostMapping("/xssTest")
    public XssTestVO xssTest(@RequestBody XssTestVO xssTestVO) {
        return xssTestVO;
    }

    public void printTraceId() {
        IntStream.range(0, 10).forEach(
                i -> threadPoolExecutor.execute(() -> log.info("hello world 2"))
        );
    }

    @RequestMapping("contextTest")
    public String contextTest() {
        return ForestContext.getRemote("user");
    }

    @RequestMapping("/upload")
    @LogPrinter(ignoreReq = {"timeTestVO.date", "timeTestVO.xssTestVO.context"}, ignoreResp = "xssTestVO.context", writer = ExampleLogWriter.class)
    public String timeTest(MultipartFile file) {
        return file.getName();
    }

    @PostMapping("/arrayTest")
    @LogPrinter(ignoreReq = {"timeTestVO.date", "timeTestVO.xssTestVO.context"}, ignoreResp = "xssTestVO.context", writer = ExampleLogWriter.class)
    public TimeTestVO[] arrayTest(@RequestBody TimeTestVO timeTestVO) {
        return new TimeTestVO[]{timeTestVO, timeTestVO, timeTestVO};
    }

}
