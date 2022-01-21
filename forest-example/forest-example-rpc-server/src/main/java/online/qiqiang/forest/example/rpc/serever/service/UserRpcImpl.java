package online.qiqiang.forest.example.rpc.serever.service;

import online.qiqiang.forest.example.rpc.api.UserRpc;
import online.qiqiang.forest.rpc.core.annotation.ForestService;
import org.springframework.stereotype.Service;

/**
 * @author qiqiang
 */
@Service
@ForestService
public class UserRpcImpl implements UserRpc {
    @Override
    public String username(String index) {
        return "forestRpc-" + index;
    }
}
