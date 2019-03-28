package com.redpig.spider.se94se.actor;

import com.alibaba.fastjson.JSON;
import com.redpig.common.Service.BaseService;
import com.redpig.common.http.HttpClientManager;
import com.redpig.common.spring.AppCtxHolder;
import com.redpig.common.utils.Utils;
import com.redpig.spider.se94se.UrlCategoryMapper;
import com.redpig.spider.se94se.entity.MansResource;
import com.redpig.spider.se94se.entity.Seed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

/**
 * Created by hetao on 2018/6/23.
 */
public class Se94seSpider {
    private static final Logger LOG = LoggerFactory.getLogger(new Object() {
        //静态方法中获取当前类名
        public String getClassName() {
            String className = this.getClass().getName();
            return className.substring(0, className.lastIndexOf('$'));
        }
    }.getClassName());

    private static final String DOMAIN = "http://www.94aass.com";
    private static final String URL_REFERER = "http://www.94aass.com/94xx/05/yinqishunv/57_2.html";
    private static final String CURRENT_CRAWL_CATEGORY = "toupaishipin";

    private BaseService baseService = AppCtxHolder.getBean("baseService");

    public static void main(String[] args) throws Exception {
        new ClassPathXmlApplicationContext("context.xml");
        Se94seSpider spider = new Se94seSpider();
        spider.crawl();
    }

    public List<Seed> prepareSeeds(){
        List<Seed> seeds = new ArrayList<Seed>();
        HashMap<String, String> map = UrlCategoryMapper.getUrlCategoryMapper();
        for(Map.Entry<String, String> entry : map.entrySet()){
            Seed seed = new Seed();
            seed.setUrl(entry.getKey());
            seed.setCategory(entry.getValue());
            seeds.add(seed);
        }
        return seeds;
    }

    public List<Seed> prepareSeeds(String category){
        List<Seed> seeds = new ArrayList<Seed>();
        HashMap<String, String> map = UrlCategoryMapper.getUrlCategoryMapper();
        for(Map.Entry<String, String> entry : map.entrySet()){
            if(category.equals(entry.getValue())){
                Seed seed = new Seed();
                seed.setUrl(entry.getKey());
                seed.setCategory(entry.getValue());
                seeds.add(seed);
            }
        }
        return seeds;
    }


    public void crawl() {
        try {
            List<Seed> seeds = prepareSeeds();
            for(Seed seed : seeds){
                int totalPageNum = getTotalPage(seed);
                LOG.info("totalPageNum:" + totalPageNum);
                for (int i = 1; i < totalPageNum; i++) {
                    flipPage(seed, i);
                }
            }
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
    }

    private int getTotalPage(Seed seed) {
        try {
            String charset = "GBK";
            String content = HttpClientManager.instance(seed.getUrl().replace("%s", "1"))
                    .addHeader("Referer", URL_REFERER)
                    .getContent(charset);
            Document doc = Jsoup.parse(content, charset);
            String totalPageNum = doc.select(".pageinfo strong").get(0).text().trim();
            return Integer.parseInt(totalPageNum);
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
        return 0;
    }

    private void flipPage(Seed seed, int pageNum) {
        String pageUrl = seed.getUrl().replace("%s", String.valueOf(pageNum));
        try {
            String charset = "GBK";
            LOG.info("flip page:" + pageUrl);

            String content = HttpClientManager.instance(pageUrl)
                    .addHeader("Referer", URL_REFERER)
                    .getContent(charset);

            List<String> urlList = parseList(content, charset);
            for (String url : urlList) {
                flipDetailPage(seed, url, charset);
            }
        } catch (Exception e) {
            LOG.info("errUrl:" + pageUrl + "\n" + e.getMessage(), e);
        }
    }

    private void flipDetailPage(Seed seed, String url, String charset) throws Exception {
        try{
            String content = HttpClientManager.instance(url)
                    .addHeader("Referer", URL_REFERER)
                    .getContent(charset);
            parseDetail(seed, content, charset);
        }catch (Exception e){
            LOG.info("errUrl:" + url + "\n" + e.getMessage(), e);
        }
    }

    private List<String> parseList(String content, String charset) {
        List<String> urlList = new ArrayList<String>();
        Document doc = Jsoup.parse(content, charset);
        Elements elems = doc.select("ul.list-box li");
        for (Element elem : elems) {
            Element tmpElem = elem.select("a").get(1);
            if (Utils.isNotNull(tmpElem)) {
                urlList.add(DOMAIN + tmpElem.attr("href").trim());
            }
        }
        return urlList;
    }

    private void parseDetail(Seed seed, String content, String charset) {
        Document doc = Jsoup.parse(content, charset);
        Element elem = doc.select("div.details h3").get(0);
        String title = getTitle(elem);
        String picUrl = DOMAIN + doc.select("div.pic img").get(0).attr("src");
        Elements elems = doc.select("div.view-list .box a");
        String playPageUrl = "";
        String playLink = "";
        if(Utils.isNotEmpty(elems)){
            playPageUrl = DOMAIN + elems.get(0).attr("href").trim();
            playLink = elems.get(1).attr("href").trim();
        }
        MansResource resource = new MansResource();
        resource.setTitle(title);
        resource.setDescription(title);
        resource.setPicUrl(picUrl);
        resource.setPlayPageUrl(playPageUrl);
        resource.setPlayLink(playLink);
        resource.setCategory(seed.getCategory());
        resource.setWebSite(DOMAIN);
        LOG.info("crawl data:" + JSON.toJSONString(resource));
        save(resource);
    }

    private String getTitle(Element elem) {
        String title = elem.text();
        String typeStr = elem.select("p").get(0).text();
        return title.substring(0, title.lastIndexOf(typeStr));
    }

    private void save(MansResource entity) {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("playPageUrl", entity.getPlayPageUrl());
            List<MansResource> entitys = baseService.listByProps(MansResource.class, params);
            if (Utils.isNotEmpty(entitys)) {
                MansResource existEntity = entitys.get(0);
                Utils.copyNonNullProperties(entity, existEntity);
                existEntity.setUpdatedTime(new Date());
                baseService.save(existEntity);
            } else {
                baseService.save(entity);
            }
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
    }

}
