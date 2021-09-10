package org.qiqiang.forest.example.mvc.controller;

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
public class MvcTestController {
    @RequestMapping("/timeTest")
    public String timeTest(@RequestBody TimeTestVO timeTestVO) {
        System.out.println(timeTestVO);
        return "success";
    }

    @PostMapping("/xssTest")
    public String xssTest(@RequestBody XssTestVO xssTestVO) {
        return xssTestVO.getContext();
    }
}
