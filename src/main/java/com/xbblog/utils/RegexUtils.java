package com.xbblog.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    /**
     * 利用正则表达式在原字符串中获取字符
     * @param pattern
     * @param origin
     * @return
     */
    public static List<List<String>> patternFindStr(String pattern, String origin)
    {
        List<List<String>> resultList = new ArrayList<List<String>>();

        Matcher matcherDuration  = Pattern.compile(pattern).matcher(origin);
        while(matcherDuration.find())
        {
            List<String> singleList = new ArrayList<String>();
            for(int i = 0; i < matcherDuration.groupCount(); i++)
            {
                singleList.add(matcherDuration.group(i + 1));
            }
            resultList.add(singleList);
        }
        return resultList;
    }

    /**
     * 取list中的值
     * @param stringList
     * @param index1 外层list的index
     * @param index2 内层list的index
     * @return
     */
    public static String getListItemValue(List<List<String>> stringList, int index1, int index2)
    {
        if(stringList.size() < (index1 + 1))
        {
            return null;
        }
        else
        {
            List<String> strings = stringList.get(index1);
            if(strings.size() < (index2 + 1))
            {
                return null;
            }
            else
            {
                return strings.get(index2);
            }
        }
    }
}
