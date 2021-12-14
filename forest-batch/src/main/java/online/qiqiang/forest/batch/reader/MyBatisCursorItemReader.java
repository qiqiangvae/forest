package online.qiqiang.forest.batch.reader;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.ClassUtils.getShortName;

/**
 * @author qiqiang
 */

@Slf4j
@SuppressWarnings("unused")
public class MyBatisCursorItemReader<T> extends AbstractItemCountingItemStreamItemReader<T>
        implements InitializingBean {

    private String queryId;

    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;

    private Map<String, Object> parameterValues;

    private Cursor<T> cursor;
    private Iterator<T> cursorIterator;

    public MyBatisCursorItemReader() {
        setName(getShortName(org.mybatis.spring.batch.MyBatisCursorItemReader.class));
    }

    @Override
    protected T doRead() {
        T next = null;
        if (cursorIterator.hasNext()) {
            next = cursorIterator.next();
        }
        return next;
    }

    @Override
    protected void doOpen() {
        Map<String, Object> parameters = new HashMap<>();
        if (parameterValues != null) {
            parameters.putAll(parameterValues);
        }

        sqlSession = sqlSessionFactory.openSession(ExecutorType.SIMPLE);
        cursor = sqlSession.selectCursor(queryId, parameters);
        cursorIterator = cursor.iterator();
    }

    /**
     * springboot下无需手动关闭session，spring会自动关闭
     */
    @Override
    protected void doClose() {
        try {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception ignore) {

        }

        try {
            if (sqlSession != null) {
                sqlSession.close();
            }
        } catch (Exception ignore) {

        }
        cursorIterator = null;
    }

    /**
     * Check mandatory properties.
     *
     * @see InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {
        notNull(sqlSessionFactory, "A SqlSessionFactory is required.");
        notNull(queryId, "A queryId is required.");
    }

    /**
     * Public setter for {@link SqlSessionFactory} for injection purposes.
     *
     * @param sqlSessionFactory a factory object for the {@link SqlSession}.
     */
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Public setter for the statement id identifying the statement in the SqlMap configuration file.
     *
     * @param queryId the id for the statement
     */
    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    /**
     * The parameter values to be used for the query execution.
     *
     * @param parameterValues the values keyed by the parameter named used in the query string.
     */
    public void setParameterValues(Map<String, Object> parameterValues) {
        this.parameterValues = parameterValues;
    }
}
