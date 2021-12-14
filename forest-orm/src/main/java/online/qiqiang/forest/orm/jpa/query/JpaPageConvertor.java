package online.qiqiang.forest.orm.jpa.query;

import online.qiqiang.forest.common.utils.BeanUtils;
import online.qiqiang.forest.query.page.ForestPage;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * 将mybatis plus分页转换为框架自带的分页信息
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class JpaPageConvertor {
    public static <T> ForestPage<T> convert(Page<T> page) {
        return new ForestPage<T>()
                .setContent(page.getContent())
                .setCurrent((long) page.getPageable().getPageNumber())
                .setCurrentSize(page.getNumberOfElements())
                .setPageSize(page.getSize())
                .setPages((long) page.getTotalPages())
                .setTotalSize(page.getTotalElements());
    }

    public static <T, K> ForestPage<K> convert(Page<T> page, Class<K> clazz, BiConsumer<T, K> consumer) {
        List<K> context = BeanUtils.copy(page.getContent(), clazz, consumer);
        return new ForestPage<K>()
                .setContent(context)
                .setCurrent((long) page.getPageable().getPageNumber())
                .setCurrentSize(context.size())
                .setPageSize(page.getSize())
                .setPages((long) page.getTotalPages())
                .setTotalSize(page.getTotalElements());
    }

    public static <T, K> ForestPage<K> convert(Page<T> page, Class<K> clazz) {
        return convert(page, clazz, null);
    }
}
