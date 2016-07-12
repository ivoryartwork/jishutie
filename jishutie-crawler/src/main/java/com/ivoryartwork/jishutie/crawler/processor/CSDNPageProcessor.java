package com.ivoryartwork.jishutie.crawler.processor;

import com.ivoryartwork.jishutie.crawler.pipeline.ConsolePipeline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * Created by Yaochao on 2016/7/12.
 */
public class CSDNPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(0);

    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("(http://blog\\.csdn\\.net/\\w+/article/details/\\w+)").all());
        String author = page.getHtml().xpath("//a[@class='user_name']/text()").toString();
        List categories = page.getHtml().xpath("//span[@class='link_categories']/a/text()").all();
        String title = page.getHtml().xpath("//span[@class='link_title']/a/text()").toString();
        String postdate = page.getHtml().xpath("//div[@id='article_details']//span[@class='link_postdate']/text()").toString();
        if (author == null || categories == null || title == null || postdate == null) {
            page.setSkip(true);
        } else {
            page.putField("author", author);
            page.putField("categories", categories);
            page.putField("title", title);
            page.putField("postdate", postdate);
        }
//        page.putField("content", page.getHtml().xpath("//div[@id='article_content']/tidyText()").toString());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new CSDNPageProcessor()).addUrl("http://blog.csdn.net/web/index.html").addPipeline(new ConsolePipeline()).thread(5).run();
    }
}
