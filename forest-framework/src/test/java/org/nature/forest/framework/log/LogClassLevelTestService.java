package org.nature.forest.framework.log;

import org.springframework.stereotype.Service;

/**
 * @author qiqiang
 */
@LogPrinter(ignoreResp = "age")
@Service
public class LogClassLevelTestService {

    public TestModel test(TestModel testModel, String second) {
        return testModel;
    }
}
