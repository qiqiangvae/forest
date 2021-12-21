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
                .setContent(page.getRecords())
                .setCurrent(page.getCurrent())
                .setCurrentSize(page.getRecords().size())
                .setPageSize((int) page.getSize())
                .setPages(page.getPages())
                .setTotalSize(page.getTotal());
    }

    public static <S, T> ForestPage<T> convert(Page<S> page, Class<T> clazz, BiConsumer<S, T> consumer) {
        List<T> context = BeanUtils.copy(page.getRecords(), clazz, consumer);
        return new ForestPage<T>()
                .setContent(context)
                .setCurrent(page.getCurrent())
                .setCurrentSize(context.size())
                .setPageSize((int) page.getSize())
                .setPages(page.getPages())
                .setTotalSize(page.getTotal());
    }

    public static <S, T> ForestPage<T> convert(Page<S> page, Class<T> clazz) {
        return convert(page, clazz, null);
    }
}
