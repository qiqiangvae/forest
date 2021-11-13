package online.qiqiang.forest.orm.mybatisplus.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.qiqiang.forest.common.utils.BeanUtils;
import online.qiqiang.forest.query.page.ForestPage;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * 将mybatis plus分页转换为框架自带的分页信息
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class PageConvertor {
    public static <T> ForestPage<T> convert(Page<T> page) {
        return new ForestPage<T>()
                .setContext(page.getRecords())
                .setCurrent(page.getCurrent())
                .setPageSize((int) page.getSize())
                .setPages(page.getPages())
                .setTotalSize(page.getTotal());
    }

    public static <T, K> ForestPage<K> convert(Page<T> page, Class<K> clazz, BiConsumer<T, K> consumer) {
        List<K> context = BeanUtils.copy(page.getRecords(), clazz, consumer);
        return new ForestPage<K>()
                .setContext(context)
                .setCurrent(page.getCurrent())
                .setPageSize((int) page.getSize())
                .setPages(page.getPages())
                .setTotalSize(page.getTotal());
    }

    public static <T, K> ForestPage<K> convert(Page<T> page, Class<K> clazz) {
        return convert(page, clazz, null);
    }
}
