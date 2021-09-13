package org.qiqiang.forest.example.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.qiqiang.forest.example.mvc.logwriter.ExampleLogWriter;
import org.qiqiang.forest.example.mvc.vo.TimeTestVO;
import org.qiqiang.forest.example.mvc.vo.XssTestVO;
import org.qiqiang.forest.framework.context.ForestContext;
import org.qiqiang.forest.framework.log.LogPrinter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiqiang
 */
@RestController
@Slf4j
public class MvcTestController {
    @RequestMapping("/hello")
    public String hello() {
        log.info("hello");
        printTraceId();
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
        log.info("hello world 2");
    }

    @RequestMapping("contextTest")
    public String contextTest() {
        return ForestContext.getRemote("user");
    }
}
