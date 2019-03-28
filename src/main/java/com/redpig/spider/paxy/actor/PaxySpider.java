package com.redpig.spider.paxy.actor;

import com.alibaba.fastjson.JSON;
import com.redpig.common.http.HttpClientManager;
import com.redpig.common.utils.Utils;
import com.redpig.spider.paxy.entity.ArticleInfo;
import com.redpig.spider.paxy.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hetao on 2018/6/20.
 */
public class PaxySpider {
    private static final Logger LOG = LoggerFactory.getLogger(PaxySpider.class);

    private static final String DEFAULT_PASSWORD = "888888";

    public PaxySpider() {
    }

    public void autoClickArticles() {
        List<UserInfo> userInfos = crawlUserInfos();
        for (UserInfo userInfo : userInfos) {
            List<ArticleInfo> articleInfos = crawlArticleList(userInfo);
            for (ArticleInfo articleInfo : articleInfos) {
                try {
                    String content = HttpClientManager.instance(PaxyConsts.URL_FILE_READ, HttpClientManager.METHOD_POST)
                            .addParam(PaxyConsts.STR_USER_CODE, userInfo.getUserCode())
                            .addParam(PaxyConsts.STR_FILE_EXPAND_CODE, articleInfo.getFileExpandCode())
                            .getContent();
                    LOG.info(userInfo.getUserRealName() + " 点击了 '" + articleInfo.getFileTitle() + "'");
                    if (Utils.isNotBlank(content) && content.length() < 100) {
                        LOG.info("返回结果：" + content);
                    } else {
                        LOG.info("返回结果：已点击");
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

    public List<UserInfo> crawlUserInfos() {
        List<UserInfo> userInfos = new ArrayList<UserInfo>();
        int start = 7301;
        for (int i = start; i < start + 50; i++) {
            try {
                String userName = PaxyConsts.STR_LXCH_PREFIX + getIncreaseNumStr(i);
                String content = HttpClientManager.instance(PaxyConsts.URL_USER_LOGIN, HttpClientManager.METHOD_POST)
                        .addParam(PaxyConsts.STR_USERNAME, userName)
                        .addParam(PaxyConsts.STR_PASSWORD, DEFAULT_PASSWORD)
                        .getContent();
                UserInfo userInfo = JSON.parseObject(content, UserInfo.class);
                if (Utils.isNotNull(userInfo)) {
                    LOG.info("userName[" + userInfo.getUserRealName() + "](" + userInfo.getUserName() + "):"
                            + JSON.toJSONString(userInfo));
                    userInfos.add(userInfo);
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return userInfos;
    }

    public List<ArticleInfo> crawlArticleList(UserInfo userInfo) {
        List<ArticleInfo> articleInfos = new ArrayList<ArticleInfo>();
        try {
            String content = HttpClientManager.instance(PaxyConsts.URL_ARTICLE_LIST, HttpClientManager.METHOD_POST)
                    .addParam(PaxyConsts.STR_TAGS, "四川省,四川省泸州市,四川省泸州市泸县,四川省泸州市泸县泸县潮河镇学校,四川省泸州市泸县泸县潮河镇学校七年级3班")
                    .addParam(PaxyConsts.STR_PAGE_NUM, "1")
                    .addParam(PaxyConsts.STR_PAGE_SIZE, "15")
                    .addParam(PaxyConsts.STR_USER_CODE, userInfo.getUserCode())
                    .addParam(PaxyConsts.STR_ROLE_CODE, userInfo.getRoleCode())
                    .addParam(PaxyConsts.STR_ORG_CODE, userInfo.getOrgCode())
                    .getContent();
            articleInfos.addAll(JSON.parseArray(content, ArticleInfo.class));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return articleInfos;
    }


    private String getIncreaseNumStr(int i) {
        String numStr = "0000";
        if (i >= 0) {
            if (i < 10) {
                numStr = "000" + String.valueOf(i);
            } else if (i < 100) {
                numStr = "00" + String.valueOf(i);
            } else if (i < 1000) {
                numStr = "0" + String.valueOf(i);
            } else if (i < 10000) {
                numStr = String.valueOf(i);
            }
        }
        return numStr;
    }

    public static void main(String[] args) {
        PaxySpider spider = new PaxySpider();
        spider.autoClickArticles();
    }

}
