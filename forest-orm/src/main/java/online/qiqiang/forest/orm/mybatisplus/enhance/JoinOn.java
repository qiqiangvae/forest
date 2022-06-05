package online.qiqiang.forest.orm.mybatisplus.enhance;

import online.qiqiang.forest.common.java.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * join on 条件
 *
 * @author qiqiang
 */
public class JoinOn {
    private List<Pair<String, String>> onPairList = new ArrayList<>();

    private JoinOn() {

    }

    public static JoinOn on(String left, String right) {
        JoinOn joinOn = new JoinOn();
        joinOn.add(left, right);
        return joinOn;
    }

    public JoinOn add(String left, String right) {
        // onPairList.add(new Pair<>(left, right));
        return this;
    }

}
