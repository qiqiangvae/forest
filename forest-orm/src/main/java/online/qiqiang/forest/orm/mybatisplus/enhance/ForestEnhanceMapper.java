package online.qiqiang.forest.orm.mybatisplus.enhance;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ResultHandler;

import java.util.Collection;

/**
 * forest 通用 mapper
 *
 * @author qiqiang
 */
public interface ForestEnhanceMapper<T> extends BaseMapper<T> {
    /**
     * 通过流获取数据
     *
     * @param wrapper 条件构造
     * @param handler {@linkplain ResultHandler}
     */
    void fetchByStream(@Param(Constants.WRAPPER) Wrapper<T> wrapper, ResultHandler<T> handler);

    /**
     * 通过游标获取数据
     *
     * @param wrapper 条件构造
     * @return 游标
     */
    Cursor<T> fetchByCursor(@Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 批量插入
     *
     * @param list 需要插入的列表
     * @return success
     */
    boolean insertBatch(@Param("list") Collection<T> list);
}
