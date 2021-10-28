package online.qiqiang.forest.common.utils;

import lombok.extern.slf4j.Slf4j;
import online.qiqiang.forest.common.java.util.OptionalCollection;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 批量操作工具类
 *
 * @author qiqiang
 */
@Slf4j
public class BatchUtils {

    /**
     * 批量操作
     * 通过 Generator#add 添加元素，每当元素达到 max 时，执行 handler 的方法，结束时没满 max 的集合需要再执行一次 handler 方法
     *
     * @param max       每批次最大数量
     * @param generator 生成对象
     * @param handler   满 max 时的操作
     * @param <T>       对象类型
     * @return 总数量
     */
    public static <T> long execute(int max, Consumer<Generator<T>> generator, Consumer<OptionalCollection<T>> handler) {
        final Generator<T> g = new Generator<>(max, handler);
        generator.accept(g);
        g.end();
        return g.count;
    }

    public static long execute(int max, Consumer<ExGenerator> generator, Map<Predicate<Class<?>>, Consumer<OptionalCollection<Object>>> consumerMap) {
        final ExGenerator g = new ExGenerator(max, consumerMap);
        generator.accept(g);
        g.end();
        return g.count;
    }

    public static class Factory {
        private final Map<Predicate<Class<?>>, Consumer<OptionalCollection<Object>>> consumerMap;

        private Factory() {
            consumerMap = new LinkedHashMap<>();
        }

        public static Factory builder() {
            return new Factory();
        }

        public Factory add(Predicate<Class<?>> predicate, Consumer<OptionalCollection<Object>> consumer) {
            consumerMap.put(predicate, consumer);
            return this;
        }

        public Map<Predicate<Class<?>>, Consumer<OptionalCollection<Object>>> build() {
            return consumerMap;
        }
    }

    public static class Generator<T> {
        final List<T> list;
        final int max;
        final Consumer<OptionalCollection<T>> handler;
        long count;

        public Generator(int max, Consumer<OptionalCollection<T>> handler) {
            this.max = max;
            this.handler = handler;
            list = new ArrayList<>(max);
        }


        public void add(T object) {
            list.add(object);
            if (list.size() == max) {
                count += max;
                handler.accept(OptionalCollection.of(list));
                list.clear();
            }
        }

        private void end() {
            count += list.size();
            handler.accept(OptionalCollection.of(list));
        }
    }

    public static class ExGenerator {
        final Map<Class<?>, List<Object>> map;
        final int max;
        final Map<Predicate<Class<?>>, Consumer<OptionalCollection<Object>>> consumerMap;
        long count;

        public ExGenerator(int max, Map<Predicate<Class<?>>, Consumer<OptionalCollection<Object>>> consumerMap) {
            this.max = max;
            this.consumerMap = consumerMap;
            map = new HashMap<>();
        }


        public void add(Object object) {
            Class<?> clazz = object.getClass();
            List<Object> list = map.computeIfAbsent(clazz, k -> new ArrayList<>(max));
            list.add(object);
            if (list.size() == max) {
                consumerMap.entrySet().stream()
                        .filter(entry -> entry.getKey().test(clazz))
                        .forEach(handler -> {
                            handler.getValue().accept(OptionalCollection.of(list));
                        });
                count += max;
                list.clear();
            }
        }

        private void end() {
            map.forEach((key, value) -> {
                consumerMap.entrySet().stream()
                        .filter(predicateConsumerEntry -> predicateConsumerEntry.getKey().test(key))
                        .forEach(handler -> {
                            handler.getValue().accept(OptionalCollection.of(value));
                        });
                count += value.size();
            });
        }
    }
}
