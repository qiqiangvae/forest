package org.nature.forest.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.nature.forest.common.exception.JsonForestException;

import java.time.ZoneId;
import java.util.*;

/**
 * json 序列化和反序列化工具类
 *
 * @author qiqiang
 */
@Slf4j
@SuppressWarnings("unused")
public class JsonUtils {
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final TypeReference<LinkedHashMap<String, Object>> MAP = new TypeReference<LinkedHashMap<String, Object>>() {
    };

    /* 默认配置 */
    static {
        //序列化的时候序列对象的所有属性
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //反序列化的时候如果多了其他属性,不抛出异常
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //如果是空对象的时候,不抛异常
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //按照首字母排序
        OBJECT_MAPPER.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        //属性为null的转换
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //时间序列化问题
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));

    }

    /**
     * 可以设置自己的 ObjectMapper，如使用 Spring 的 ObjectMapper
     */
    public static void init(ObjectMapper objectMapper) {
        LoggerUtils.info(log, () -> log.info("使用自定义的 ObjectMapper"));
        JsonUtils.OBJECT_MAPPER = objectMapper;
    }

    /**
     * 对象转 json string
     *
     * @param object 对象
     * @return json String
     */
    public static String write2String(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonForestException("对象转 json string 异常", e);
        }
    }

    public static HashMap<String, Object> convert2Map(Object object) {
        try {
            return OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(object), MAP);
        } catch (JsonProcessingException e) {
            throw new JsonForestException("对象转 json string 异常", e);
        }
    }

    /**
     * json string 转对象
     *
     * @param jsonString json 内容
     * @param clazz      对象 class
     * @param <T>        对象 class
     * @return 对象
     */
    public static <T> T read2Object(String jsonString, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new JsonForestException("json string 转对象异常", e);
        }
    }

    /**
     * json string 转 List
     *
     * @param jsonString json context
     * @param clazz      Target class
     * @param <T>        Target class
     * @return list
     */
    public static <T> List<T> read2List(String jsonString, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, getArrayListType(clazz));
        } catch (JsonProcessingException e) {
            throw new JsonForestException("json string 转对象异常", e);
        }
    }

    private static JavaType getArrayListType(Class<?>... elementClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(ArrayList.class, elementClasses);
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}
