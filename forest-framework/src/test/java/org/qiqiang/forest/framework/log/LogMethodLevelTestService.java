package org.qiqiang.forest.framework.log;

import org.springframework.stereotype.Service;

/**
 * @author qiqiang
 */
@Service
public class LogMethodLevelTestService {

    @LogPrinter(ignoreReq = "testModel.name")
    public TestModel test(TestModel testModel, String second) {
        return testModel;
    }
}
