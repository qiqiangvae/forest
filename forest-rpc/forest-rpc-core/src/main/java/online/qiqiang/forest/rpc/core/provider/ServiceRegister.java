package online.qiqiang.forest.rpc.core.provider;

import online.qiqiang.forest.rpc.core.annotation.ForestService;
import online.qiqiang.forest.rpc.core.matedata.ServiceMetaData;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务注册
 *
 * @author qiqiang
 */
public class ServiceRegister {

    private static final Map<ServiceMetaData, Object> serviceMap = new ConcurrentHashMap<>();


    public static void register(Class<?> clazz, Method method, ForestService forestService, Object service) {
        ServiceMetaData serviceMetaData = new ServiceMetaData();
        serviceMetaData.setMethodName(method.getName());
        serviceMetaData.setVersion(forestService.version());
        serviceMetaData.setParamClasses(method.getParameterTypes());
        serviceMetaData.setClazz(clazz);
        serviceMetaData.setReturnClazz(method.getReturnType());
        serviceMap.putIfAbsent(serviceMetaData, service);
    }

    public static void register(Object service) {
        ForestService forestService = service.getClass().getAnnotation(ForestService.class);
        if (forestService == null) {
            return;
        }
        Method[] methods = service.getClass().getDeclaredMethods();
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            return;
        }
        for (Method method : methods) {
            register(interfaces[0], method, forestService, service);
        }
    }

    public static Object getService(ServiceMetaData metaData) {
        return serviceMap.get(metaData);
    }
}
