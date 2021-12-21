package online.qiqiang.forest.orm.jpa.query;

import online.qiqiang.forest.common.utils.BeanUtils;
import online.qiqiang.forest.query.page.ForestPage;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * 将 Jpa 分页转换为框架自带的分页信息
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class JpaPageConvertor {
    public static <T> ForestPage<T> convert(Page<T> page) {
        return new ForestPage<T>()
                .setContent(page.getContent())
                .setCurrent((long) page.getNumber())
                .setCurrentSize(page.getContent().size())
                .setPageSize(page.getSize())
                .setPages((long) page.getTotalPages())
                .setTotalSize(page.getTotalElements());
    }

    public static <S, T> ForestPage<T> convert(Page<S> page, Class<T> clazz, BiConsumer<S, T> consumer) {
        List<T> context = BeanUtils.copy(page.getContent(), clazz, consumer);
        return new ForestPage<T>()
                .setContent(context)
                .setCurrent((long) page.getNumber())
                .setCurrentSize(context.size())
                .setPageSize(page.getSize())
                .setPages((long) page.getTotalPages())
                .setTotalSize(page.getTotalElements());
    }

    public static <S, T> ForestPage<T> convert(Page<S> page, Class<T> clazz) {
        return convert(page, clazz, null);
    }
}
