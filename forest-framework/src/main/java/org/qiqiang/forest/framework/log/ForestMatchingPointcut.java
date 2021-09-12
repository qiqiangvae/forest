package org.qiqiang.forest.framework.log;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * @author qiqiang
 */
public class ForestMatchingPointcut implements Pointcut, ClassFilter, MethodMatcher {

    private final Set<String> classnameSet = new HashSet<>();

    private final ClassFilter classFilter = new AnnotationClassFilter(LogPrinter.class);

    public ForestMatchingPointcut(String packagePath, ResourceLoader resourceLoader) {
        if (StringUtils.isNotBlank(packagePath)) {
            ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
            packagePath = StringUtils.replace(packagePath, ".", "/");
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourceLoader);
            ClassLoader classLoader = ForestMatchingPointcut.class.getClassLoader();
            try {
                String locationPattern = "classpath*:" + packagePath + "/*.class";
                Resource[] resources = resolver.getResources(locationPattern);
                for (Resource resource : resources) {
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    ClassMetadata classMetadata = reader.getClassMetadata();
                    String className = classMetadata.getClassName();
                    Class<?> clazz = classLoader.loadClass(className);
                    if (!clazz.isInterface()) {
                        classnameSet.add(clazz.getName());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e.getLocalizedMessage(), e);
            }
        }
    }

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    @Override
    public boolean matches(Class<?> clazz) {
        // 扫描包下的所有类都可以
        if (classnameSet.contains(clazz.getName())) {
            return true;
        }
        // 被注解声明的类也可以
        if (classFilter.matches(clazz)) {
            return true;
        }
        // 方法里被注解声明的类也可以
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            LogPrinter logPrinter = method.getAnnotation(LogPrinter.class);
            if (logPrinter != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        // 只支持 public 方法
        return method.getModifiers() == Modifier.PUBLIC;
    }

    @Override
    public boolean isRuntime() {
        return false;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        throw new UnsupportedOperationException("Illegal MethodMatcher usage");
    }
}
