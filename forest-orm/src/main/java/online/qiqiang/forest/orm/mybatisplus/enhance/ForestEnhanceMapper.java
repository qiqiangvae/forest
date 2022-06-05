package online.qiqiang.forest.orm.mybatisplus.enhance;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import online.qiqiang.forest.common.java.util.Pair;
import online.qiqiang.forest.orm.mybatisplus.ForestMybatisPlusConst;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ResultHandler;

import java.util.Collection;
import java.util.List;

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

    /**
     * replace into
     *
     * @param entity 实体数据
     * @return 是否成功
     */
    boolean replace(T entity);

    /**
     * left join
     *
     * @param leftWrapper  左条件
     * @param rightWrapper 右条件
     * @param joinOn       on 条件
     * @param <R>          右实体
     * @return 数据集
     */
    <R> List<Pair<T, R>> leftJoin(@Param(ForestMybatisPlusConst.LEFT_WRAPPER) Wrapper<T> leftWrapper, @Param(ForestMybatisPlusConst.RIGHT_WRAPPER) Wrapper<R> rightWrapper, @Param(ForestMybatisPlusConst.JOIN_ON) JoinOn joinOn);
}
