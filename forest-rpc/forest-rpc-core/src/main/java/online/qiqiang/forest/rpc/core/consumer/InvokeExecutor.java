package online.qiqiang.forest.rpc.core.consumer;

import online.qiqiang.forest.rpc.core.matedata.RpcWrapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author qiqiang
 */
public class InvokeExecutor {
    private static final Map<String, CountDownLatch> WAIT_MAP = new ConcurrentHashMap<>();
    private static final Map<String, Object> RESPONSE_MAP = new ConcurrentHashMap<>();


    public static Object getResponse(String requestId, int timeout) {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            WAIT_MAP.put(requestId, latch);
            RpcResponseObject value = new RpcResponseObject();
            RESPONSE_MAP.put(requestId, value);
            if (timeout <= 0) {
                latch.await();
            } else {
                latch.await(timeout, TimeUnit.MILLISECONDS);
            }
            Object response = RESPONSE_MAP.get(requestId);
            if (value == response) {
                throw new TimeoutException("请求超时，请求时间[" + (System.currentTimeMillis() - value.getStartTime()) + "]");
            }
            RpcWrapper rpcWrapper = (RpcWrapper) response;
            if (rpcWrapper.getException() != null) {
                throw rpcWrapper.getException();
            }
            return rpcWrapper.getResponse();
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            // 移除元素
            WAIT_MAP.remove(requestId);
            RESPONSE_MAP.remove(requestId);
        }
    }

    public static void setResponse(String requestId, RpcWrapper response) {
        RESPONSE_MAP.put(requestId, response);
        CountDownLatch countDownLatch = WAIT_MAP.get(requestId);
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }
}
