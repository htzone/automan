package com.redpig.spider.se94se;

import java.util.HashMap;

/**
 * Created by hetao on 2018/7/3.
 */
public class UrlCategoryMapper {

    private static HashMap<String, String> map = new HashMap<String, String>();

    static {
        map.put("http://www.94aass.com/94xx/05/yinqishunv/57_%s.html", "yinqishunv");
        map.put("http://www.94aass.com/94xx/05/zhongwenzimu/54_%s.html", "zhongwenzimu");
        map.put("http://www.94aass.com/94xx/05/toupaishipin/53_%s.html", "toupaishipin");
        map.put("http://www.94aass.com/94xx/05/biantainuenuedai/58_%s.html", "biantainuenuedai");
    }

    public static HashMap<String, String> getUrlCategoryMapper(){
        return map;
    }

}
