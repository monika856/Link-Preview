package com.example.monikasaini.preview.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Monika on 06/06/17.
 */

public class PageData {

    private boolean success = false;
    private String htmlCode = "";
    private String title = "";
    private String description = "";
    private String url = "";
    private String finalUrl = "";
    private HashMap<String, String> metaTags = new HashMap<String, String>();

    private List<String> images = new ArrayList<String>();
    private String displayImage;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getHtmlCode() {
        return htmlCode;
    }

    public void setHtmlCode(String htmlCode) {
        this.htmlCode = htmlCode;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFinalUrl() {
        return finalUrl;
    }

    public void setFinalUrl(String finalUrl) {
        this.finalUrl = finalUrl;
    }


    public HashMap<String, String> getMetaTags() {
        return metaTags;
    }

    public void setMetaTags(HashMap<String, String> metaTags) {
        this.metaTags = metaTags;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(String displayImage) {
        this.displayImage = displayImage;
    }
}
