package com.example.monikasaini.preview.utility;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Monika on 03/06/17.
 */

public class JsoupUtility {


    public static final String METATAG_PATTERN = "<meta(.*?)>";
    public static final String METATAG_CONTENT_PATTERN = "content=\"(.*?)\"";


    public static String pregMatch(String content, String pattern, int index) {

        String match = "";
        Matcher matcher = Pattern.compile(pattern).matcher(content);

        while (matcher.find()) {
            match = matcher.group(index);
            break;
        }

        return extendedTrim(match);
    }

    public static List<String> pregMatchAll(String content, String pattern,
                                            int index) {

        List<String> matches = new ArrayList<String>();
        Matcher matcher = Pattern.compile(pattern).matcher(content);

        while (matcher.find()) {
            matches.add(extendedTrim(matcher.group(index)));
        }

        return matches;
    }

    /**
     * Removes extra spaces and trim the string
     */
    public static String extendedTrim(String content) {
        return content.replaceAll("\\s+", " ").replace("\n", " ")
                .replace("\r", " ").trim();
    }


    public static boolean isImage(String url) {
        return url.matches("(.+?)\\.(jpg|png|gif|bmp)$");
    }

    public static HashMap<String, String> getMetaTags(String content) {

        HashMap<String, String> metaTags = new HashMap<String, String>();
        metaTags.put("url", "");
        metaTags.put("title", "");
        metaTags.put("description", "");
        metaTags.put("image", "");

        List<String> matches = pregMatchAll(content,
                METATAG_PATTERN, 1);

        for (String match : matches) {
            final String lowerCase = match.toLowerCase();
            if (lowerCase.contains("property=\"og:url\"")
                    || lowerCase.contains("property='og:url'")
                    || lowerCase.contains("name=\"url\"")
                    || lowerCase.contains("name='url'"))
                updateMetaTag(metaTags, "url", separeMetaTagsContent(match));
            else if (lowerCase.contains("property=\"og:title\"")
                    || lowerCase.contains("property='og:title'")
                    || lowerCase.contains("name=\"title\"")
                    || lowerCase.contains("name='title'"))
                updateMetaTag(metaTags, "title", separeMetaTagsContent(match));
            else if (lowerCase
                    .contains("property=\"og:description\"")
                    || lowerCase
                    .contains("property='og:description'")
                    || lowerCase.contains("name=\"description\"")
                    || lowerCase.contains("name='description'"))
                updateMetaTag(metaTags, "description", separeMetaTagsContent(match));
            else if (lowerCase.contains("property=\"og:image\"")
                    || lowerCase.contains("property='og:image'")
                    || lowerCase.contains("name=\"image\"")
                    || lowerCase.contains("name='image'"))
                updateMetaTag(metaTags, "image", separeMetaTagsContent(match));
        }

        return metaTags;
    }

    public static void updateMetaTag(HashMap<String, String> metaTags, String url, String value) {
        if (value != null && (value.length() > 0)) {
            metaTags.put(url, value);
        }
    }

    /**
     * Gets content from metatag
     */
    public static String separeMetaTagsContent(String content) {
        String result = pregMatch(content, METATAG_CONTENT_PATTERN,
                1);
        return htmlDecode(result);
    }

    public static String htmlDecode(String content) {
        return Jsoup.parse(content).text();
    }

    public static String stripTags(String content) {
        return Jsoup.parse(content).text();
    }

    public static String crawlCode(String content) {
        String result = "";
        String resultSpan = "";
        String resultParagraph = "";
        String resultDiv = "";
        resultSpan = getTagContent("span", content);
        resultParagraph = getTagContent("p", content);
        resultDiv = getTagContent("div", content);
        if (resultParagraph.length() > resultSpan.length() && resultParagraph.length() >= resultDiv.length()) {
            result = resultParagraph;
        } else if (resultParagraph.length() > resultSpan.length() && resultParagraph.length() < resultDiv.length()) {
            result = resultDiv;
        } else {
            result = resultParagraph;
        }

        return htmlDecode(result);
    }

    public static String getTagContent(String tag, String content) {
        String pattern = "<" + tag + "(.*?)>(.*?)</" + tag + ">";
        String result = "";
        String currentMatch = "";
        List matches = pregMatchAll(content, pattern, 2);
        int matchesSize = matches.size();

        for (int matchFinal = 0; matchFinal < matchesSize; ++matchFinal) {
            currentMatch = stripTags((String) matches.get(matchFinal));
            if (currentMatch.length() >= 120) {
                result = extendedTrim(currentMatch);
                break;
            }
        }

        if (result.equals("")) {
            String var9 = pregMatch(content, pattern, 2);
            result = extendedTrim(var9);
        }

        result = result.replaceAll("&nbsp;", "");
        return htmlDecode(result);
    }

    public static List<String> getImages(Document document, int imageQuantity) {
        List<String> matches = new ArrayList<String>();

        Elements media = document.select("[src]");

        for (Element srcElement : media) {
            if (srcElement.tagName().equals("img")) {
                matches.add(srcElement.attr("abs:src"));
            }
        }

        matches = matches.subList(0, imageQuantity);

        return matches;
    }

    public static String getMetaImageUrl(Document doc) {
        Elements metaOgImage = doc.select("meta[property=og:image]");
        if (metaOgImage != null) {
            return metaOgImage.attr("content");
        }
        return null;
    }

    public static String getMetaTitle(Document doc) {
        Elements metaOgTitle = doc.select("meta[property=og:title]");
        if (metaOgTitle != null) {
            return metaOgTitle.attr("content");
        } else {
            return doc.title();
        }
    }

    public static String getMetaDescription(Document doc) {
        Elements metaOgTitle = doc.select("meta[property=og:description]");
        if (metaOgTitle != null) {
            return metaOgTitle.attr("content");
        } else {
            return null;
        }
    }

}
