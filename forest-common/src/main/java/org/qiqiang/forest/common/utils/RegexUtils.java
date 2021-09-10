package org.qiqiang.forest.common.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 *
 * @author qiqiang
 */
public class RegexUtils {

    public static ArrayList<String[]> getListMatcher(String str, String regex) {
        ArrayList<String[]> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            int i = matcher.groupCount();
            String[] s = new String[i];
            for (int j = 1; j < (i + 1); j++) {
                s[j - 1] = matcher.group(j);
            }
            list.add(s);
        }
        return list;
    }

    public static String replaceMatcher(String str, String regex, String tostr) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        StringBuffer buf = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buf, tostr);
        }
        matcher.appendTail(buf);
        return buf.toString();
    }

    public static String getHiddenInputRegex() {
        return "<input[^>]+type=\"hidden\"[^>]+name=\"([^\"]+)\"[^>]+value=\"([^\"]*)\"[^>]*>";
    }

    public static String getEditorImgRegex() {
        return "<img[^>]+sid=['\"]([^'\"]+)['\"][^>]*>";
    }

    public static Integer[] getEditorImgList(String str) {
        ArrayList<String[]> list = RegexUtils.getListMatcher(str, RegexUtils.getEditorImgRegex());
        Set<Integer> set = new HashSet<>();
        for (String[] aa : list) {
            set.add(NumberUtils.parseInt(aa[0]));
        }
        return set.toArray(new Integer[0]);
    }

    public static boolean compare(String regex, String str) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean find(String regex, String str) {
        Pattern reg = Pattern.compile(regex);
        Matcher matcher = reg.matcher(str);
        return matcher.find();
    }

    public static String replaceBlankSpan(String str) {
        return RegexUtils
                .replaceMatcher(
                        str,
                        "<SPAN style=\"FONT-SIZE: 0pt; COLOR: #ffffff\"[^>]*>[^<]*</SPAN>",
                        "");
    }

    public static String toLink(String content) {
        content = RegexUtils
                .replaceMatcher(
                        content,
                        "((?:(?:http|ftp|https):\\/\\/){0,1}(?:(?:\\w|\\d)+\\.)+(?:\\S+))",
                        "<a href=\"$1\">$1</a>");
        return content;
    }
}
