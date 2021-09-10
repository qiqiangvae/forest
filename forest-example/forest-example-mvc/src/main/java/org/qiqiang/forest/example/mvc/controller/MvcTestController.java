package org.qiqiang.forest.example.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.qiqiang.forest.example.mvc.vo.TimeTestVO;
import org.qiqiang.forest.example.mvc.vo.XssTestVO;
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
    public String timeTest(@RequestBody TimeTestVO timeTestVO) {
        System.out.println(timeTestVO);
        return "success";
    }

    @PostMapping("/xssTest")
    public String xssTest(@RequestBody XssTestVO xssTestVO) {
        return xssTestVO.getContext();
    }

    public void printTraceId() {
        log.info("hello world 2");
    }
}
