package com.example.monikasaini.preview.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.monikasaini.preview.events.EventDataFetched;
import com.example.monikasaini.preview.models.PageData;
import com.example.monikasaini.preview.utility.JsoupUtility;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;

/**
 * Created by Monika on 06/06/17.
 */

public class GetDataService extends IntentService {
    private PageData pageData = new PageData();


    public GetDataService() {
        super("GetDataService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String url = intent.getStringExtra("url");
        pageData.setFinalUrl(url);

        if (!pageData.getFinalUrl().equals("")) {
            if (JsoupUtility.isImage(pageData.getFinalUrl()) && !pageData.getFinalUrl().contains("dropbox")) {
                pageData.setSuccess(true);
                pageData.getImages().add(pageData.getFinalUrl());
                pageData.setTitle("");
                pageData.setDescription("");
            } else {
                try {
                    Document finalLinkSet = Jsoup.connect(pageData.getFinalUrl()).userAgent("Mozilla").get();
                    pageData.setHtmlCode(JsoupUtility.extendedTrim(finalLinkSet.toString()));
                    HashMap metaTags = JsoupUtility.getMetaTags(pageData.getHtmlCode());
                    pageData.setMetaTags(metaTags);
                    pageData.setTitle(JsoupUtility.getMetaTitle(finalLinkSet));
                    if (JsoupUtility.getMetaDescription(finalLinkSet) != null)
                        pageData.setDescription(JsoupUtility.getMetaDescription(finalLinkSet));
                    else
                        pageData.setDescription((String) metaTags.get("description"));
                    if (pageData.getTitle().equals("")) {
                        String matchTitle = JsoupUtility.pregMatch(pageData.getHtmlCode(), "<title(.*?)>(.*?)</title>", 2);
                        if (!matchTitle.equals("")) {
                            pageData.setTitle(JsoupUtility.htmlDecode(matchTitle));
                        }
                    }

                    if (pageData.getDescription().equals("")) {
                        pageData.setDescription(JsoupUtility.crawlCode(pageData.getHtmlCode()));
                    }

                    pageData.setDescription(pageData.getDescription().replaceAll("<script(.*?)>(.*?)</script>", ""));
                    if (JsoupUtility.getMetaImageUrl(finalLinkSet) != null) {
                        pageData.setDisplayImage(JsoupUtility.getMetaImageUrl(finalLinkSet));
                    } else
                        pageData.setImages(JsoupUtility.getImages(finalLinkSet, 1));
                    pageData.setSuccess(true);
                    String[] finalLinkSet1 = pageData.getFinalUrl().split("&");
                    pageData.setUrl(finalLinkSet1[0]);
                    pageData.setDescription(JsoupUtility.stripTags(pageData.getDescription()));

                    EventBus.getDefault().post(new EventDataFetched(pageData));
                } catch (Exception var5) {
                    pageData.setSuccess(false);
                    EventBus.getDefault().post(new EventDataFetched(pageData));
                }
            }
        }


    }
}
