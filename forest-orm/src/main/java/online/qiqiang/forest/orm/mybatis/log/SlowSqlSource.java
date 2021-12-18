package online.qiqiang.forest.orm.mybatis.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qiqiang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlowSqlSource {
    private String sqlId;
    private String sql;
    private long time;
}
