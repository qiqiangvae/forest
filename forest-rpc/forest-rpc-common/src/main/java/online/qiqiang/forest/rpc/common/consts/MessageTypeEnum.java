package online.qiqiang.forest.rpc.common.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author qiqiang
 */
@AllArgsConstructor
@Getter
public enum MessageTypeEnum {
    PING(0),
    PONG(1),
    EMPTY(2),
    REQ(3),
    RESP(4);
    private final int code;

    public static MessageTypeEnum ofCode(int code) {
        return Stream.of(values()).filter(item -> item.getCode() == code).findAny()
                .orElseThrow(() -> new NullPointerException("不存在code" + code + "的类型."));
    }
}
