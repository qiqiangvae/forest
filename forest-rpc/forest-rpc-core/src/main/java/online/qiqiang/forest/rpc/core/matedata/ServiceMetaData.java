package online.qiqiang.forest.rpc.core.matedata;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author qiqiang
 */
@Data
public class ServiceMetaData implements Serializable {
    /**
     * 服务名
     */
    private Class<?> clazz;

    private String methodName;

    private Class<?>[] paramClasses;

    private Class<?> returnClazz;

    private String version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceMetaData that = (ServiceMetaData) o;
        return Objects.equals(clazz, that.clazz) && Objects.equals(methodName, that.methodName) && Arrays.equals(paramClasses, that.paramClasses) && Objects.equals(returnClazz, that.returnClazz) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(clazz, methodName, returnClazz, version);
        result = 31 * result + Arrays.hashCode(paramClasses);
        return result;
    }
}
