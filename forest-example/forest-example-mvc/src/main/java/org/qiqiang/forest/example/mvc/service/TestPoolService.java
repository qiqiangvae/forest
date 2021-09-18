package org.qiqiang.forest.example.mvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author qiqiang
 */
@Service
@Slf4j
public class TestPoolService {
    @Async
    public void print() {
        log.info("Spring @Async");
    }
}
