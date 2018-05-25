package com.agroneo.app.discuss.posts;

import com.agroneo.app.utils.Json;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostParser {


    public static String parse(String text, List<Json> docs) {

        text = text.replaceAll("\n\n", "</p><p>");
        text = text.replaceAll("\n", "<br/>");
        text = "<p>" + text + "</p>";
        text = text.replaceAll("\\[bold](.+?)\\[/bold]", "<strong>$1</strong>");
        text = text.replaceAll("\\[italic](.+?)\\[/italic]", "<em>$1</em>");
        text = text.replaceAll("\\[quote](.+?)\\[/quote]", "<blockquote>$1</blockquote>");

        Pattern pattern = Pattern.compile("\\[url=?([^]]+)?](.+?)\\[/url]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String text_ = matcher.group(2).replace("#", "@~X@X~@");
            String url_ = (matcher.group(1) == null) ? text_ : matcher.group(1).replace("#", "@~X@X~@");
            text = text.replace(matcher.group(0), "<a href=\"" + url_.replace("#", "@~X@X~@") + "\">" + text_ + "</a>");
        }

        text = Jsoup.clean(text, new Whitelist().addTags("strong", "a", "p", "br", "em", "blockquote").addAttributes("a", "href"));
        text = text.replaceAll("<[a-z]></[a-z]>", "");

        text = text.replaceAll("([\n]+)", "\n").replace("  ", "\t").replace("\t ", "\t");
        text = text.replaceAll("[\t ]+<p></p>\n", "");

        text = video(text);

        text = hasher(text);

        if (docs != null && docs.size() > 0) {
            text = docs(text, docs);
        }
        text = text.replace("@~X@X~@", "#");
        return text;
    }

    private static String docs(String text, List<Json> docs) {

        if (docs == null) {
            return text;
        }
        Pattern pattern = Pattern.compile("\\[Photos\\(([a-z0-9]+)\\)\\|?([^]]+)?]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        List<String> keys = new ArrayList<>();
        List<Json> new_docs = new ArrayList<>(docs);

        while (matcher.find()) {
            String key = matcher.group(1);
            String info = "";

            for (Json doc : docs) {
                if (doc.getId().equals(key)) {
                    new_docs.remove(doc);
                    if (!doc.getString("text", "").equals("")) {
                        info += "<span>" + doc.getString("text", "") + "</span>";
                    }
                }
            }

            if (matcher.group(2) != null && !matcher.group(2).replaceAll("([ ]+)", "").equals("")) {
                info += "<figcaption>" + matcher.group(2) + "</figcaption>";
            }
            text = text.replace(matcher.group(), "<figure class=\"img\">" + "<a href=\"/files/" + key + "\">" + "<picture>" + "<source srcset=\"https://agroneo.net/files/" + key + "@325\" media=\"(min-width: 0px) and (max-width: 361px)\">" + "<img src=\"/files/" + key + "@500\" />" + info + "</picture>" + "</a>" + "</figure>");
        }

        docs.clear();
        docs.addAll(new_docs);
        return text;

		/*
		<picture><source srcset="https://agroneo.net/ui/logo@25x25" media="(max-width: 700px)"><img src="https://agroneo.net/ui/logo@55x55"></picture>
		 */
    }

    private static String video(String text) {
        Map<String, Pattern> tubes = new HashMap<>();
        tubes.put("https://www.youtube.com/embed/", Pattern.compile("(?:https?://)?(?:youtu\\.be/|(?:www\\.)?youtube\\.com/(?:watch(?:\\.php)?\\?.*v=|v/|embed/))([a-zA-Z0-9\\-_]+)"));
        tubes.put("https://www.dailymotion.com/embed/video/", Pattern.compile("(?:https?://)(?:www.)?(?:dailymotion.com\\/(?:video|hub)|dai.ly)/([^_ <]+)"));
        tubes.put("https://player.vimeo.com/video/", Pattern.compile("(?:https?://)(?:www\\.|player\\.)?vimeo.com/([0-9]+)"));
        tubes.put("https://ok.ru/videoembed/", Pattern.compile("(?:https?://)(?:m\\.)?ok.ru/(?:video/)?([0-9]+)"));

        for (Map.Entry<String, Pattern> item : tubes.entrySet()) {
            text = text.replaceAll(item.getValue().pattern(), "<iframe class=\"video\" width=\"560\" height=\"315\" src=\"" + item.getKey() + "$1\" frameborder=\"0\"  gesture=\"media\" allow=\"encrypted-media\" referrerpolicy=\"no-referrer\" allowfullscreen></iframe>");
        }
        return text;
    }

    private static String hasher(String text) {
        String punct = "?!¿.,;:";
        Pattern pattern = Pattern.compile("#([^\\p{javaWhitespace}" + punct + "]+)([\\p{javaWhitespace}" + punct + "])?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            try {
                text = text.replace(matcher.group(), "<a href=\"/hashtag/" + URLEncoder.encode(matcher.group(1).toLowerCase(), "UTF-8") + "\">#" + matcher.group(1).replace("_", " ") + "</a>" + (matcher.group(2) != null ? matcher.group(2) : ""));
            } catch (Exception e) {

            }
        }
        return text;
    }
}
