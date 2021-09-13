package org.qiqiang.forest.mvc.xss;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

/**
 * @author qiqiang
 */
@Slf4j
public class JsoupUtils {
    private static final int QUOTATION_MARKS_LENGTH = 2;


    /**
     * 标签白名单
     * relaxed() 允许的标签:
     * a, b, blockquote, br, caption, cite, code, col, colgroup, dd, dl, dt, em, h1, h2, h3, h4,
     * h5, h6, i, img, li, ol, p, pre, q, small, strike, strong, sub, sup, table, tbody, td, tfoot, th, thead, tr, u, ul。
     * 结果不包含标签rel=nofollow ，如果需要可以手动添加。
     */
    static Safelist WHITELIST = Safelist.relaxed();

    /**
     * 配置过滤化参数,不对代码进行格式化
     */
    static Document.OutputSettings OUTPUT_SETTINGS = new Document.OutputSettings().prettyPrint(false);

    static {
        // 富文本编辑时一些样式是使用style来进行实现的
        // 比如红色字体 style="color:red;"
        // 所以需要给所有标签添加style属性
        WHITELIST.addAttributes(":all", "style");
    }

    public static String clean(String content) {
        if (content != null && content.length() > 0) {
            content = content.trim();
            return Jsoup.clean(content, "", WHITELIST, OUTPUT_SETTINGS);
        }
        return null;
    }

    /**
     * 处理Json类型的Html标签,进行xss过滤
     */
    public static String cleanJson(String s) {
        //先处理双引号的问题
        s = jsonStringConvert(s);
        return clean(s);
    }

    public static String jsonStringConvert(String s) {
        char[] temp = s.toCharArray();
        int n = temp.length;
        for (int i = 0; i < n; i++) {
            if (temp[i] == ':' && temp[i + 1] == '"') {
                for (int j = i + QUOTATION_MARKS_LENGTH; j < n; j++) {
                    if (temp[j] == '"') {
                        //如果该字符为双引号,下个字符不是逗号或大括号,替换
                        if (temp[j + 1] != ',' && temp[j + 1] != '}') {
                            //将json字符串本身的双引号以外的双引号变成单引号
                            temp[j] = '\'';
                        } else if (temp[j + 1] == ',' || temp[j + 1] == '}') {
                            break;
                        }
                    }
                }
            }
        }
        return new String(temp);
    }
}
