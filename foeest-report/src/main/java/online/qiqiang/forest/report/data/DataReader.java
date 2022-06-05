package online.qiqiang.forest.report.data;

/**
 * @author qiqiang
 */
public interface DataReader<C extends DataCondition> {
    void read(C dataCondition);
}
