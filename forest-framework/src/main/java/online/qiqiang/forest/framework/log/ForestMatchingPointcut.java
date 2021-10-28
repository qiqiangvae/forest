package online.qiqiang.forest.framework.log;

import org.apache.commons.collections4.CollectionUtils;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiqiang
 */
public class ForestMatchingPointcut implements Pointcut, ClassFilter, MethodMatcher {

    private final Set<String> classnameSet = new HashSet<>();

    private final ClassFilter classFilter = new AnnotationClassFilter(LogPrinter.class);

    public ForestMatchingPointcut(Set<String> packagePath, ResourceLoader resourceLoader) {
        if (CollectionUtils.isNotEmpty(packagePath)) {
            ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
            final Set<String> paths = packagePath.stream()
                    .map(path -> StringUtils.replace(path, ".", "/"))
                    .collect(Collectors.toSet());
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourceLoader);
            ClassLoader classLoader = ForestMatchingPointcut.class.getClassLoader();
            try {
                MetadataReader reader;
                ClassMetadata classMetadata;
                Class<?> clazz;
                for (String path : paths) {
                    String locationPattern = "classpath*:" + path + "/*.class";
                    Resource[] resources = resolver.getResources(locationPattern);
                    for (Resource resource : resources) {
                        reader = readerFactory.getMetadataReader(resource);
                        classMetadata = reader.getClassMetadata();
                        String className = classMetadata.getClassName();
                        clazz = classLoader.loadClass(className);
                        if (!clazz.isInterface() && !clazz.isEnum() && !clazz.isAnnotation()) {
                            classnameSet.add(clazz.getName());
                        }
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
        return Stream.of(methods)
                .map(method -> method.getAnnotation(LogPrinter.class))
                .anyMatch(Objects::nonNull);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        // 只支持 public 方法且不是静态方法
        return Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers());
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
